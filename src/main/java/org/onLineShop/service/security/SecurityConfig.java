package org.onLineShop.service.security;

import org.onLineShop.service.security.oauth2.CustomOAuth2UserService;
import org.onLineShop.service.security.oauth2.Oauth2LonginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomOAuth2UserService oauth2UserService;
	@Autowired
	private Oauth2LonginSuccessHandler oauth2LonginSuccessHandler;
	
	@Bean
	public UserDetailsService UserDetailsService() {
		return new AppUserDetails(); 
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider Authentication() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(UserDetailsService());
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.formLogin();
		http.headers().frameOptions().disable();
		http.authorizeHttpRequests().anyRequest().permitAll();
		//http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/saveUser/**","/saveRole/**").hasAuthority("ADMIN");
		//http.authorizeHttpRequests().requestMatchers("/facebook/**","/login/**","/oauth2/**","/h2-console/**"/*,"/refreshToken/**","/accounts/user/**"*/).permitAll();
		//http.authorizeHttpRequests().anyRequest().authenticated();
        //http.formLogin().permitAll().loginPage("/loginPage.html");
        //http.logout().permitAll();
		http.oauth2Login()
		     //.loginPage("/login")
		     .userInfoEndpoint()
		     .userService(oauth2UserService)
		     .and()
		     .successHandler(oauth2LonginSuccessHandler);
		http.addFilter(new JwtAuthentificationFilter(Authentication()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();	
	}
	
}
