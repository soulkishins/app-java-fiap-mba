package br.com.fiap.nubank.credit.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.fiap.nubank.credit.enumeration.ContractType;
import br.com.fiap.nubank.credit.enumeration.DocumentType;
import br.com.fiap.nubank.credit.enumeration.ReceivableState;
import br.com.fiap.nubank.credit.model.AccessRequest;
import br.com.fiap.nubank.credit.model.Contract;
import br.com.fiap.nubank.credit.model.Participant;
import br.com.fiap.nubank.credit.model.Receivable;

@Component()
@Profile("demo")
public class AutoCreateData {
	
	@PersistenceContext
	private EntityManager manager;
	
	@EventListener(ApplicationReadyEvent.class)
    @Transactional
	public void initializeMockDB() {
		
		persistParticipantDemo1();
		persistParticipantDemo2();
		
	}
	
	private void persistParticipantDemo1() {
		
		var part = Participant
				.builder()
				.name("Demonstração PJ Novo")
				.identificationId("16002128000124")
				.identificationType(DocumentType.CNPJ)
				.createdDate(new Date())
				.build();
		
		manager.persist(part);
	}
	
	private void persistParticipantDemo2() {
		
		var part = Participant
				.builder()
				.name("Demonstração PJ Completo")
				.identificationId("39315801000181")
				.identificationType(DocumentType.CNPJ)
				.createdDate(new Date())
				.build();
		
		manager.persist(part);
		
		persistAccessReceivablesFor(part);
	}
	
	private void persistAccessReceivablesFor(Participant part) {

		var accessReceivable = AccessRequest
				.builder()
				.protocolOptIn(UUID.randomUUID().toString())
				.participant(part)
				.banner("VCD")
				.acquirer("35722930000133")
				.beginDate(new Date())
				.endDate(Date.from(LocalDateTime.now().plusDays(60).toInstant(ZoneOffset.UTC)))
				.active(true)
				.createdDate(new Date())
				.build();
		
		manager.persist(accessReceivable);
		
		accessReceivable = AccessRequest
				.builder()
				.protocolOptIn(UUID.randomUUID().toString())
				.participant(part)
				.banner("MCC")
				.acquirer("04242332000133")
				.beginDate(new Date())
				.endDate(Date.from(LocalDateTime.now().plusDays(60).toInstant(ZoneOffset.UTC)))
				.active(true)
				.createdDate(new Date())
				.build();

		manager.persist(accessReceivable);
		
		persistContractFor(part, accessReceivable);

	}
	
	private void persistContractFor(Participant part, AccessRequest accessRequest) {
		
		var value = new BigDecimal(25000.00);
		var tax = new BigDecimal(1.5);
		var discount = value.multiply(tax).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		var netValue = value.subtract(discount);
		
		var contract = Contract
				.builder()
				.protocol(UUID.randomUUID().toString())
				.participant(part)
				.banners(accessRequest.getBanner())
				.acquirers(accessRequest.getAcquirer())
				.type(ContractType.DISPOSAL)
				.beginDate(new Date())
				.endDate(Date.from(LocalDateTime.now().plusDays(60).toInstant(ZoneOffset.UTC)))
				.contractValue(value)
				.discountValue(discount)
				.netValue(netValue)
				.tax(tax)
				.createdDate(new Date())
				.build();

		manager.persist(contract);

		persistReceivable(contract);
		
	}
	
	private void persistReceivable(Contract contract) {
		for (double limit = contract.getContractValue().doubleValue(); limit > 0.0;) {
			var rand = nextDouble(300.0, 1500.0);
			if (limit < rand)
				rand = limit;
			limit -= rand;

			var value = new BigDecimal(rand);
			var tax = new BigDecimal(1.5);
			var discount = value.multiply(tax).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
			var netValue = value.subtract(discount);

			var receivable = Receivable
					.builder()
					.protocol(UUID.randomUUID().toString())
					.contract(contract)
					.state(ReceivableState.PENDING)
					.receivableDate(getReceivableDate(contract.getContractValue().doubleValue(), limit, contract.getBeginDate(), contract.getEndDate()))
					.receivableValue(value)
					.discountValue(discount)
					.netValue(netValue)
					.createdDate(new Date())
					.build();
			
			manager.persist(receivable);
		}
		
	}

	private Date getReceivableDate(double value, double limitValue, Date beginDate, Date endDate) {
		LocalDate date = LocalDate.now();
		var percent = (value - limitValue) / value;
		long nDay = (long) (Duration.between(
			LocalDate.ofInstant(beginDate.toInstant(), ZoneId.of("Z")).atStartOfDay(),
			LocalDate.ofInstant(endDate.toInstant(), ZoneId.of("Z")).atStartOfDay()
		).toDays() * percent);
		
		if (nDay == 0)
			nDay = 1;

		return Date.from(date.plusDays(nDay).atStartOfDay().toInstant(ZoneOffset.UTC));
	}

	private double nextDouble(double origin, double bound) {
		return ((long) ((origin + Math.random() * (bound - origin)) * 100)) / 100.0;
	}
}
