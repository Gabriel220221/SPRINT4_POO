package br.com.motiva.repository;

import br.com.motiva.model.Intervencao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface IntervencaoRepository extends JpaRepository<Intervencao, Long> {
    List<Intervencao> findByStatus(String status);
    List<Intervencao> findByTrechoId(Long trechoId);
    List<Intervencao> findByDataPrevistaBetween(LocalDate inicio, LocalDate fim);
}
