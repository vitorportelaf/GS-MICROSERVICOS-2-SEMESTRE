package br.com.fiap.hora_certa.mapper;

import br.com.fiap.hora_certa.dto.IdosoCreateRequest;
import br.com.fiap.hora_certa.dto.IdosoResponse;
import br.com.fiap.hora_certa.dto.IdosoUpdateRequest;
import br.com.fiap.hora_certa.model.Idoso;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class IdosoMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public IdosoMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // O cuidador é buscado pela Service a partir do cuidadorId (nunca pelo ModelMapper).
        modelMapper.typeMap(IdosoCreateRequest.class, Idoso.class)
                .addMappings(m -> {
                    m.skip(Idoso::setId);
                    m.skip(Idoso::setCuidador);
                });
        modelMapper.typeMap(IdosoUpdateRequest.class, Idoso.class)
                .addMappings(m -> {
                    m.skip(Idoso::setId);
                    m.skip(Idoso::setCuidador);
                });
        modelMapper.typeMap(Idoso.class, IdosoResponse.class)
                .addMappings(m -> {
                    m.map(src -> src.getCuidador().getId(), IdosoResponse::setCuidadorId);
                    m.map(src -> src.getCuidador().getNome(), IdosoResponse::setCuidadorNome);
                });
    }

    public Idoso toModel(IdosoCreateRequest dto) {
        return modelMapper.map(dto, Idoso.class);
    }

    public void toModel(IdosoUpdateRequest dto, Idoso idoso) {
        modelMapper.map(dto, idoso);
    }

    public IdosoResponse toDto(Idoso entity) {
        return modelMapper.map(entity, IdosoResponse.class);
    }
}
