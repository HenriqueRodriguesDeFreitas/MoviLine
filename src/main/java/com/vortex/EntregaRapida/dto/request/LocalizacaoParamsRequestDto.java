package com.vortex.EntregaRapida.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record LocalizacaoParamsRequestDto(@NotBlank UUID estadoId,
                                          @NotBlank UUID cidadeId,
                                          @NotBlank UUID bairroId,
                                          @NotBlank UUID ruaId) {
}
