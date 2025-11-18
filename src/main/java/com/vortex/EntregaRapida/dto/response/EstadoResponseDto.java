package com.vortex.EntregaRapida.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EstadoResponseDto(
        @Schema(description = "Id do estado",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
        @Schema(description = "Nome do estado", example = "Pará") String nome) {
}
