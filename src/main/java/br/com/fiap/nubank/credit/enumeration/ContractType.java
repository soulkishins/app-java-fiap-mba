package br.com.fiap.nubank.credit.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractType {
	ENGRAVE(1),
	DISPOSAL(2);
	
	private final int value;

}
