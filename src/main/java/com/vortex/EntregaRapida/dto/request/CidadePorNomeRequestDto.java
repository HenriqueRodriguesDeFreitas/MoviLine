package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CidadePorNomeRequestDto(
        @NotNull UUID estadoId,
        @NotBlank String cidadeNome
) {
}
