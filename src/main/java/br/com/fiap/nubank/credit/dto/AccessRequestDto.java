package br.com.fiap.nubank.credit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDto {

	private Long id;
	private String banner;
	private String acquirer;

}
