package br.com.api.projeto.model.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TokenResponse {
	
	private String token;
	
	private long expiresIn;
	
	private String username;

	

}
