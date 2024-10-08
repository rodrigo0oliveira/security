package br.com.api.projeto.model.domain;

import java.math.BigDecimal;

import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
	
	@Column(name = "roomnumber",unique = true)
	@NotNull
	private String roomnumber;
	
	@Column(name = "dailyPrice")
	@NotNull
	private BigDecimal dailyPrice;
	
	
	@Column(name = "status")
	@NotNull
	private Status status;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private RoomType roomtype;

	public Room(String roomNumber, BigDecimal dailyPrice,RoomType roomtype) {
		this.roomnumber = roomNumber;
		this.dailyPrice = dailyPrice;
		this.roomtype = roomtype;
	}

	public Room(RoomType roomtype,BigDecimal dailyPrice, Status status) {
		this.dailyPrice = dailyPrice; 
		this.status = status;
		this.roomtype = roomtype;
	}
	
	
	
	
	
	
	

}
