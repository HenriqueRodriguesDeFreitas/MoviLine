package com.vortex.EntregaRapida.mapper;

import com.vortex.EntregaRapida.dto.request.EstadoRequestDto;
import com.vortex.EntregaRapida.dto.response.EstadoResponseDto;
import com.vortex.EntregaRapida.model.Estado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EstadoResponseDto toResponse(Estado estado);

    @Mapping(source = "nome", target = "nome")
    Estado toEntity(EstadoRequestDto requestDto);
}
