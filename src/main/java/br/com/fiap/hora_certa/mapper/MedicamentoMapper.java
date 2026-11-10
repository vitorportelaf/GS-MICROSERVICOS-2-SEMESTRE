package br.com.fiap.hora_certa.mapper;

import br.com.fiap.hora_certa.dto.MedicamentoCreateRequest;
import br.com.fiap.hora_certa.dto.MedicamentoResponse;
import br.com.fiap.hora_certa.dto.MedicamentoUpdateRequest;
import br.com.fiap.hora_certa.model.Medicamento;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class MedicamentoMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public MedicamentoMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(MedicamentoCreateRequest.class, Medicamento.class)
                .addMappings(m -> {
                    m.skip(Medicamento::setId);
                    m.skip(Medicamento::setIdoso);
                });
        modelMapper.typeMap(MedicamentoUpdateRequest.class, Medicamento.class)
                .addMappings(m -> {
                    m.skip(Medicamento::setId);
                    m.skip(Medicamento::setIdoso);
                });
        modelMapper.typeMap(Medicamento.class, MedicamentoResponse.class)
                .addMappings(m -> {
                    m.map(src -> src.getIdoso().getId(), MedicamentoResponse::setIdosoId);
                    m.map(src -> src.getIdoso().getNome(), MedicamentoResponse::setIdosoNome);
                });
    }

    public Medicamento toModel(MedicamentoCreateRequest dto) {
        return modelMapper.map(dto, Medicamento.class);
    }

    public void toModel(MedicamentoUpdateRequest dto, Medicamento medicamento) {
        modelMapper.map(dto, medicamento);
    }

    public MedicamentoResponse toDto(Medicamento entity) {
        return modelMapper.map(entity, MedicamentoResponse.class);
    }
}
