package br.com.matheus.consultaclima.repository;

import br.com.matheus.consultaclima.model.ConsultaClima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsultaClimaRepository extends JpaRepository<ConsultaClima, Long> {
    List<ConsultaClima> findByCidadeContainingIgnoreCase(String cidade);

    List<ConsultaClima> findByTemperaturaGreaterThanEqual(Double temperatura);

    List<ConsultaClima> findTop5ByOrderByDataConsultaDesc();

    @Query("SELECT c.cidade, COUNT(c) FROM ConsultaClima c GROUP BY c.cidade ORDER BY COUNT(c) DESC")
    List<Object[]> buscarCidadesMaisConsultadas();
}