package com.vortex.EntregaRapida.exception;

import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandle {

    ErroResponseDto response;

    @ExceptionHandler(ConflitoDeEntidadeException.class)
    public ResponseEntity<ErroResponseDto> conflitoDeEntidadeHandle(
            ConflitoDeEntidadeException ex) {
        response = toResponse(
                HttpStatus.CONFLICT, "Erro: conflito de entidade", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ConflitoEntidadeInexistente.class)
    public ResponseEntity<ErroResponseDto> conflitoDeEntidadeHandle(
            ConflitoEntidadeInexistente ex) {
        response = toResponse(
                HttpStatus.NOT_FOUND, "Erro: entidade inexistente", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
