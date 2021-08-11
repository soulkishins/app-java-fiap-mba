package br.com.fiap.nubank.credit.service;

import static br.com.fiap.nubank.credit.repository.CommonsSpecifications.distinct;
import static br.com.fiap.nubank.credit.repository.ContractSpecifications.fetchReceivables;
import static br.com.fiap.nubank.credit.repository.ContractSpecifications.whereParticipantEqual;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.nubank.credit.model.Contract;
import br.com.fiap.nubank.credit.repository.ContractRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {
	
	private final ContractRepository contractRepository;

	public Optional<Contract> findById(Long id) {
		return contractRepository.findById(id);
	}

	public Contract save(Contract contract) {
		return contractRepository.save(contract);
	}

	public void deleteById(Long id) {
		contractRepository.deleteById(id);
	}

	public List<Contract> findByParticipant(Long participant) {
		return contractRepository.findAll(
			fetchReceivables()
			.and(whereParticipantEqual(participant))
			.and(distinct())
		);
	}

}
