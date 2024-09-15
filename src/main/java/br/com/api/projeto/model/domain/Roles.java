package br.com.api.projeto.model.domain;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@EqualsAndHashCode(of = "id")
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Roles implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Getter 
	@Column(name = "role_id")
	private String id;
	
	@Column(name = "name")
	@Getter @Setter 
	private String name;
	

	public Roles(String name) {
		this.name = name;
	}


	@Override
	public String getAuthority() {
		return name;
	}
	
	
	
	

}
