package br.com.fiap.nubank.credit.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.fiap.nubank.credit.enumeration.ContractType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreContractDto {

	private ContractType type;

	private String protocol;

	private String banners;
	
	private String acquirers;

	private Date beginDate;

	private Date endDate;

	private BigDecimal contractValue;

	private BigDecimal discountValue;

	private BigDecimal netValue;

	private BigDecimal tax;

	private List<ReceivableDto> receivables;

}
