package br.com.fiap.hora_certa.controller;

import br.com.fiap.hora_certa.dto.MedicamentoCreateRequest;
import br.com.fiap.hora_certa.dto.MedicamentoResponse;
import br.com.fiap.hora_certa.dto.MedicamentoUpdateRequest;
import br.com.fiap.hora_certa.dto.ErroResponse;
import br.com.fiap.hora_certa.service.MedicamentoService;
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
@RequestMapping("api/${api.version}/medicamentos")
@Tag(name = "Medicamentos", description = "Remédios de uso diário e horário do lembrete")
public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar medicamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medicamento cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Idoso informado não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<MedicamentoResponse> cadastrar(@Valid @RequestBody MedicamentoCreateRequest dto) {
        MedicamentoResponse criado = service.cadastrar(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar medicamentos")
    @ApiResponse(responseCode = "200", description = "Lista de medicamentos")
    public ResponseEntity<List<MedicamentoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar medicamento pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Medicamento não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<MedicamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar medicamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicamento atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Medicamento não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<MedicamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody MedicamentoUpdateRequest dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir medicamento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medicamento excluído"),
            @ApiResponse(responseCode = "404", description = "Medicamento não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Medicamento possui doses registradas", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
