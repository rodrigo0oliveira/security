package br.com.api.projeto.model.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {
	
	private String name;
	
	private String password;
	
	private String document;
	
	private String roleName;

}
