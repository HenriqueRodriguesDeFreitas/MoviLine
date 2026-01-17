package com.vortex.EntregaRapida.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record BairroResponseDto(@Schema(name = "Id",
        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
                                @Schema(name = "Estado",
                                        example = "Pará") String estado,
                                @Schema(name = "Cidade",
                                        example = "Breves") String cidade,
                                @Schema(name = "Nome",
                                        example = "Aeroporto") String nome) {
}
