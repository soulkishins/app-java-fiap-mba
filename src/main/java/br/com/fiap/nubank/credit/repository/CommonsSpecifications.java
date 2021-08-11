package br.com.fiap.nubank.credit.repository;

import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("serial")
public abstract class CommonsSpecifications {

	public final static <T> Specification<T> whereId(Long id) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Objects.isNull(id))
					return null;
				return builder.equal(root.get("id"), id);
			}
		};
	}
	
	public final static <T> Specification<T> distinct() {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!Number.class.isAssignableFrom(query.getResultType()))
					query.distinct(true);
				return null;
			}
		};
	}
	
}
