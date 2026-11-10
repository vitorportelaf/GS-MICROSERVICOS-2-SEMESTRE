package br.com.fiap.hora_certa.mapper;

import br.com.fiap.hora_certa.dto.DoseCreateRequest;
import br.com.fiap.hora_certa.dto.DoseResponse;
import br.com.fiap.hora_certa.dto.DoseUpdateRequest;
import br.com.fiap.hora_certa.model.Dose;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class DoseMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public DoseMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(DoseCreateRequest.class, Dose.class)
                .addMappings(m -> {
                    m.skip(Dose::setId);
                    m.skip(Dose::setMedicamento);
                });
        modelMapper.typeMap(DoseUpdateRequest.class, Dose.class)
                .addMappings(m -> {
                    m.skip(Dose::setId);
                    m.skip(Dose::setMedicamento);
                });
        modelMapper.typeMap(Dose.class, DoseResponse.class)
                .addMappings(m -> {
                    m.map(src -> src.getMedicamento().getId(), DoseResponse::setMedicamentoId);
                    m.map(src -> src.getMedicamento().getNome(), DoseResponse::setMedicamentoNome);
                });
    }

    public Dose toModel(DoseCreateRequest dto) {
        return modelMapper.map(dto, Dose.class);
    }

    public void toModel(DoseUpdateRequest dto, Dose dose) {
        modelMapper.map(dto, dose);
    }

    public DoseResponse toDto(Dose entity) {
        return modelMapper.map(entity, DoseResponse.class);
    }
}
