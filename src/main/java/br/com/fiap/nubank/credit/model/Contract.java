package br.com.fiap.nubank.credit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

import br.com.fiap.nubank.credit.enumeration.ContractType;
import br.com.fiap.nubank.credit.enumeration.ContractTypeConverter;
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
@Table(name = "contract")
@SequenceGenerator(name = "sq_contract", sequenceName = "sq_contract", allocationSize = 1)
public class Contract {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_contract")
	@EqualsAndHashCode.Include
	private Long id;

	@ManyToOne
	@JoinColumn(name = "participant", nullable = false, updatable = false, referencedColumnName = "id")
	private Participant participant;

	@Column(name = "banners", nullable = false, updatable = false)
	private String banners;

	@Column(name = "acquirers", nullable = false, updatable = false)
	private String acquirers;

	@Column(name = "type", nullable = false, updatable = false)
	@Convert(converter = ContractTypeConverter.class)
	private ContractType type;
	
	@Column(name = "protocol", nullable = false, updatable = false)
	private String protocol;

	@Column(name = "begin_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDate;

	@Column(name = "end_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "value", nullable = false, updatable = false)
	private BigDecimal contractValue;

	@Column(name = "discount", nullable = false, updatable = false)
	private BigDecimal discountValue;

	@Column(name = "net_value", nullable = false, updatable = false)
	private BigDecimal netValue;

	@Column(name = "tax", nullable = false, updatable = false)
	private BigDecimal tax;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@OneToMany(mappedBy = "contract", cascade = CascadeType.PERSIST)
	private Set<Receivable> receivables;

}
