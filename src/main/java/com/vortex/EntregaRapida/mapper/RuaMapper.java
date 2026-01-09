package com.vortex.EntregaRapida.mapper;

import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.model.Rua;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RuaMapper {

    @Mapping(source = "bairro.nome", target = "bairro")
    @Mapping(source = "bairro.cidade.nome", target = "cidade")
    @Mapping(source = "bairro.cidade.estado.nome", target = "estado")
    RuaResponseDto toResponse(Rua rua);
}
