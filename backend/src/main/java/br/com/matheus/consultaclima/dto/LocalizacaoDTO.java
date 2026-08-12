package br.com.matheus.consultaclima.dto;

public record LocalizacaoDTO(
        String nome,
        Double latitude,
        Double longitude,
        String pais,
        String estado,
        String fusoHorario
) {
}