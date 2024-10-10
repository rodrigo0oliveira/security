package br.com.api.projeto.model.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_reserves")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Reserve {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;
	
	@Column(name = "checkin")
	@NotNull
	private LocalDate checkin;
	
	@Column(name = "checkout")
	@NotNull
	private LocalDate checkout;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	@JsonIgnoreProperties({"id","password","document","roles","authorities",
		"accountNonExpired","accountNonLocked","credentialsNonExpired",
		"enabled"})
	private User user;
	
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	@JsonIgnoreProperties({"id","status"})
	@NotNull
	private Room room;

}
