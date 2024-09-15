package br.com.api.projeto.model.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserGuest extends User{

	private static final long serialVersionUID = 1L;
	
	private LocalDate birthDate;
	
	private String adress;
	
	private String phoneNumber;

	

}
