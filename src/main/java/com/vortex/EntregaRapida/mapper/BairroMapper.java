package com.vortex.EntregaRapida.mapper;

import com.vortex.EntregaRapida.dto.response.BairroResponseDto;
import com.vortex.EntregaRapida.model.Bairro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BairroMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "estado", source = "cidade.estado.nome")
    @Mapping(target = "cidade", source = "cidade.nome")
    BairroResponseDto toResponse(Bairro bairro);
}
