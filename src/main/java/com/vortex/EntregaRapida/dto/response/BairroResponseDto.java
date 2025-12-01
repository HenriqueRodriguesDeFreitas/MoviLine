package com.vortex.EntregaRapida.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record BairroResponseDto(@Schema(name = "Id", description = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
                                @Schema(name = "Nome", description = "Aeroporto") String nome) {
}
