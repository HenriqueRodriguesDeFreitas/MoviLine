package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CidadeRequestDto(
        @Size(max = 32) @NotBlank String nome,
        @NotNull UUID idEstado) {
}
