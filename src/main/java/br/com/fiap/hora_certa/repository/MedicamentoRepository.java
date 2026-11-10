package br.com.fiap.hora_certa.repository;

import br.com.fiap.hora_certa.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    boolean existsByIdosoId(Long idosoId);
}
