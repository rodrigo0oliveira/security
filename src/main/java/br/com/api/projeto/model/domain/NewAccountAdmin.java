package br.com.api.projeto.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewAccountAdmin extends NewAccountRequest{
	
	private String position;

}
