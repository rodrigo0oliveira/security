package br.com.api.projeto.model.domain.enums;

import lombok.Getter;

@Getter
public enum RoomType {
	
	SOLTEIRO("SOLTEIRO"),
	CASAL("CASAL"),
	SUÍTE("SUÍTE");
	
	private String name;
	
	RoomType(String name){
		this.name = name;
	}
	
	

}
