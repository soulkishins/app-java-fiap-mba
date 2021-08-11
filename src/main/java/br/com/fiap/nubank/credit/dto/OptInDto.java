package br.com.fiap.nubank.credit.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptInDto {
	
	private Long participant;
	private List<String> banners;
	private List<String> acquirers;

}
