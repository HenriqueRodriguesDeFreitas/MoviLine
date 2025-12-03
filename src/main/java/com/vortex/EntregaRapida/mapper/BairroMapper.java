package com.vortex.EntregaRapida.mapper;

import com.vortex.EntregaRapida.dto.response.BairroResponseDto;
import com.vortex.EntregaRapida.model.Bairro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BairroMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    BairroResponseDto toResponse(Bairro bairro);
}
