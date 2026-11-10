package br.com.fiap.hora_certa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

public class MedicamentoResponse {

    private Long id;

    private String nome;

    private String dosagem;

    @Schema(type = "string", pattern = "HH:mm", example = "08:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;

    private Boolean lembretePorVoz;

    private Boolean ativo;

    private Long idosoId;

    private String idosoNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Boolean getLembretePorVoz() {
        return lembretePorVoz;
    }

    public void setLembretePorVoz(Boolean lembretePorVoz) {
        this.lembretePorVoz = lembretePorVoz;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getIdosoId() {
        return idosoId;
    }

    public void setIdosoId(Long idosoId) {
        this.idosoId = idosoId;
    }

    public String getIdosoNome() {
        return idosoNome;
    }

    public void setIdosoNome(String idosoNome) {
        this.idosoNome = idosoNome;
    }
}
