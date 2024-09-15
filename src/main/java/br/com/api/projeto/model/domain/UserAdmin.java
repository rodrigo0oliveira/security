package br.com.api.projeto.model.domain;

import java.util.Collection;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserAdmin extends User{
	
	private static final long serialVersionUID = 1L;
	
	private String position;
	
	public UserAdmin(String id,String username,String password
			,String document,Collection<Roles> roles,String position) {
		
		super(id,username,password,document,roles);
		this.position = position;
	}

}
