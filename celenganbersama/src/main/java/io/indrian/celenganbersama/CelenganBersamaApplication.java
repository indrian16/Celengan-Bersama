package io.indrian.celenganbersama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CelenganBersamaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CelenganBersamaApplication.class, args);
	}

}
