package br.com.fiap.nubank.credit.model;

import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "access_request")
@SequenceGenerator(name = "sq_access_request", sequenceName = "sq_access_request", allocationSize = 1)
public class AccessRequest {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_access_request")
	@EqualsAndHashCode.Include
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "participant", nullable = false, updatable = false, referencedColumnName = "id")
	private Participant participant;

	@Column(name = "banner", nullable = false, updatable = false)
	private String banner;
	
	@Column(name = "acquirer", nullable = false, updatable = false)
	private String acquirer;
	
	@Column(name = "protocol_in", nullable = false, updatable = false)
	private String protocolOptIn;
	
	@Column(name = "protocol_out")
	private String protocolOptOut;

	@Column(name = "begin_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private Boolean active;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "last_modified_date")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

}
