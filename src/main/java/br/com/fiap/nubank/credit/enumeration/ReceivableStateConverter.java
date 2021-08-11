package br.com.fiap.nubank.credit.enumeration;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ReceivableStateConverter implements AttributeConverter<ReceivableState, Integer> {
	
	@Override
	public Integer convertToDatabaseColumn(ReceivableState attribute) {
		if (Objects.isNull(attribute))
			return null;
		return attribute.getValue();
	}

	@Override
	public ReceivableState convertToEntityAttribute(Integer data) {
		if (Objects.isNull(data))
			return null;

		for (ReceivableState value : ReceivableState.values()) {
			if (value.getValue() == data.intValue())
				return value;
		}

		return null;
	}

}
