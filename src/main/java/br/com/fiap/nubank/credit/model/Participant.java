package br.com.fiap.nubank.credit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import br.com.fiap.nubank.credit.enumeration.DocumentType;
import br.com.fiap.nubank.credit.enumeration.DocumentTypeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "participant")
@SequenceGenerator(name = "sq_participant", sequenceName = "sq_participant", allocationSize = 1)
public class Participant {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_participant")
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "identification", nullable = false, updatable = false)
	private String identificationId;

	@Column(name = "identification_type", nullable = false, updatable = false)
	@Convert(converter = DocumentTypeConverter.class)
	private DocumentType identificationType;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "last_modified_date")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

}
