package br.com.matheus.consultaclima.dto;

public record ClimaDTO(
        Double temperatura,
        Double sensacaoTermica,
        Integer umidade,
        Double velocidadeVento,
        String condicaoClimatica
) {
}