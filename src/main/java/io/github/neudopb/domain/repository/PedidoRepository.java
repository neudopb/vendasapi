package io.github.neudopb.domain.repository;

import io.github.neudopb.domain.entity.Cliente;
import io.github.neudopb.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByCliente(Cliente cliente);
}
