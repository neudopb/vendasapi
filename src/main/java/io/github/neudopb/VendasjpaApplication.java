package io.github.neudopb;

import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VendasjpaApplication {

	@Bean
	public CommandLineRunner commandLineRunner(@Autowired ClienteRepository repository) {
		return args -> {
			Cliente c = new Cliente("Neudo");
			repository.save(c);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(VendasjpaApplication.class, args);
	}

}
