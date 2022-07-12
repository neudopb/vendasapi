package io.github.neudopb.service;

import io.github.neudopb.api.dto.PedidoDTO;
import io.github.neudopb.domain.entity.Pedido;
import io.github.neudopb.domain.enums.StatusPedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedido(Integer id);
    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
