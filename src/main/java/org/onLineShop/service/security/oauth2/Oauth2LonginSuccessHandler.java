package org.onLineShop.service.security.oauth2;

import java.io.IOException;

import org.onLineShop.entity.UserApp;
import org.onLineShop.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class Oauth2LonginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private IAccountService iAccountService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		  CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
			UserApp user = iAccountService.findUserByEmailAdress(oAuth2User.getEmail());
			if(user!=null) {
				user.setEmail(oAuth2User.getEmail());
				user.setUserName(oAuth2User.getName());
	            iAccountService.saveAndUpdateOauthUser(user);
			}else {
				UserApp u = new UserApp();
				u.setEmail(oAuth2User.getEmail());
				u.setUserName(oAuth2User.getName());
				u.setActive(true);
	            iAccountService.saveAndUpdateOauthUser(u);
			}
			super.onAuthenticationSuccess(request, response, authentication);
		
	}
	

}
