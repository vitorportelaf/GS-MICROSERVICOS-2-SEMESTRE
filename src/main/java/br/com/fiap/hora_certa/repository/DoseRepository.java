package br.com.fiap.hora_certa.repository;

import br.com.fiap.hora_certa.model.Dose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoseRepository extends JpaRepository<Dose, Long> {

    boolean existsByMedicamentoId(Long medicamentoId);
}
