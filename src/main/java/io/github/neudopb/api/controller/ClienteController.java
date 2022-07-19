package io.github.neudopb.api.controller;

import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.repository.ClienteRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/clientes")
@Api("API Clientes")
public class ClienteController {

    private ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salvar novo Cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validação")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return repository.save(cliente);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Editar Cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente) {
        repository
                .findById(id)
                .map(clientePut -> {
                    cliente.setId(clientePut.getId());
                    repository.save(cliente);
                    return clientePut;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cliente não encontrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deletar Cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public void delete(@PathVariable Integer id) {
        repository.findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cliente não encontrado."));
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um Cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public Cliente getClienteById(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Cliente não encontrado."));
    }

    @GetMapping
    @ApiOperation("Buscar por Clientes")
    @ApiResponse(code = 200, message = "Clientes encontrados")
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                                    .matching()
                                    .withIgnoreCase()
                                    .withStringMatcher(
                                            ExampleMatcher.StringMatcher.CONTAINING
                                    );
        Example example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }
}
