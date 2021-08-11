package br.com.fiap.nubank.credit.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceivableState {
	PENDING(1),
	DUE(2),
	PAID(3);
	
	private final int value;

}
