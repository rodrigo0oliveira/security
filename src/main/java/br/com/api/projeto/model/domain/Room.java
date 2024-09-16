package br.com.api.projeto.model.domain;

import java.math.BigDecimal;

import br.com.api.projeto.model.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_rooms")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;
	
	@Column(name = "roomNumber",unique = true)
	private String roomNumber;
	
	@Column(name = "dailyPrice")
	private BigDecimal dailyPrice;
	
	@Column(name = "status")
	private Status status;

	public Room(String roomNumber, BigDecimal dailyPrice) {
		this.roomNumber = roomNumber;
		this.dailyPrice = dailyPrice;
	}
	
	
	
	

}
