package br.com.fiap.nubank.credit.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.nubank.credit.model.Participant;

@Repository
@Transactional(readOnly = true)
public interface ParticipantRepository extends CrudRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

}
