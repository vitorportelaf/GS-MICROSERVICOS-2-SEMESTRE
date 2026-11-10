package br.com.fiap.hora_certa.service;

import br.com.fiap.hora_certa.dto.CuidadorCreateRequest;
import br.com.fiap.hora_certa.dto.CuidadorResponse;
import br.com.fiap.hora_certa.dto.CuidadorUpdateRequest;
import br.com.fiap.hora_certa.exception.RecursoNaoEncontradoException;
import br.com.fiap.hora_certa.exception.RegraNegocioException;
import br.com.fiap.hora_certa.mapper.CuidadorMapper;
import br.com.fiap.hora_certa.model.Cuidador;
import br.com.fiap.hora_certa.repository.CuidadorRepository;
import br.com.fiap.hora_certa.repository.IdosoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuidadorService {

    private final CuidadorRepository repository;
    private final IdosoRepository idosoRepository;
    private final CuidadorMapper mapper;

    public CuidadorService(CuidadorRepository repository, IdosoRepository idosoRepository, CuidadorMapper mapper) {
        this.repository = repository;
        this.idosoRepository = idosoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public CuidadorResponse cadastrar(CuidadorCreateRequest dto) {
        if (repository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RegraNegocioException("Já existe um cuidador com o e-mail " + dto.getEmail() + ".");
        }
        return mapper.toDto(repository.save(mapper.toModel(dto)));
    }

    @Transactional(readOnly = true)
    public List<CuidadorResponse> listar() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CuidadorResponse buscarPorId(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public CuidadorResponse atualizar(Long id, CuidadorUpdateRequest dto) {
        Cuidador cuidador = buscarEntidade(id);
        if (repository.existsByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)) {
            throw new RegraNegocioException("Já existe outro cuidador com o e-mail " + dto.getEmail() + ".");
        }
        mapper.toModel(dto, cuidador);
        return mapper.toDto(repository.save(cuidador));
    }

    @Transactional
    public void excluir(Long id) {
        Cuidador cuidador = buscarEntidade(id);
        if (idosoRepository.existsByCuidadorId(id)) {
            throw new RegraNegocioException(
                    "O cuidador possui idosos vinculados. Exclua ou transfira os idosos antes.");
        }
        repository.delete(cuidador);
    }

    /** Também usado pela IdosoService para validar o cuidador informado. */
    Cuidador buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cuidador", id));
    }
}
