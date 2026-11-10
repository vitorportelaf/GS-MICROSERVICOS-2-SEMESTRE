package br.com.fiap.hora_certa.controller;

import br.com.fiap.hora_certa.dto.IdosoCreateRequest;
import br.com.fiap.hora_certa.dto.IdosoResponse;
import br.com.fiap.hora_certa.dto.IdosoUpdateRequest;
import br.com.fiap.hora_certa.dto.ErroResponse;
import br.com.fiap.hora_certa.service.IdosoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/${api.version}/idosos")
@Tag(name = "Idosos", description = "Pessoas idosas acompanhadas pelo Hora Certa")
public class IdosoController {

    private final IdosoService service;

    public IdosoController(IdosoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar idoso")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Idoso cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cuidador informado não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<IdosoResponse> cadastrar(@Valid @RequestBody IdosoCreateRequest dto) {
        IdosoResponse criado = service.cadastrar(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar idosos")
    @ApiResponse(responseCode = "200", description = "Lista de idosos")
    public ResponseEntity<List<IdosoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar idoso pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Idoso encontrado"),
            @ApiResponse(responseCode = "404", description = "Idoso não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<IdosoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar idoso")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Idoso atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Idoso não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<IdosoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody IdosoUpdateRequest dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir idoso")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Idoso excluído"),
            @ApiResponse(responseCode = "404", description = "Idoso não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Idoso possui medicamentos cadastrados", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
