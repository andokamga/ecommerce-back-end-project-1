package org.onLineShop.service.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends OncePerRequestFilter{
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		if(request.getServletPath().equals("/refreshToken")||request.getServletPath().equals("/login")) {
			filterChain.doFilter(request,response);
		}
		else {
			String authorizationToken = request.getHeader(JwtUtil.AUTH_HEADER);
			if(authorizationToken!=null && authorizationToken.startsWith(JwtUtil.PREFIX)){
				try {
					String jwt = authorizationToken.substring(JwtUtil.PREFIX.length());
					Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
					JWTVerifier JwtVerifier = JWT.require(algorithm).build();
					DecodedJWT decodedJwt = JwtVerifier.verify(jwt);
					String userName = decodedJwt.getSubject();
					String[] roles = decodedJwt.getClaim("roles").asArray(String.class);
					Collection<GrantedAuthority> authorities = new ArrayList<>();
					for(String r:roles) {
						authorities.add(new SimpleGrantedAuthority(r));
					}
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,null,authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request,response);
				} catch (Exception e) {
					response.setHeader("error_message", e.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				
			}
			else {
				filterChain.doFilter(request,response);
			}
		}
		
	}

}
