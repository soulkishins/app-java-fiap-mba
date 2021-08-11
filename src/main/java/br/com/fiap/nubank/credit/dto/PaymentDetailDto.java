package br.com.fiap.nubank.credit.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailDto {

	private BigDecimal receivableValue;
	private BigDecimal blockValue;
	private BigDecimal freeValue;

}
