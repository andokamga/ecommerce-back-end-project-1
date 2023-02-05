package org.onLineShop.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.onLineShop.dao.UserAppRepository;
import org.onLineShop.dao.UserRoleRepository;
import org.onLineShop.entity.UserApp;
import org.onLineShop.entity.UserRole;
import org.onLineShop.service.from.UrlData;
import org.onLineShop.service.from.UtilDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class IAccountServiceImpl implements IAccountService{
	@Autowired
	public UserRoleRepository userRoleRepository;
	@Autowired
	public UserAppRepository userAppRepository;
	@Autowired
	public PasswordEncoder PasswordEncoder;
	@Override
	public String uploadDirectory() {
		return System.getProperty("user.home")+"/shop/profils";
	}
	@Override
	public UserApp addUser(UserApp user) {
		String roleName="USER";
		if(userRoleRepository.findByUserRoleNameContains(roleName)!=null) {
			UserRole role  =  userRoleRepository.findByUserRoleNameContains(roleName);
			String password = user.getPassword();
			user.setUserImage("Unknown.jpg");
			user.setPassword(PasswordEncoder.encode(password));
			user.getUserRoles().add(role);
			return userAppRepository.save(user);
		}
		return null;
	}

	@Override
	public UserRole addRole(UserRole role) {
		return userRoleRepository.save(role);
		
	}

	@Override
	public boolean deleteUser(long id) throws IOException {
		if(userAppRepository.findById(id).isPresent()) {
			deletePhotoIfExist(id);
			UserApp user = userAppRepository.findById(id).get();
			userRoleRepository.findAll().forEach(role->{
				if(role.getUserApps().contains(user)) {
					role.getUserApps().remove(user);
				}
			});
			deletePhotoIfExist(id);
			userAppRepository.delete(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteRole(long id) {
		if(userRoleRepository.findById(id).isPresent()) {
			UserRole role = userRoleRepository.findById(id).get();
			userAppRepository.findAll().forEach(user->{
				if(user.getUserRoles().contains(role)) {
					user.getUserRoles().remove(role);
				}
			});
			userRoleRepository.delete(role);
			return true;
		}
		return false;
	}

	@Override
	public UserApp getOneUser(long id) {
		if(userAppRepository.findById(id).isPresent()) {
			return userAppRepository.findById(id).get();
		}
		return null;
	}

	@Override
	public UserRole getOneRole(long id) {
		if(userRoleRepository.findById(id).isPresent()) {
			return userRoleRepository.findById(id).get();
		}
		return null;
	}

	@Override
	public UserApp updateUser(UserApp user) {
		return userAppRepository.save(user);
		
	}

	@Override
	public UserRole updateRole(UserRole role) {
		return userRoleRepository.save(role);
		
	}

	@Override
	public byte[] uploadPhoto(MultipartFile file, Long id) throws Exception {
		//StringBuilder fileName = new StringBuilder();
		if(userAppRepository.findById(id).isPresent()) {
			deletePhotoIfExist(id);
			String filename = id+ file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")) ;
			Path filenamePath = Paths.get(uploadDirectory(),filename);
			Files.write(filenamePath, file.getBytes());
			UserApp user = userAppRepository.findById(id).get();
			user.setUserImage(filename);
			userAppRepository.save(user);
			return getImage(id);
		}
		return null;
	}


	@Override
	public byte[] getImage(long id) throws IOException {
		if(userAppRepository.findById(id).isPresent()) {
			UserApp user = userAppRepository.findById(id).get();
			String userImage = user.getUserImage();
			File file = new File(uploadDirectory()+"/"+userImage);
			Path path = Paths.get(file.toURI());
			if(!Files.exists(path)){
				File defautFile = new File(UtilDate.PROFIL_IMAGE+userImage);
				Path defautFath = Paths.get(defautFile.toURI());
				return Files.readAllBytes(defautFath);
			}
			return Files.readAllBytes(path);
		}
		return null;
	}
	@Override
	public boolean deletePhotoIfExist(long id) throws IOException {
		if(userAppRepository.findById(id).isPresent()) {
			String filename = userAppRepository.findById(id).get().getUserImage() ;
			Path filenamePath = Paths.get(uploadDirectory(),filename);
			if(Files.deleteIfExists(filenamePath)) {
				UserApp user = userAppRepository.findById(id).get();
				user.setUserImage("Unknown.jpg");
				userAppRepository.save(user);
				return true;
			}
			
		}
		return false;
		
	}

	@Override
	public UserApp loadUserByUsername(String username) {
		return userAppRepository.findByUserName(username);
	}

	@Override
	public boolean addRoleToUser(UrlData urlData) {
		if(userAppRepository.findById(urlData.getIdUser()).isPresent()) {
			if(userRoleRepository.findById(urlData.getIdRole()).isPresent()) {
				UserApp user = userAppRepository.findById(urlData.getIdUser()).get();
				UserRole role = userRoleRepository.findById(urlData.getIdRole()).get();
				user.getUserRoles().add(role);
				role.getUserApps().add(user);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeRoleUser(UrlData UrlData) {
		if(userAppRepository.findById(UrlData.getIdUser()).isPresent()) {
			if(userRoleRepository.findById(UrlData.getIdRole()).isPresent()) {
				UserApp user = userAppRepository.findById(UrlData.getIdUser()).get();
				UserRole role = userRoleRepository.findById(UrlData.getIdRole()).get();
				user.getUserRoles().remove(role);
				role.getUserApps().remove(user);
				return true;
			}
		}
		return false;
	}

	@Override
	public Page<UserApp> getAlluser(PageRequest pageRequest) {
		return userAppRepository.findAll(pageRequest);
	}

	@Override
	public List<UserRole> getAllRole() {
		return userRoleRepository.findAll();
	}
	@Override
	public UserApp verifyUsername(UrlData urlData) {
		return userAppRepository.findByUserName(urlData.getUsername());
	}

}
