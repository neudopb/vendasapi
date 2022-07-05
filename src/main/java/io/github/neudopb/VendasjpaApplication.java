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
			clientes.salvar(new Cliente("Neudo"));
			clientes.salvar(new Cliente("Heitor"));

			List<Cliente> todosClientes = clientes.listarTodos();
			todosClientes.forEach(System.out::println);

			System.out.println("-----EDITANDO-----");
			todosClientes.forEach(c -> {
				c.setNome(c.getNome() + " novo");
				clientes.atualizar(c);
			});

			todosClientes = clientes.listarTodos();
			todosClientes.forEach(System.out::println);

			System.out.println("-----BUSCANDO-----");
			clientes.buscarNome("Neud").forEach(System.out::println);

			System.out.println("-----DELETANDO-----");
			clientes.listarTodos().forEach(c -> {
				clientes.deletar(c);
			});

			todosClientes = clientes.listarTodos();
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
