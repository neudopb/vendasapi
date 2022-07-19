package io.github.neudopb.api.controller;

import io.github.neudopb.api.dto.AtualizacaoStatusPedidoDTO;
import io.github.neudopb.api.dto.InfoItemPedidoDTO;
import io.github.neudopb.api.dto.InfoPedidoDTO;
import io.github.neudopb.api.dto.PedidoDTO;
import io.github.neudopb.domain.entity.ItemPedido;
import io.github.neudopb.domain.entity.Pedido;
import io.github.neudopb.domain.enums.StatusPedido;
import io.github.neudopb.service.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/pedidos")
@Api("API Pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salvar novo Pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validação")
    })
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um Pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido encontrado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado")
    })
    public InfoPedidoDTO getById(@PathVariable Integer id) {
        return service
                .obterPedido(id)
                .map(pedido -> converter(pedido))
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualizar Status do Pedido")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Status do pedido atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Pedido não encontrado")
    })
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody @Valid AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InfoPedidoDTO converter(Pedido pedido) {
        return InfoPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .nomeCliente(pedido.getCliente().getNome())
                .cpf(pedido.getCliente().getCpf())
                .total(pedido.getTotal())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InfoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map(
                item -> InfoItemPedidoDTO
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoProduto(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
