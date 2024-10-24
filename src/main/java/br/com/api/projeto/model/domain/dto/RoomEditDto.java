package br.com.api.projeto.model.domain.dto;

import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomEditDto {

    @NotNull
    private BigDecimal dailyPrice;

    @NotNull
    private Status status;

    @NotNull
    private RoomType roomtype;
}
