package br.com.fiap.nubank.credit.enumeration;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ContractTypeConverter implements AttributeConverter<ContractType, Integer> {
	
	@Override
	public Integer convertToDatabaseColumn(ContractType attribute) {
		if (Objects.isNull(attribute))
			return null;
		return attribute.getValue();
	}

	@Override
	public ContractType convertToEntityAttribute(Integer data) {
		if (Objects.isNull(data))
			return null;

		for (ContractType value : ContractType.values()) {
			if (value.getValue() == data.intValue())
				return value;
		}

		return null;
	}

}
