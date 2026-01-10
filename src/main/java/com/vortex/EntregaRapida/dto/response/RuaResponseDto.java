package com.vortex.EntregaRapida.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record RuaResponseDto(@Schema(description = "Nome do estado",
        example = "Pará") String estado,
                             @Schema(description = "Nome da cidade",
                                     example = "Breves") String cidade,
                             @Schema(description = "Nome da cidade",
                                     example = "Aeroporto") String bairro,
                             @Schema(description = "Id da rua",
                                     example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
                             @Schema(description = "Nome da rua",
                                     example = "Presidente Vargas") String nome) {
}
