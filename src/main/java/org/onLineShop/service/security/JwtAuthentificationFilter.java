package org.onLineShop.service.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthentificationFilter extends UsernamePasswordAuthenticationFilter {
	public DaoAuthenticationProvider DaoAuthenticationProvider;
	
	public JwtAuthentificationFilter(DaoAuthenticationProvider daoAuthenticationProvider) {
		this.DaoAuthenticationProvider = daoAuthenticationProvider;
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = request.getParameter("userName");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken AuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
		return DaoAuthenticationProvider.authenticate(AuthenticationToken);
	}
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
		String jwtaccessToken = JWT.create()
				                   .withSubject(user.getUsername())
				                   .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_ACCESS_TOKEN))
				                   .withIssuer(request.getRequestURI().toString())
				                   .withClaim("roles",user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
				                   .sign(algorithm);
		String jwtrefreshaccessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_REFRESH_TOKEN))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);
		//response.setHeader("Authorisetion", jwtaccessToken);
		Map<String, String> idToken = new HashMap<>();
		idToken.put("access-token", jwtaccessToken);
		idToken.put("refresh-token", jwtrefreshaccessToken);
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), idToken);
	}

}
