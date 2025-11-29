package com.vortex.EntregaRapida.mapper;

import com.vortex.EntregaRapida.dto.response.CidadeResponseDto;
import com.vortex.EntregaRapida.model.Cidade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CidadeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CidadeResponseDto toResponse(Cidade cidade);
}
