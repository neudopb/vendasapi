package io.github.neudopb.api.dto;

import io.github.neudopb.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "O campo código do cliente é obrigatório")
    private Integer cliente;
    @NotNull(message = "O campo total do pedido é obrigatório")
    private BigDecimal total;
    @NotEmptyList(message = "Pedido não pode ser realizado sem itens")
    private List<ItemPedidoDTO> itens;
}
