package br.com.fiap.hora_certa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/** Registro de uma dose: horário previsto, confirmação (botão Tomei) e aviso ao cuidador. */
@Entity
@Table(name = "doses")
public class Dose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_prevista", nullable = false)
    private LocalDateTime dataHoraPrevista;

    @Column(name = "data_hora_confirmacao")
    private LocalDateTime dataHoraConfirmacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusDose status;

    @Column(name = "cuidador_avisado", nullable = false)
    private Boolean cuidadorAvisado;

    @Column(length = 255)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

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

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}
