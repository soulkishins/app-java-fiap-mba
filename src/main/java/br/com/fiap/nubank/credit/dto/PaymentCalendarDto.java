package br.com.fiap.nubank.credit.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCalendarDto {

	@Builder.Default
	private Map<String, PaymentDetailDto> calendar = new HashMap<>();

	private BigDecimal tax;
	private BigDecimal value;
	private BigDecimal discount;
	private BigDecimal netValue;

}
