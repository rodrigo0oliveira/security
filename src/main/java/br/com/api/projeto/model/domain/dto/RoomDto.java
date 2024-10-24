package br.com.api.projeto.model.domain.dto;

import java.math.BigDecimal;
import br.com.api.projeto.model.domain.enums.RoomType;
import jakarta.validation.constraints.NotNull;
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
	
	@NotNull
	private String roomnumber;

	@NotNull
	private BigDecimal dailyPrice;

	@NotNull
	private RoomType roomType;
	

}
