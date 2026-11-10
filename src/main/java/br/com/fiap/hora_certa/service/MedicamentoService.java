package br.com.fiap.hora_certa.service;

import br.com.fiap.hora_certa.dto.MedicamentoCreateRequest;
import br.com.fiap.hora_certa.dto.MedicamentoResponse;
import br.com.fiap.hora_certa.dto.MedicamentoUpdateRequest;
import br.com.fiap.hora_certa.exception.RecursoNaoEncontradoException;
import br.com.fiap.hora_certa.exception.RegraNegocioException;
import br.com.fiap.hora_certa.mapper.MedicamentoMapper;
import br.com.fiap.hora_certa.model.Medicamento;
import br.com.fiap.hora_certa.repository.DoseRepository;
import br.com.fiap.hora_certa.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicamentoService {

    private final MedicamentoRepository repository;
    private final DoseRepository doseRepository;
    private final IdosoService idosoService;
    private final MedicamentoMapper mapper;

    public MedicamentoService(MedicamentoRepository repository, DoseRepository doseRepository,
                              IdosoService idosoService, MedicamentoMapper mapper) {
        this.repository = repository;
        this.doseRepository = doseRepository;
        this.idosoService = idosoService;
        this.mapper = mapper;
    }

    @Transactional
    public MedicamentoResponse cadastrar(MedicamentoCreateRequest dto) {
        Medicamento medicamento = mapper.toModel(dto);
        medicamento.setIdoso(idosoService.buscarEntidade(dto.getIdosoId()));
        medicamento.setAtivo(true); // todo medicamento novo começa com o lembrete ligado
        if (medicamento.getLembretePorVoz() == null) {
            medicamento.setLembretePorVoz(true);
        }
        return mapper.toDto(repository.save(medicamento));
    }

    @Transactional(readOnly = true)
    public List<MedicamentoResponse> listar() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MedicamentoResponse buscarPorId(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public MedicamentoResponse atualizar(Long id, MedicamentoUpdateRequest dto) {
        Medicamento medicamento = buscarEntidade(id);
        mapper.toModel(dto, medicamento);
        if (medicamento.getLembretePorVoz() == null) {
            medicamento.setLembretePorVoz(true);
        }
        return mapper.toDto(repository.save(medicamento));
    }

    @Transactional
    public void excluir(Long id) {
        Medicamento medicamento = buscarEntidade(id);
        if (doseRepository.existsByMedicamentoId(id)) {
            throw new RegraNegocioException(
                    "O medicamento possui doses registradas. Para parar os lembretes, atualize com ativo = false.");
        }
        repository.delete(medicamento);
    }

    Medicamento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Medicamento", id));
    }
}
