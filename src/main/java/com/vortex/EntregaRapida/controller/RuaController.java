package com.vortex.EntregaRapida.controller;

import com.vortex.EntregaRapida.docs.ErroExamples;
import com.vortex.EntregaRapida.dto.request.RuaRequestDto;
import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.mapper.PageResponseMapper;
import com.vortex.EntregaRapida.service.RuaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Rua", description = "Operações relacionadas ao cadastro e manutenções de cidades.")
@RequestMapping("rua")
public class RuaController {

    private final RuaService ruaService;
    private static final String TYPE_JSON = "application/json";
    private static final String DESC_CODE_404 = "Erro de recurso não encontrado.";
    private static final String DESC_CODE_409 = "Erro de conflito entre entidades.";
    private final PageResponseMapper pageResponseMapper;

    public RuaController(RuaService ruaService, PageResponseMapper pageResponseMapper) {
        this.ruaService = ruaService;
        this.pageResponseMapper = pageResponseMapper;
    }

    @PostMapping
    @Operation(summary = "Cadastrar rua", description = "Método usado para cadastrar uma nova rua em um bairro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rua cadastrada com sucesso.",
            content = @Content(mediaType = TYPE_JSON,
                    schema = @Schema(implementation = RuaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
            content = @Content(mediaType = TYPE_JSON,
            schema = @Schema(implementation = ErroResponseDto.class),
            examples = {
                    @ExampleObject(name = "Estado não possui a cidade com o Id informado.",
                    value = ErroExamples.ERRO_404),
                    @ExampleObject(name = "Cidade não possui um bairro com o Id informado.",
                    value = ErroExamples.ERRO_404)
            })),
            @ApiResponse(responseCode = "409", description = DESC_CODE_409,
            content =  @Content(mediaType = TYPE_JSON,
            schema = @Schema(implementation = ErroResponseDto.class),
            examples = {
                    @ExampleObject(name = "Bairro já possui rua com o mesmo nome.",
                    value = ErroExamples.ERRO_409)
            }))
    })
    public ResponseEntity<RuaResponseDto> cadastrarRua(@RequestBody @Valid RuaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ruaService.cadastrarRua(request));
    }
}
