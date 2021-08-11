package br.com.fiap.nubank.credit.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.nubank.credit.model.Receivable;
import br.com.fiap.nubank.credit.repository.ReceivableRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceivableService {

	private final ReceivableRepository receivableRepository;
	
	public Optional<Receivable> findById(Long id) {
		return receivableRepository.findById(id);
	}
	
	public Receivable save(Receivable receivable) {
		return receivableRepository.save(receivable);
	}
	
	public void deleteById(Long id) {
		receivableRepository.deleteById(id);
	}

}
