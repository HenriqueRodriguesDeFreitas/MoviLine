package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RuaRequestDto(@NotNull UUID estadoId,
                            @NotNull UUID cidadeId,
                            @NotNull UUID bairroId,
                            @NotBlank String nome) {
}
