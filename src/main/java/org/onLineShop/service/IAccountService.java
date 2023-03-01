package org.onLineShop.service;

import java.io.IOException;
import java.util.List;

import org.onLineShop.entity.UserApp;
import org.onLineShop.entity.UserRole;
import org.onLineShop.service.from.UrlData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


public interface IAccountService {
	public String uploadDirectory();
	public byte[] getImage(long id)throws IOException;
	public byte[] uploadPhoto(MultipartFile file, Long id) throws Exception;
	public UserApp addUser(UserApp user );
	public UserRole addRole(UserRole Role);
	public boolean deleteUser(long id)throws IOException;
	public boolean deleteRole(long id);
	public UserApp loadUserByUsername(String username);
	public UserApp getOneUser(long id);
	public UserRole getOneRole(long id);
	public boolean addRoleToUser(UrlData UrlData);
	public boolean removeRoleUser(UrlData UrlData);
	public UserApp updateUser(UserApp user );
	public UserRole updateRole(UserRole Role);
	public Page<UserApp> getAlluser(PageRequest pageRequest);
	public List<UserRole> getAllRole();
	public boolean deletePhotoIfExist(long id) throws IOException;
	public UserApp verifyUsername(UrlData urlData);
	public UserRole findRoleByRoleName(String roleName);
	public void saveAndUpdateOauthUser(UserApp user);
	public UserApp findUserByEmailAdress(String email);
}
