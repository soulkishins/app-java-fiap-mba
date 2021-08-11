package br.com.fiap.nubank.credit.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.fiap.nubank.credit.enumeration.ReceivableState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivableDto {

	private String protocol;

	private ReceivableState state;
	
	private Date receivableDate;

	private Date effectiveDate;

	private BigDecimal receivableValue;

	private BigDecimal discountValue;

	private BigDecimal netValue;
	
}
