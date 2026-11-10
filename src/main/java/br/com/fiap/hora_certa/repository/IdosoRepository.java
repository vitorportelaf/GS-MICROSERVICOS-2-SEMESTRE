package br.com.fiap.hora_certa.repository;

import br.com.fiap.hora_certa.model.Idoso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdosoRepository extends JpaRepository<Idoso, Long> {

    boolean existsByCuidadorId(Long cuidadorId);
}
