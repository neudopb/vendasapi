package io.github.neudopb.api.controller;

import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/clientes")
public class ClienteController {

    private ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer id) {
        Optional<Cliente> cliente = repository.findById(id);

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente) {
        Cliente newCliente = repository.save(cliente);
        return ResponseEntity.ok(newCliente);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Cliente> cliente = repository.findById(id);

        if (cliente.isPresent()) {
            repository.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity update(@PathVariable Integer id, @RequestBody Cliente cliente) {
        return repository
                .findById(id)
                .map(clientePut -> {
                    cliente.setId(clientePut.getId());
                    repository.save(cliente);
                    return ResponseEntity.noContent().build();
                }).orElseGet( () -> ResponseEntity.notFound().build());
    }
}
