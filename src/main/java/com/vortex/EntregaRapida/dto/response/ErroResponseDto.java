package com.vortex.EntregaRapida.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErroResponseDto(
        @JsonProperty("data_hora") String timestamp,
        int httpsValue,
        String erro,
        String descricao) {
}
