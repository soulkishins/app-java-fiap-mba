package br.com.fiap.nubank.credit.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.nubank.credit.model.Contract;

@Repository
@Transactional(readOnly = true)
public interface ContractRepository extends CrudRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {

}
