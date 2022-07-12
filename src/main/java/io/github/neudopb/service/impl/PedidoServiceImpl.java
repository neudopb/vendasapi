package io.github.neudopb.service.impl;

import io.github.neudopb.domain.repository.PedidoRepository;
import io.github.neudopb.service.PedidoService;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService {

    private PedidoRepository repository;

    public PedidoServiceImpl(PedidoRepository repository) {
        this.repository = repository;
    }
}
