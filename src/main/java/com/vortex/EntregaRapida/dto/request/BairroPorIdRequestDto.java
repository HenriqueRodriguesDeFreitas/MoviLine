package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BairroPorIdRequestDto(@NotNull UUID estadoId,
                                    @NotNull UUID cidadeId,
                                    @NotNull UUID bairroId) {
}
