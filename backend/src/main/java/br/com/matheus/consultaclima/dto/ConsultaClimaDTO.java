package br.com.matheus.consultaclima.dto;

import java.time.LocalDateTime;

public record ConsultaClimaDTO(
        Long id,
        String cidade,
        String estado,
        String pais,
        Double temperatura,
        Double sensacaoTermica,
        Integer umidade,
        Double velocidadeVento,
        String condicaoClimatica,
        LocalDateTime dataConsulta
) {
}