package br.com.fiap.nubank.credit.enumeration;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DocumentTypeConverter implements AttributeConverter<DocumentType, Integer> {
	
	@Override
	public Integer convertToDatabaseColumn(DocumentType attribute) {
		if (Objects.isNull(attribute))
			return null;
		return attribute.getValue();
	}

	@Override
	public DocumentType convertToEntityAttribute(Integer data) {
		if (Objects.isNull(data))
			return null;

		for (DocumentType value : DocumentType.values()) {
			if (value.getValue() == data.intValue())
				return value;
		}

		return null;
	}

}
