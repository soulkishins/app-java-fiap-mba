package br.com.fiap.nubank.credit.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import br.com.fiap.nubank.credit.enumeration.ReceivableState;
import br.com.fiap.nubank.credit.enumeration.ReceivableStateConverter;
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
@Table(name = "receivable")
@SequenceGenerator(name = "sq_receivable", sequenceName = "sq_receivable", allocationSize = 1)
public class Receivable {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_receivable")
	@EqualsAndHashCode.Include
	private Long id;

	@ManyToOne
	@JoinColumn(name = "contract", nullable = false, updatable = false, referencedColumnName = "id")
	private Contract contract;
	
	@Column(name = "protocol", nullable = false, updatable = false)
	private String protocol;

	@Column(name = "state", nullable = false)
	@Convert(converter = ReceivableStateConverter.class)
	private ReceivableState state;
	
	@Column(name = "receivable_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivableDate;

	@Column(name = "effective_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;

	@Column(name = "value", nullable = false, updatable = false)
	private BigDecimal receivableValue;

	@Column(name = "discount", nullable = false, updatable = false)
	private BigDecimal discountValue;

	@Column(name = "net_value", nullable = false, updatable = false)
	private BigDecimal netValue;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "last_modified_date")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

}
