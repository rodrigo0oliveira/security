package br.com.api.projeto.model.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewReserveDto {
	
	private String checkin;
	
	private String checkout;

	private String roomnumber;

}
