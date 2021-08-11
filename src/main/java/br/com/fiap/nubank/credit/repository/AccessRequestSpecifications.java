package br.com.fiap.nubank.credit.repository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.nubank.credit.model.AccessRequest;

@SuppressWarnings("serial")
public abstract class AccessRequestSpecifications extends CommonsSpecifications {
	
	public final static Specification<AccessRequest> fetchParticipant() {
		return new Specification<AccessRequest>() {
			public Predicate toPredicate(Root<AccessRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!Number.class.isAssignableFrom(query.getResultType()))
            		root.fetch("participant", JoinType.INNER);
            	return null;
			}
		};
	}
	
	public final static Specification<AccessRequest> whereIdIn(List<Long> ids) {
		return new Specification<AccessRequest>() {
			public Predicate toPredicate(Root<AccessRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Objects.isNull(ids) || ids.isEmpty())
					return null;
				return root.get("id").in(ids);
			}
		};
	}

	public final static Specification<AccessRequest> whereParticipantEqual(Long participant) {
		return new Specification<AccessRequest>() {
			public Predicate toPredicate(Root<AccessRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Objects.isNull(participant))
					return null;
				return builder.equal(root.get("participant").get("id"), participant);
			}
		};
	}

	public final static Specification<AccessRequest> whereBannerIn(List<String> banners) {
		return new Specification<AccessRequest>() {
			public Predicate toPredicate(Root<AccessRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Objects.isNull(banners) || banners.isEmpty())
					return null;
				return root.get("banner").in(banners);
			}
		};
	}

	public final static Specification<AccessRequest> whereAcquirerIn(List<String> acquirers) {
		return new Specification<AccessRequest>() {
			public Predicate toPredicate(Root<AccessRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Objects.isNull(acquirers) || acquirers.isEmpty())
					return null;
				return root.get("acquirer").in(acquirers);
			}
		};
	}

	public final static Specification<AccessRequest> whereIsActive() {
		return new Specification<AccessRequest>() {
			public Predicate toPredicate(Root<AccessRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.and(
					builder.equal(root.get("active"), true),
					builder.greaterThanOrEqualTo(root.get("beginDate"), Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)))
				);
			}
		};
	}
	
}
