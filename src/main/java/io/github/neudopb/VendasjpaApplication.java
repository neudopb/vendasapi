package io.github.neudopb;

import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VendasjpaApplication {

	@Bean
	public CommandLineRunner init(@Autowired ClienteRepository clienteRepository) {
		return args -> {
			System.out.println("-----SALVANDO-----");
			clienteRepository.save(new Cliente("Neudo"));
			clienteRepository.save(new Cliente("Heitor"));

			List<Cliente> todosClientes = clienteRepository.findAll();
			todosClientes.forEach(System.out::println);

			System.out.println("-----EDITANDO-----");
			todosClientes.forEach(c -> {
				c.setNome(c.getNome() + " novo");
				clienteRepository.save(c);
			});

			todosClientes = clienteRepository.findAll();
			todosClientes.forEach(System.out::println);

			System.out.println("-----BUSCANDO-----");
			System.out.println("findByNomeLike");
			clienteRepository.findByNomeLike("%Neud%").forEach(System.out::println);
			clienteRepository.findNomeQuery("%Neud%").forEach(System.out::println);

			System.out.println("findByNomeOrIdOrderById");
			clienteRepository.findByNomeLikeOrIdOrderById("Neud%", 2).forEach(System.out::println);

			System.out.println("findOneByNome");
			System.out.println(clienteRepository.findOneByNome("Heitor novo"));

			System.out.println("existsByNome");
			System.out.println(clienteRepository.existsByNome("Heitor novo"));

			System.out.println("-----DELETANDO-----");
			clienteRepository.findAll().forEach(c -> {
				clienteRepository.delete(c);
			});

			todosClientes = clienteRepository.findAll();
			if (todosClientes.isEmpty()) {
				System.out.println("Lista vazia");
			} else {
				todosClientes.forEach(System.out::println);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(VendasjpaApplication.class, args);
	}

}
