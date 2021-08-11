package br.com.fiap.nubank.credit.service;

import static br.com.fiap.nubank.credit.repository.AccessRequestSpecifications.fetchParticipant;
import static br.com.fiap.nubank.credit.repository.AccessRequestSpecifications.whereAcquirerIn;
import static br.com.fiap.nubank.credit.repository.AccessRequestSpecifications.whereBannerIn;
import static br.com.fiap.nubank.credit.repository.AccessRequestSpecifications.whereIdIn;
import static br.com.fiap.nubank.credit.repository.AccessRequestSpecifications.whereIsActive;
import static br.com.fiap.nubank.credit.repository.AccessRequestSpecifications.whereParticipantEqual;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.fiap.nubank.credit.model.AccessRequest;
import br.com.fiap.nubank.credit.repository.AccessRequestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessRequestService {
	
	private final AccessRequestRepository accessRequestRepository;
	
	public Optional<AccessRequest> findById(Long id) {
		return accessRequestRepository.findById(id);
	}
	
	public AccessRequest save(AccessRequest accessRequest) {
		return accessRequestRepository.save(accessRequest);
	}
	
	public Iterable<AccessRequest> saveAll(Iterable<AccessRequest> accessRequests) {
		return accessRequestRepository.saveAll(accessRequests);
	}
	
	public void deleteById(Long id) {
		accessRequestRepository.deleteById(id);
	}

	public long countByBannerAndAcquire(Long id, List<String> banners, List<String> acquires) {
		return accessRequestRepository.count(
			Specification
			.where(whereParticipantEqual(id))
			.and(whereBannerIn(banners))
			.and(whereAcquirerIn(acquires))
			.and(whereIsActive())
		);
	}

	public List<AccessRequest> findByParticipantAndBannerAndAcquirer(Long id, List<String> banners, List<String> acquires) {
		return accessRequestRepository.findAll(
			Specification
			.where(whereParticipantEqual(id))
			.and(whereBannerIn(banners))
			.and(whereAcquirerIn(acquires))
			.and(whereIsActive())
		);
	}

	public List<AccessRequest> findByAccessRequestsId(List<Long> ids) {
		return accessRequestRepository.findAll(
			fetchParticipant()
			.and(whereIdIn(ids))
		);
	}

	public List<AccessRequest> findByParticipant(Long id) {
		return accessRequestRepository.findAll(whereParticipantEqual(id));
	}

}
