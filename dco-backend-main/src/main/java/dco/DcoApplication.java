package dco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // auditing
public class DcoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DcoApplication.class, args);
	}

}
