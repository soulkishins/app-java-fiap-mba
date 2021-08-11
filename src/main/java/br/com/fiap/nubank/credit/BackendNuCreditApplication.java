package br.com.fiap.nubank.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendNuCreditApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendNuCreditApplication.class, args);
	}
	
}
