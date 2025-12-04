package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CidadePorIdRequestDto(
        @NotNull UUID estadoId,
        @NotNull UUID cidadeId) {
}
