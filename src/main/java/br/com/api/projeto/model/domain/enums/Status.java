package br.com.api.projeto.model.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
	
	EMRESERVA("EM RESERVA"),
	DISPONÍVEL("DISPONÍVEL");
	
	private String name;
	
	Status(String name){
		this.name = name;
	}
	
	

}
