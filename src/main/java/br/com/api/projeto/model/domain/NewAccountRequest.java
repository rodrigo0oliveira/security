package br.com.api.projeto.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewAccountRequest {
	
	private String name;
	
	private String password;
	
	private String roleName;
	

}
