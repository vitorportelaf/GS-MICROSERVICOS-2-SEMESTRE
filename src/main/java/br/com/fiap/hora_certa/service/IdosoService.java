package br.com.fiap.hora_certa.service;

import br.com.fiap.hora_certa.dto.IdosoCreateRequest;
import br.com.fiap.hora_certa.dto.IdosoResponse;
import br.com.fiap.hora_certa.dto.IdosoUpdateRequest;
import br.com.fiap.hora_certa.exception.RecursoNaoEncontradoException;
import br.com.fiap.hora_certa.exception.RegraNegocioException;
import br.com.fiap.hora_certa.mapper.IdosoMapper;
import br.com.fiap.hora_certa.model.Idoso;
import br.com.fiap.hora_certa.repository.IdosoRepository;
import br.com.fiap.hora_certa.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IdosoService {

    /** Tempo padrão até avisar o cuidador (o mesmo do app Hora Certa). */
    static final int TOLERANCIA_PADRAO_MINUTOS = 30;

    private final IdosoRepository repository;
    private final MedicamentoRepository medicamentoRepository;
    private final CuidadorService cuidadorService;
    private final IdosoMapper mapper;

    public IdosoService(IdosoRepository repository, MedicamentoRepository medicamentoRepository,
                        CuidadorService cuidadorService, IdosoMapper mapper) {
        this.repository = repository;
        this.medicamentoRepository = medicamentoRepository;
        this.cuidadorService = cuidadorService;
        this.mapper = mapper;
    }

    @Transactional
    public IdosoResponse cadastrar(IdosoCreateRequest dto) {
        Idoso idoso = mapper.toModel(dto);
        idoso.setCuidador(cuidadorService.buscarEntidade(dto.getCuidadorId()));
        aplicarPadroes(idoso);
        return mapper.toDto(repository.save(idoso));
    }

    @Transactional(readOnly = true)
    public List<IdosoResponse> listar() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public IdosoResponse buscarPorId(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public IdosoResponse atualizar(Long id, IdosoUpdateRequest dto) {
        Idoso idoso = buscarEntidade(id);
        mapper.toModel(dto, idoso);
        idoso.setCuidador(cuidadorService.buscarEntidade(dto.getCuidadorId()));
        aplicarPadroes(idoso);
        return mapper.toDto(repository.save(idoso));
    }

    @Transactional
    public void excluir(Long id) {
        Idoso idoso = buscarEntidade(id);
        if (medicamentoRepository.existsByIdosoId(id)) {
            throw new RegraNegocioException(
                    "O idoso possui medicamentos cadastrados. Exclua os medicamentos antes.");
        }
        repository.delete(idoso);
    }

    Idoso buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Idoso", id));
    }

    private void aplicarPadroes(Idoso idoso) {
        if (idoso.getToleranciaMinutos() == null) {
            idoso.setToleranciaMinutos(TOLERANCIA_PADRAO_MINUTOS);
        }
    }
}
