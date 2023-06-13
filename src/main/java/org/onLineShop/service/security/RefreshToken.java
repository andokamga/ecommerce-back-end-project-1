package org.onLineShop.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.onLineShop.entity.UserApp;
import org.onLineShop.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class RefreshToken {
	@Autowired
	public IAccountService iAccountService;
	public boolean jwtRefreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String authorizationToken = request.getHeader(JwtUtil.AUTH_HEADER);
		if(authorizationToken!=null && authorizationToken.startsWith(JwtUtil.PREFIX)){
			try {
				String jwt = authorizationToken.substring(JwtUtil.PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
				JWTVerifier JwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJwt = JwtVerifier.verify(jwt);
				String userName = decodedJwt.getSubject();
				System.out.println(decodedJwt);
				System.out.println(userName);
				UserApp user = iAccountService.loadUserByUsername(userName);
				String jwtaccessToken = JWT.create()
						                   .withSubject(user.getUserName())
						                   .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_ACCESS_TOKEN))
						                   .withIssuer(request.getRequestURI().toString())
						                   .withClaim("roles",user.getUserRoles().stream().map(r->r.getUserRoleName()).collect(Collectors.toList()))
						                   .sign(algorithm);
				Map<String, String> idToken = new HashMap<>();
				idToken.put("accessToken", jwtaccessToken);
				idToken.put("refreshToken", jwt);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
				return true;
			} catch (Exception e) {
				throw e;
			}
			
		}
		else {
			throw new RuntimeException("refresh token required !!!");
		}
	}

}
