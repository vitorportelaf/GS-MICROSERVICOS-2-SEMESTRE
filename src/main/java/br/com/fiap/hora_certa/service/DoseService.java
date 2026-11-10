package br.com.fiap.hora_certa.service;

import br.com.fiap.hora_certa.dto.DoseCreateRequest;
import br.com.fiap.hora_certa.dto.DoseResponse;
import br.com.fiap.hora_certa.dto.DoseUpdateRequest;
import br.com.fiap.hora_certa.exception.RecursoNaoEncontradoException;
import br.com.fiap.hora_certa.exception.RegraNegocioException;
import br.com.fiap.hora_certa.mapper.DoseMapper;
import br.com.fiap.hora_certa.model.Dose;
import br.com.fiap.hora_certa.model.Medicamento;
import br.com.fiap.hora_certa.model.StatusDose;
import br.com.fiap.hora_certa.repository.DoseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoseService {

    private final DoseRepository repository;
    private final MedicamentoService medicamentoService;
    private final DoseMapper mapper;

    public DoseService(DoseRepository repository, MedicamentoService medicamentoService, DoseMapper mapper) {
        this.repository = repository;
        this.medicamentoService = medicamentoService;
        this.mapper = mapper;
    }

    @Transactional
    public DoseResponse cadastrar(DoseCreateRequest dto) {
        Medicamento medicamento = medicamentoService.buscarEntidade(dto.getMedicamentoId());
        if (!medicamento.getAtivo()) {
            throw new RegraNegocioException("O medicamento está inativo e não recebe novas doses.");
        }
        Dose dose = mapper.toModel(dto);
        dose.setMedicamento(medicamento);
        dose.setCuidadorAvisado(false);
        dose.setStatus(dose.getDataHoraConfirmacao() != null ? StatusDose.TOMADA : StatusDose.PENDENTE);
        validarConfirmacao(dose);
        return mapper.toDto(repository.save(dose));
    }

    @Transactional(readOnly = true)
    public List<DoseResponse> listar() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public DoseResponse buscarPorId(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public DoseResponse atualizar(Long id, DoseUpdateRequest dto) {
        Dose dose = buscarEntidade(id);
        mapper.toModel(dto, dose);
        aplicarStatus(dose);
        validarConfirmacao(dose);
        return mapper.toDto(repository.save(dose));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private Dose buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Dose", id));
    }

    /**
     * - Confirmação informada (botão Tomei): a dose fica TOMADA.
     * - TOMADA sem data de confirmação: registra a confirmação agora.
     * - NAO_CONFIRMADA não pode ter data de confirmação.
     */
    private void aplicarStatus(Dose dose) {
        if (dose.getStatus() == StatusDose.NAO_CONFIRMADA && dose.getDataHoraConfirmacao() != null) {
            throw new RegraNegocioException("Uma dose NAO_CONFIRMADA não pode ter data de confirmação.");
        }
        if (dose.getDataHoraConfirmacao() != null) {
            dose.setStatus(StatusDose.TOMADA);
        } else if (dose.getStatus() == StatusDose.TOMADA) {
            dose.setDataHoraConfirmacao(LocalDateTime.now());
        }
    }

    /** A confirmação não pode ser mais de 1 hora antes do horário previsto. */
    private void validarConfirmacao(Dose dose) {
        if (dose.getDataHoraConfirmacao() != null
                && dose.getDataHoraConfirmacao().isBefore(dose.getDataHoraPrevista().minusHours(1))) {
            throw new RegraNegocioException(
                    "A confirmação não pode ser mais de 1 hora antes do horário previsto da dose.");
        }
    }
}
