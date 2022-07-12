package io.github.neudopb.service.impl;

import io.github.neudopb.api.dto.ItemPedidoDTO;
import io.github.neudopb.api.dto.PedidoDTO;
import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.entity.ItemPedido;
import io.github.neudopb.domain.entity.Pedido;
import io.github.neudopb.domain.entity.Produto;
import io.github.neudopb.domain.enums.StatusPedido;
import io.github.neudopb.domain.repository.ClienteRepository;
import io.github.neudopb.domain.repository.ItemPedidoRepository;
import io.github.neudopb.domain.repository.PedidoRepository;
import io.github.neudopb.domain.repository.ProdutoRepository;
import io.github.neudopb.exception.RegraNegocioException;
import io.github.neudopb.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoServiceImpl(
            PedidoRepository repository, ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido!"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedido = converterItens(pedido, dto.getItens());
        repository.save(pedido);
        itemPedidoRepository.saveAll(itemPedido);
        pedido.setItens(itemPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedido(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    public List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if(itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens!");
        }

        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtoRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: " + idProduto
                                    )
                            );

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
