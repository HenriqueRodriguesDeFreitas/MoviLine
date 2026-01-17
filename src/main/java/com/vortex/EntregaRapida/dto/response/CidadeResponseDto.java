package com.vortex.EntregaRapida.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CidadeResponseDto(
        @Schema(name = "Id da cidade", examples = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
        @Schema(name = "Estado", example = "Pará") String estado,
        @Schema(name = "Nome", example = "Breves") String nome) {
}
