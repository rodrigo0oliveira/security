package br.com.api.projeto.model.domain.dto;

import java.math.BigDecimal;
import br.com.api.projeto.model.domain.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "roomnumber")
public class RoomDto {
	

	private String roomnumber;
	
	private BigDecimal dailyPrice;
	
	private RoomType roomType;
	

}
