package io.github.neudopb;

import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.repository.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VendasjpaApplication {

	@Bean
	public CommandLineRunner init(@Autowired Clientes clientes) {
		return args -> {
			System.out.println("-----SALVANDO-----");
			clientes.save(new Cliente("Neudo"));
			clientes.save(new Cliente("Heitor"));

			List<Cliente> todosClientes = clientes.findAll();
			todosClientes.forEach(System.out::println);

			System.out.println("-----EDITANDO-----");
			todosClientes.forEach(c -> {
				c.setNome(c.getNome() + " novo");
				clientes.save(c);
			});

			todosClientes = clientes.findAll();
			todosClientes.forEach(System.out::println);

			System.out.println("-----BUSCANDO-----");
			System.out.println("findByNomeLike");
			clientes.findByNomeLike("%Neud%").forEach(System.out::println);

			System.out.println("findByNomeOrIdOrderById");
			clientes.findByNomeLikeOrIdOrderById("Neud%", 2).forEach(System.out::println);

			System.out.println("findOneByNome");
			System.out.println(clientes.findOneByNome("Heitor novo"));

			System.out.println("existsByNome");
			System.out.println(clientes.existsByNome("Heitor novo"));

			System.out.println("-----DELETANDO-----");
			clientes.findAll().forEach(c -> {
				clientes.delete(c);
			});

			todosClientes = clientes.findAll();
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
