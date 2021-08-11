package br.com.fiap.nubank.credit.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {
	CPF(1),
	CNPJ(2);
	
	private final int value;

}
