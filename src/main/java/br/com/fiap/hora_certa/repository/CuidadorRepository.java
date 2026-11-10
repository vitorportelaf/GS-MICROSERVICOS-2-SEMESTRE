package br.com.fiap.hora_certa.repository;

import br.com.fiap.hora_certa.model.Cuidador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
