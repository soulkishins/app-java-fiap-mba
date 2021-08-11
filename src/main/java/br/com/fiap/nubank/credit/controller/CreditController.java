package br.com.fiap.nubank.credit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.nubank.credit.dto.AccessRequestDto;
import br.com.fiap.nubank.credit.dto.ContractDto;
import br.com.fiap.nubank.credit.dto.OptInDto;
import br.com.fiap.nubank.credit.dto.OptInFilterDto;
import br.com.fiap.nubank.credit.dto.PaymentCalendarDto;
import br.com.fiap.nubank.credit.dto.PreContractDto;
import br.com.fiap.nubank.credit.service.CreditFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/credit")
public class CreditController {
	
	private final CreditFacade creditFacade;
	
	@ApiOperation(value = "Confirmar a existência da permissão de acesso à agenda por meio de opt-in.", response = Void.class)
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Opt-in ativo para consulta de recebíveis." ),
        @ApiResponse( code=206, message = "Opt-in parcialmente ativo para consulta de recebíveis." ),
        @ApiResponse( code=404, message = "Opt-in não ativado para consulta de recebíveis." ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@GetMapping("/opt-in/check")
	public ResponseEntity<Void> checkOptIn(OptInFilterDto optin) {
		var accessReceivables = creditFacade.checkOptIn(optin);
		if (accessReceivables == optin.getBanners().size() * optin.getAcquirers().size())
			return ResponseEntity.ok().build();
		if (accessReceivables > 0)
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ApiOperation(value = "Confirmar a existência da permissão de acesso à agenda por meio de opt-in.", response = AccessRequestDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Opt-in ativo para consulta de recebíveis." ),
        @ApiResponse( code=206, message = "Opt-in parcialmente ativo para consulta de recebíveis." ),
        @ApiResponse( code=404, message = "Opt-in não ativado para consulta de recebíveis." ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@GetMapping("/opt-in/by-participant/{participant}")
	public ResponseEntity<List<AccessRequestDto>> listOptIn(@PathVariable Long participant) {
		return ResponseEntity.status(HttpStatus.OK).body(creditFacade.findOptInByParticipant(participant));
	}

	@ApiOperation(value = "Informar permissão de acesso à agenda por meio de opt-in.", response = Void.class)
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Opt-in realizado com sucesso." ),
        @ApiResponse( code=206, message = "Opt-in realizado parcialmente com sucesso." ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=404, message = "Participante não localizado ou não habilitado." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@PostMapping("/opt-in")
	public ResponseEntity<Void> doOptIn(@RequestBody OptInDto optin) {
		var success = creditFacade.doOptIn(optin);
		if (success < 0)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		if (success == optin.getBanners().size() * optin.getAcquirers().size())
			return ResponseEntity.ok().build();
		if (success > 0)
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@ApiOperation(value = "Suspender permissão de acesso à agenda por meio de opt-out.", response = Void.class)
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Opt-out realizado com sucesso." ),
        @ApiResponse( code=206, message = "Opt-out realizado parcialmente com sucesso." ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@PostMapping("/opt-out")
	public ResponseEntity<Void> doOptOut(@RequestBody List<Long> accessRequests) {
		var success = creditFacade.doOptOut(accessRequests);
		if (success == accessRequests.size())
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		if (success > 0)
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Consultar informações de agenda por força de um contrato ou opt-in.", response = PaymentCalendarDto.class)
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Consulta da Agenda de Recebíveis realizada com sucesso" ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@GetMapping("/receivables")
	public ResponseEntity<PaymentCalendarDto> findReceivables(@RequestParam("optin") List<Long> accessRequests) {
		return ResponseEntity.status(HttpStatus.OK).body(creditFacade.findReceivables(accessRequests));
	}

	@ApiOperation(value = "Consultar Contrato.", response = ContractDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Consulta do Contrato realizada com sucesso." ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@GetMapping("/contract/by-participant/{participant}")
	public ResponseEntity<List<ContractDto>> findContractsByParticipant(@PathVariable Long participant) {
		return ResponseEntity.status(HttpStatus.OK).body(creditFacade.findContractsByParticipant(participant));
	}

	@ApiOperation(value = "Simular Contrato.", response = PreContractDto.class)
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Simução do Contrato realizada com sucesso." ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@PostMapping("/contract/by-participant/{participant}/simulate")
	public ResponseEntity<PreContractDto> simulateContract(@PathVariable Long participant, @RequestBody PreContractDto contract) {
		return ResponseEntity.status(HttpStatus.OK).body(creditFacade.simulateContract(contract));
	}

	@ApiOperation(value = "Gerar novo contrato.", response = ContractDto.class)
    @ApiResponses(value = {
        @ApiResponse( code=200, message = "Contrato efetivado com sucesso" ),
        @ApiResponse( code=401, message = "Não autorizado realizar operação." ),
        @ApiResponse( code=403, message = "Acesso proibido a função." ),
        @ApiResponse( code=500, message = "Erro ao processar a requisição." ),
    })
	@PostMapping("/contract/by-participant/{participant}/prepayment")
	public ResponseEntity<ContractDto> prepaymentReceivable(@PathVariable Long participant, @RequestBody PreContractDto contract) {
		return ResponseEntity.status(HttpStatus.OK).body(creditFacade.prepaymentReceivable(contract));
	}

}
