package br.com.fiap.nubank.credit.repository;

import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.nubank.credit.model.Contract;

@SuppressWarnings("serial")
public abstract class ContractSpecifications extends CommonsSpecifications {

	public final static Specification<Contract> fetchReceivables() {
		return new Specification<Contract>() {
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!Number.class.isAssignableFrom(query.getResultType()))
            		root.fetch("receivables", JoinType.INNER);
            	return null;
			}
		};
	}
	
	public final static Specification<Contract> whereParticipantEqual(Long participant) {
		return new Specification<Contract>() {
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Objects.isNull(participant))
					return null;
				return builder.equal(root.get("participant").get("id"), participant);
			}
		};
	}
}
