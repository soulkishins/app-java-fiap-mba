package br.com.fiap.nubank.credit.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import br.com.fiap.nubank.credit.dto.ContractDto;
import br.com.fiap.nubank.credit.dto.ParticipantReceivableDto;
import br.com.fiap.nubank.credit.dto.PaymentCalendarDto;
import br.com.fiap.nubank.credit.dto.PaymentDetailDto;
import br.com.fiap.nubank.credit.dto.PreContractDto;
import br.com.fiap.nubank.credit.model.AccessRequest;
import br.com.fiap.nubank.credit.service.IRegistrationService;

/**
 * Classe Mock para simular um retorno que realizado para Cerc, CIP ou Tag IMF
 * Num ambiente real, o client precisa contratar o servico da Registradora e pagar
 * por cada request realizado a API e tratar os dados de acordo com sua regra de
 * negocio.
 *
 */
@Service
@Profile("demo")
public class MockRegistrationService implements IRegistrationService {
	
	private Map<String, List<ParticipantReceivableDto>> mockReceivables;

	@Override
	public String createOptin(AccessRequest accessRequest) {
		return UUID.randomUUID().toString();
	}

	@Override
	public String createOptOut(AccessRequest accessRequest) {
		return UUID.randomUUID().toString();
	}

	@Override
	public PaymentCalendarDto findReceivables(List<AccessRequest> accessRequests) {
		return estimateContract(
				accessRequests
				.parallelStream()
				.flatMap(this::findReceivables)
				.collect(
					PaymentCalendarDto::new,
					this::organizeCalendarByDate,
					this::combineCalendar
				));
	}

	@Override
	public PreContractDto simulateContract(PreContractDto contract) {
		return null;
	}

	@Override
	public ContractDto effectuateContract(PreContractDto contract) {
		return null;
	}
	
	/* Aux Functions */
	@PostConstruct
	public void createMockData() {
		
		mockReceivables = new HashMap<>();
		
		String[] participants = {"16002128000124", "39315801000181"};
		String[] banners = {"MCC", "VCD", "MCD"};
		String[] acquires = {"35722930000133", "04242332000133", "83859428000111"};
		
		for (var participant : participants) {
			for (var banner : banners) {
				for (var acquire : acquires) {
					String key = participant + "|" + banner + "|" + acquire;
					mockReceivables.put(key, generateParticipantReceivableData());
				}
			}
		}

	}
	
	private List<ParticipantReceivableDto> generateParticipantReceivableData() {
		
		var receivables = new ArrayList<ParticipantReceivableDto>();
		var date = LocalDate.now();

		for (var i = 0; i < 90; i++) {
			var value = nextDouble(300.0, 5000.0);
			var free = nextDouble(0, value);
			var block = ((long)(value * 100) - (long)(free * 100)) / 100.0;
			date = date.plusDays(1);
			receivables.add(
				ParticipantReceivableDto
				.builder()
				.paymentDate(date)
				.receivableValue(BigDecimal.valueOf(value))
				.blockValue(BigDecimal.valueOf(block))
				.freeValue(BigDecimal.valueOf(free))
				.build()
			);
		}
		
		return receivables;
	}

	private double nextDouble(double origin, double bound) {
		return ((long) ((origin + Math.random() * (bound - origin)) * 100)) / 100.0;
	}
	
	private PaymentCalendarDto estimateContract(PaymentCalendarDto calendar) {
		var payments = calendar
				.getCalendar()
				.entrySet()
				.parallelStream()
				.<PaymentDetailDto>collect(
					() -> PaymentDetailDto
					.builder()
					.receivableValue(BigDecimal.ZERO)
					.blockValue(BigDecimal.ZERO)
					.freeValue(BigDecimal.ZERO)
					.build(),
					(detail, entry) -> this.combinePaymentDetailDto(detail, entry.getValue()),
					this::combinePaymentDetailDto
				);
		var tax = new BigDecimal(1.5); // Campo tax deve ser servidor por outro micro-servico baseado em risco e outros fatores, para proposito de demo valor fixo.
		var value = payments.getFreeValue();
		var discount = value.multiply(tax).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		var netValue = value.subtract(discount);

		calendar.setTax(tax);
		calendar.setValue(value);
		calendar.setDiscount(discount);
		calendar.setNetValue(netValue);

		return calendar;
	}
	
	private Stream<ParticipantReceivableDto> findReceivables(AccessRequest accessRequest) {
		return mockReceivables
		.getOrDefault(
			accessRequest.getParticipant().getIdentificationId() + "|" + accessRequest.getBanner() + "|" + accessRequest.getAcquirer(),
			Collections.emptyList()
		)
		.stream();
	}
	
	private PaymentCalendarDto organizeCalendarByDate(PaymentCalendarDto calendar, ParticipantReceivableDto receivable) {
		String mounthYear = receivable.getPaymentDate().format(DateTimeFormatter.ofPattern("MM/yyy"));
		var receivableGroup = calendar.getCalendar().get(mounthYear);
		if (Objects.isNull(receivableGroup)) {
			calendar.getCalendar().put(
				mounthYear,
				PaymentDetailDto
				.builder()
				.blockValue(receivable.getBlockValue())
				.freeValue(receivable.getFreeValue())
				.receivableValue(receivable.getReceivableValue())
				.build()
			);
		} else {
			receivableGroup.setBlockValue(receivableGroup.getBlockValue().add(receivable.getBlockValue()));
			receivableGroup.setFreeValue(receivableGroup.getFreeValue().add(receivable.getFreeValue()));
			receivableGroup.setReceivableValue(receivableGroup.getReceivableValue().add(receivable.getReceivableValue()));
		}
		return calendar;
	}
	
	private PaymentCalendarDto combineCalendar(PaymentCalendarDto calendar, PaymentCalendarDto calendarToJoin) {
		calendarToJoin
		.getCalendar()
		.entrySet()
		.forEach(entry -> {
			var detail = calendar.getCalendar().get(entry.getKey());
			var detailToJoin = entry.getValue();
			if (Objects.isNull(detail)) {
				calendar.getCalendar().put(entry.getKey(), detailToJoin);
			} else {
				combinePaymentDetailDto(detail, detailToJoin);
			}
		});
		return calendar;
	}
	
	private PaymentDetailDto combinePaymentDetailDto(PaymentDetailDto detail, PaymentDetailDto detailToJoin) {
		detail.setBlockValue(detail.getBlockValue().add(detailToJoin.getBlockValue()));
		detail.setFreeValue(detail.getFreeValue().add(detailToJoin.getFreeValue()));
		detail.setReceivableValue(detail.getReceivableValue().add(detailToJoin.getReceivableValue()));
		return detail;
	}

}
