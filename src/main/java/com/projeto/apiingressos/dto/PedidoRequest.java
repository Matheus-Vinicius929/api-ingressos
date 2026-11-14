package com.projeto.apiingressos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PedidoRequest {

    @NotNull(message = "O showId é obrigatório")
    @Positive(message = "O showId deve ser positivo")
    private Long showId;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }
}
