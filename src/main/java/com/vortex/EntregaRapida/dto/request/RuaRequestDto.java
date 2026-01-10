package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RuaRequestDto(@NotNull UUID estadoId,
                            @NotNull UUID cidadeId,
                            @NotNull UUID bairroId,
                            @NotBlank String nome,
                            @NotBlank @Size(min = 8 ,max = 8) String cep) {
}
