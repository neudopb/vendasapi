package io.github.neudopb.service;

import io.github.neudopb.api.dto.PedidoDTO;
import io.github.neudopb.domain.entity.Pedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedido(Integer id);
}
