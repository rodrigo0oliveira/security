package br.com.api.projeto.model.domain;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(of = "id")
@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_roles")
public class Roles implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	@Column(name = "role_id")
	private String id;
	
	@Column(name = "name")
	private String name;
	

	public Roles(String name) {
		this.name = name;
	}


	@Override
	public String getAuthority() {
		return name;
	}
	
	@Override
	public String toString() {
        return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\"}";
    }
	
	
	
	

}
