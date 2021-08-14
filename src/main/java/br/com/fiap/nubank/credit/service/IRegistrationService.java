package br.com.fiap.nubank.credit.service;

import java.util.List;

import br.com.fiap.nubank.credit.dto.ContractDto;
import br.com.fiap.nubank.credit.dto.PaymentCalendarDto;
import br.com.fiap.nubank.credit.dto.PreContractDto;
import br.com.fiap.nubank.credit.model.AccessRequest;

public interface IRegistrationService {

	String createOptin(AccessRequest accessRequest);

	String createOptOut(AccessRequest accessRequest);

	PaymentCalendarDto findReceivables(List<AccessRequest> accessRequests);

	PreContractDto simulateContract(PreContractDto contract);

	ContractDto effectuateContract(List<AccessRequest> accessRequests, PaymentCalendarDto paymentCalendarToPrepayment);

}
