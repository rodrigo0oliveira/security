package br.com.api.projeto.model.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewReserveDto {

	@NotNull
	private String checkin;

	@NotNull
	private String checkout;

	@NotNull
	private String roomnumber;

}
