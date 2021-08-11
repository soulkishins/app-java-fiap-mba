package br.com.fiap.nubank.credit.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.fiap.nubank.credit.dto.AccessRequestDto;
import br.com.fiap.nubank.credit.dto.ContractDto;
import br.com.fiap.nubank.credit.dto.OptInDto;
import br.com.fiap.nubank.credit.dto.OptInFilterDto;
import br.com.fiap.nubank.credit.dto.PaymentCalendarDto;
import br.com.fiap.nubank.credit.dto.PreContractDto;
import br.com.fiap.nubank.credit.dto.ReceivableDto;
import br.com.fiap.nubank.credit.model.AccessRequest;
import br.com.fiap.nubank.credit.model.Contract;
import br.com.fiap.nubank.credit.model.Participant;
import br.com.fiap.nubank.credit.model.Receivable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditFacade {
	
	private final ParticipantService participantService;
	private final AccessRequestService accessRequestService;
	private final ContractService contractService;
	private final IRegistrationService registrationService;

	public long checkOptIn(OptInFilterDto optin) {
		return accessRequestService.countByBannerAndAcquire(optin.getParticipant(), optin.getBanners(), optin.getAcquirers());
	}

	public List<AccessRequestDto> findOptInByParticipant(Long participant) {
		return accessRequestService
				.findByParticipant(participant)
				.stream()
				.map(this::mapToAccessRequestDto)
				.collect(Collectors.toList());
	}
	
	private AccessRequestDto mapToAccessRequestDto(AccessRequest entity) {
		return AccessRequestDto
				.builder()
				.id(entity.getId())
				.banner(entity.getBanner())
				.acquirer(entity.getAcquirer())
				.build();
	}

	public long doOptIn(OptInDto optin) {
		var participant = participantService
				.findById(optin.getParticipant())
				.orElse(null);
		
		if (Objects.isNull(participant))
			return -1;
		
		var optins = optin
			.getBanners()
			.parallelStream()
			.<AccessRequest>flatMap(banner -> requestAccess(participant, banner, optin.getAcquirers()))
			.collect(Collectors.toList())
		;

		accessRequestService.saveAll(optins);

		return optins.stream().filter(AccessRequest::getActive).count();
	}
	
	private Stream<AccessRequest> requestAccess(Participant participant, String banner, List<String> acquirers) {
		return acquirers
		.parallelStream()
		.map(acquirer -> requestAccess(participant, banner, acquirer));
	}
	
	private AccessRequest requestAccess(Participant participant, String banner, String acquirer) {
		var accessRequest = AccessRequest
				.builder()
				.participant(participant)
				.banner(banner)
				.acquirer(acquirer)
				.beginDate(new Date())
				.active(false)
				.endDate(Date.from(LocalDateTime.now().plusYears(1).toInstant(ZoneOffset.UTC)))
				.createdDate(new Date())
				.build();
		try {
			String protocol = registrationService.createOptin(accessRequest);
			accessRequest.setActive(true);
			accessRequest.setProtocolOptIn(protocol);
		} catch (Exception e) {
			log.error("Error in request access for banner {} of acquirer {} for participant {}", banner, acquirer, participant.getIdentificationId());
		}
		return accessRequest;
	}

	public long doOptOut(List<Long> accessRequests) {
		var optOuts = accessRequestService.findByAccessRequestsId(accessRequests);
		
		optOuts.parallelStream().forEach(this::cancelAccess);

		accessRequestService.saveAll(optOuts);

		return optOuts.stream().filter(AccessRequest::getActive).count();
	}
	
	private void cancelAccess(AccessRequest accessRequest) {
		if (accessRequest.getActive()) {
			String protocol = registrationService.createOptOut(accessRequest);
			accessRequest.setActive(false);
			accessRequest.setProtocolOptIn(protocol);
			accessRequest.setLastModifiedDate(new Date());
		}
	}
	

	public PaymentCalendarDto findReceivables(List<Long> accessRequests) {
		return registrationService.findReceivables(accessRequestService.findByAccessRequestsId(accessRequests));
	}

	public List<ContractDto> findContractsByParticipant(Long participant) {
		return contractService
				.findByParticipant(participant)
				.stream()
				.map(this::contractToContractDto)
				.collect(Collectors.toList())
				;
	}
	
	private ContractDto contractToContractDto(Contract contract) {
		return ContractDto
				.builder()
				.type(contract.getType())
				.protocol(contract.getProtocol())
				.banners(contract.getBanners())
				.acquirers(contract.getAcquirers())
				.beginDate(contract.getBeginDate())
				.endDate(contract.getEndDate())
				.contractValue(contract.getContractValue())
				.discountValue(contract.getDiscountValue())
				.netValue(contract.getNetValue())
				.tax(contract.getTax())
				.receivables(contract.getReceivables().stream().map(this::receivableToReceivableDto).collect(Collectors.toList()))
				.build();
	}
	
	private ReceivableDto receivableToReceivableDto(Receivable receivable) {
		return ReceivableDto
			.builder()
			.protocol(receivable.getProtocol())
			.state(receivable.getState())
			.receivableDate(receivable.getReceivableDate())
			.effectiveDate(receivable.getEffectiveDate())
			.receivableValue(receivable.getReceivableValue())
			.discountValue(receivable.getDiscountValue())
			.netValue(receivable.getNetValue())
			.build();
	}

	public PreContractDto simulateContract(PreContractDto contract) {
		return registrationService.simulateContract(contract);
	}

	public ContractDto prepaymentReceivable(PreContractDto contract) {

		var realContract = registrationService.effectuateContract(contract);

		var record = contractDtoToContract(realContract);

		return contractToContractDto(contractService.save(record));

	}

	private Contract contractDtoToContract(ContractDto contract) {
		return Contract
				.builder()
				.type(contract.getType())
				.protocol(contract.getProtocol())
				.banners(contract.getBanners())
				.acquirers(contract.getAcquirers())
				.beginDate(contract.getBeginDate())
				.endDate(contract.getEndDate())
				.contractValue(contract.getContractValue())
				.discountValue(contract.getDiscountValue())
				.netValue(contract.getNetValue())
				.tax(contract.getTax())
				.receivables(contract.getReceivables().stream().map(this::receivableDtoToReceivable).collect(Collectors.toSet()))
				.createdDate(new Date())
				.build();
	}
	
	private Receivable receivableDtoToReceivable(ReceivableDto receivable) {
		return Receivable
			.builder()
			.protocol(receivable.getProtocol())
			.state(receivable.getState())
			.receivableDate(receivable.getReceivableDate())
			.effectiveDate(receivable.getEffectiveDate())
			.receivableValue(receivable.getReceivableValue())
			.discountValue(receivable.getDiscountValue())
			.netValue(receivable.getNetValue())
			.createdDate(new Date())
			.build();
	}
}
