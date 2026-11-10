package br.com.fiap.hora_certa.dto;

import br.com.fiap.hora_certa.model.StatusDose;
import java.time.LocalDateTime;

public class DoseResponse {

    private Long id;

    private LocalDateTime dataHoraPrevista;

    private LocalDateTime dataHoraConfirmacao;

    private StatusDose status;

    private Boolean cuidadorAvisado;

    private String observacao;

    private Long medicamentoId;

    private String medicamentoNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public String getMedicamentoNome() {
        return medicamentoNome;
    }

    public void setMedicamentoNome(String medicamentoNome) {
        this.medicamentoNome = medicamentoNome;
    }
}
