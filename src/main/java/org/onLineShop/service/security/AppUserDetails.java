package org.onLineShop.service.security;

import java.util.ArrayList;
import java.util.Collection;

import org.onLineShop.entity.UserApp;
import org.onLineShop.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class AppUserDetails implements UserDetailsService {
	@Autowired
	private IAccountService iAccountService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserApp user = iAccountService.loadUserByUsername(username);
		if(user!=null) {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			user.getUserRoles().forEach(role->{
				authorities.add(new SimpleGrantedAuthority(role.getUserRoleName()));
			});
			return new User(user.getUserName(),user.getPassword(),authorities);
		}
		return null;
	}

}
