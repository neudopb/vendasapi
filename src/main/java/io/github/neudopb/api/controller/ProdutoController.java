package io.github.neudopb.api.controller;

import io.github.neudopb.domain.entity.Produto;
import io.github.neudopb.domain.repository.ProdutoRepository;
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
@RequestMapping("api/produtos")
@Api("API Produtos")
public class ProdutoController {

    private ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salvar novo Produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validação")
    })
    public Produto save(@RequestBody @Valid Produto produto) {
        return repository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Editar Produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        repository
                .findById(id)
                .map(produtoPut -> {
                    produto.setId(produtoPut.getId());
                    repository.save(produto);
                    return produtoPut;
                }).orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Produto não encontrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deletar Produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto deletado com sucesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public void delete(@PathVariable Integer id) {
        repository
                .findById(id)
                .map(produto -> {
                    repository.delete(produto);
                    return Void.TYPE;
                }).orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Produto não encontrado."));
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um Produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public Produto getProdutoById(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Produto não encontrado."));
    }

    @GetMapping
    @ApiOperation("Buscar por Produtos")
    @ApiResponse(code = 200, message = "Produtos encontrados")
    public List<Produto> find(Produto filtro) {
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
