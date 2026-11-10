package br.com.fiap.hora_certa.dto;

import br.com.fiap.hora_certa.model.StatusDose;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Dados para atualizar uma dose (o medicamento não muda)")
public class DoseUpdateRequest {

    @Schema(example = "2026-11-16T08:00:00")
    @NotNull(message = "A data e hora prevista é obrigatória.")
    private LocalDateTime dataHoraPrevista;

    @Schema(description = "Quando o idoso apertou Tomei (se informado, a dose fica TOMADA)", example = "2026-11-16T08:02:00")
    private LocalDateTime dataHoraConfirmacao;

    @Schema(description = "PENDENTE, TOMADA ou NAO_CONFIRMADA", example = "NAO_CONFIRMADA")
    @NotNull(message = "O status é obrigatório.")
    private StatusDose status;

    @Schema(description = "Se o cuidador já foi avisado", example = "true")
    @NotNull(message = "Informe se o cuidador foi avisado.")
    private Boolean cuidadorAvisado;

    @Schema(example = "Tomou depois do café.")
    @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
    private String observacao;

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

    public StatusDose getStatus() {
        return status;
    }

    public void setStatus(StatusDose status) {
        this.status = status;
    }

    public Boolean getCuidadorAvisado() {
        return cuidadorAvisado;
    }

    public void setCuidadorAvisado(Boolean cuidadorAvisado) {
        this.cuidadorAvisado = cuidadorAvisado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
