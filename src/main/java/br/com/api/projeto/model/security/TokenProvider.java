package br.com.api.projeto.model.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import br.com.api.projeto.model.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
	
	private static final int UNAUTHORIZED = 401;
	
	private final ObjectMapper objectMapper;
	
	@Value("${jwt.key}")
	private String jwtKey;
	
	@Value("${jwt.expiration-time}")
	private Integer expirationTime;
	
	public TokenResponse generateToken(Authentication authentication) throws JsonProcessingException {
		
		final Date now = new Date();
		
		long expirationInMillis = expirationTime.longValue();
		
		Date expirationDate = new Date(System.currentTimeMillis()+expirationInMillis);
		
		final User user = getUsuario(authentication);
		
		
		final String auth = Jwts.builder()
				.setIssuer("WEB TOKEN")
				.setSubject(user.toString())
				.setIssuedAt(now)
				.setNotBefore(now)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, jwtKey.getBytes(StandardCharsets.UTF_8))
				.compact();
		
		return TokenResponse.builder()
                .token(auth)
                .expiresIn(expirationInMillis)
                .username(user.getUsername())
                .build();
		
		
	}
	
	public boolean isValid(String jwt,ServletResponse servletResponse) throws IOException {
		try {
			jwt = extractToken(jwt);
			SignedJWT signedJWT = SignedJWT.parse(jwt);
			JWSVerifier jwsVerifier = new MACVerifier(jwtKey.getBytes(StandardCharsets.UTF_8));
			if(!signedJWT.verify(jwsVerifier)) {
				log.error("Assinatura inválida");
                ((HttpServletResponse) servletResponse).sendError(UNAUTHORIZED);
                return false;
			}
			Jwts.parser().setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8))
			.parseClaimsJws(jwt);
			return true;
		}catch (Exception e) {
			log.error("Token inválido : {}"+e.getMessage());
			((HttpServletResponse)servletResponse).sendError(UNAUTHORIZED);
			return false;
		}
	}
	
	public User getUserFromToken(String jwt) throws JsonMappingException, JsonProcessingException {
		jwt = extractToken(jwt);
		Claims claims = Jwts.parser().setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8))
				.parseClaimsJws(jwt).getBody();
		
		return objectMapper.readValue(claims.getSubject(),User.class);
	}
	

	private String extractToken(String authToken) {
		if(authToken.toLowerCase().startsWith("bearer")) {
			return authToken.substring("bearer ".length());
		}
		return authToken;
	}

	public User getUsuario(Authentication authentication) {
		return (User) authentication.getPrincipal();
	}
	
	

	

}
