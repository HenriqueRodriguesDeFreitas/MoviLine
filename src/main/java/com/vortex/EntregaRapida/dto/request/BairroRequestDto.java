package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BairroRequestDto(@Size(max = 150) @NotBlank String nome,
                               @NotNull UUID cidadeId) {
}
