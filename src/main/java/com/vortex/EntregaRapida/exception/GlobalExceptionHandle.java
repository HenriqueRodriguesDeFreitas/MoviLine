package com.vortex.EntregaRapida.exception;

import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandle {

    ErroResponseDto response;

    @ExceptionHandler(ConflitoDeEntidadeException.class)
    public ResponseEntity<ErroResponseDto> ConflitoDeEntidadeHandle(
            ConflitoDeEntidadeException ex) {
        response = toResponse(
                HttpStatus.CONFLICT, "Erro: conflito de entidade", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDto> conflitoArgumentoInvalido(IllegalArgumentException ex) {
    response = toResponse(HttpStatus.BAD_REQUEST,
            "Erro: entrada de argumento inválido", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private static ErroResponseDto toResponse(HttpStatus status,
                                              String erro,
                                              Exception e) {
        return new ErroResponseDto(LocalDateTime.now(),
                status.value(),
                erro,
                e.getMessage());
    }
}
