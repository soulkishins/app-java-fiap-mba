package br.com.fiap.nubank.credit.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.nubank.credit.model.Participant;
import br.com.fiap.nubank.credit.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantService {

	private final ParticipantRepository participantRepository;
	
	public Optional<Participant> findById(Long id) {
		return participantRepository.findById(id);
	}
	
	public Participant save(Participant participant) {
		return participantRepository.save(participant);
	}
	
	public void deleteById(Long id) {
		participantRepository.deleteById(id);
	}

}
