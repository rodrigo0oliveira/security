package br.com.api.projeto.model.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;

}
