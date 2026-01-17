package com.vortex.EntregaRapida.mapper;

import com.vortex.EntregaRapida.dto.response.PageResponseDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

    default <T> PageResponseDto<T> toPageResponse(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
