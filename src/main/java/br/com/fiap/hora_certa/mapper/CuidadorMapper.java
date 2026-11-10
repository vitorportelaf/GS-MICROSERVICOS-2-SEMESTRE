package br.com.fiap.hora_certa.mapper;

import br.com.fiap.hora_certa.dto.CuidadorCreateRequest;
import br.com.fiap.hora_certa.dto.CuidadorResponse;
import br.com.fiap.hora_certa.dto.CuidadorUpdateRequest;
import br.com.fiap.hora_certa.model.Cuidador;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class CuidadorMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public CuidadorMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public Cuidador toModel(CuidadorCreateRequest dto) {
        return modelMapper.map(dto, Cuidador.class);
    }

    /** Copia os dados do DTO para o cuidador que já existe no banco. */
    public void toModel(CuidadorUpdateRequest dto, Cuidador cuidador) {
        modelMapper.map(dto, cuidador);
    }

    public CuidadorResponse toDto(Cuidador entity) {
        return modelMapper.map(entity, CuidadorResponse.class);
    }
}
