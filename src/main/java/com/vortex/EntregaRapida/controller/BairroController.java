package com.vortex.EntregaRapida.controller;

import com.vortex.EntregaRapida.docs.ErroExamples;
import com.vortex.EntregaRapida.dto.request.BairroPorIdRequestDto;
import com.vortex.EntregaRapida.dto.request.BairroRequestDto;
import com.vortex.EntregaRapida.dto.response.BairroResponseDto;
import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.service.BairroService;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Bairro", description = "Operações relacionadas ao cadastro e manutenções de bairros")
@RestController
@RequestMapping("bairro")
public class BairroController {

    private final BairroService bairroService;
    private static final String TYPE_JSON = "application/json";
    private static final String DESC_CODE_404 = "Erro de recurso não encontrado.";
    private static final String DESC_CODE_409 = "Erro de conflito entre entidades.";

    public BairroController(BairroService bairroService) {
        this.bairroService = bairroService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar bairro",
            description = "Método usado para cadastrar um novo bairro em uma cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Bairro cadastrado com sucesso.",
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = BairroResponseDto.class))),

            @ApiResponse(responseCode = "404",
                    description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(name = "Estado não possui cidade com o id informado.",
                                    value = ErroExamples.ERRO_404))),
            @ApiResponse(responseCode = "409",
                    description = DESC_CODE_409,
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(name = "Cidade já possui bairro com mesmo nome",
                                    value = ErroExamples.ERRO_409)))
    })
    public ResponseEntity<BairroResponseDto> cadastrarBairro(@RequestBody @Valid BairroRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bairroService.cadastrarBairro(dto));
    }

    @PutMapping("/{idBairro}")
    @Operation(summary = "Atualizar bairro", description = "Método usado para atualizar dados de um bairro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.",
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = BairroResponseDto.class))),

            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = "Nenhum bairro encontrado com o id informado.",
                                    value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = "Estado não possui cidade com o id informado.",
                                            value = ErroExamples.ERRO_404)})),

            @ApiResponse(responseCode = "409", description = DESC_CODE_409,
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = "Cidade já possui bairro com mesmo nome.",
                                    value = ErroExamples.ERRO_409)}
                    ))
    })
    public ResponseEntity<BairroResponseDto> atualizarBairro(@PathVariable("idBairro") UUID id,
                                                             @RequestBody @Valid BairroRequestDto dto) {
        return ResponseEntity.ok(bairroService.atualizarBairro(id, dto));
    }

    @GetMapping("/{bairroId}")
    @Operation(summary = "Busca bairro por id",
            description = "Busca o bairro de uma cidade de acordo com seu id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca retornada com sucesso",
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = BairroResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(name = "Cidade não possui bairro com o id informado.",
                                    value = ErroExamples.ERRO_404)))
    })
    public ResponseEntity<BairroResponseDto> buscarPorId(@RequestParam UUID estadoId,
                                                         @RequestParam UUID cidadeId,
                                                         @RequestParam UUID bairroId) {
        var dto = new BairroPorIdRequestDto(estadoId, cidadeId, bairroId);
        return ResponseEntity.ok(bairroService.buscarPorId(dto));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Busca bairro por nome", description = "Retorna bairro de uma cidade de acordo com o nome pesquisado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca retornada com sucesso.",
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = BairroResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON, schema =
                    @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = "Estado não possui cidade com o id informado",
                                    value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = "Cidade não possui bairro com o mesmo nome",
                                            value = ErroExamples.ERRO_404)
                            }))
    })
    public ResponseEntity<BairroResponseDto> buscarPorNome(
            @RequestParam String nome,
            @RequestParam UUID cidadeId,
            @RequestParam UUID estadoId) {
        var dto = new BairroRequestDto(estadoId, cidadeId, nome);
        return ResponseEntity.ok(bairroService.buscarBairroPorNome(dto));
    }
}
