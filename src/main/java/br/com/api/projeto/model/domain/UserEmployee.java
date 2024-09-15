package br.com.api.projeto.model.domain;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEmployee extends User{

	private static final long serialVersionUID = 1L;
	
	private String position;
	
	private BigDecimal salary;

}
