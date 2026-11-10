package br.com.fiap.hora_certa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Dados para registrar uma dose")
public class DoseCreateRequest {

    @Schema(example = "2026-11-16T08:00:00")
    @NotNull(message = "A data e hora prevista é obrigatória.")
    private LocalDateTime dataHoraPrevista;

    @Schema(description = "Quando o idoso apertou Tomei (se informado, a dose fica TOMADA)", example = "2026-11-16T08:02:00")
    private LocalDateTime dataHoraConfirmacao;

    @Schema(example = "Tomou depois do café.")
    @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
    private String observacao;

    @Schema(description = "Id do medicamento", example = "1")
    @NotNull(message = "O medicamento é obrigatório.")
    private Long medicamentoId;

    public LocalDateTime getDataHoraPrevista() {
        return dataHoraPrevista;
    }

    public void setDataHoraPrevista(LocalDateTime dataHoraPrevista) {
        this.dataHoraPrevista = dataHoraPrevista;
    }

    public LocalDateTime getDataHoraConfirmacao() {
        return dataHoraConfirmacao;
    }

    public void setDataHoraConfirmacao(LocalDateTime dataHoraConfirmacao) {
        this.dataHoraConfirmacao = dataHoraConfirmacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }
}
