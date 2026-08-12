package br.com.matheus.consultaclima.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLocalizacao(
        @JsonAlias("name") String nome,
        @JsonAlias("latitude") Double latitude,
        @JsonAlias("longitude") Double longitude,
        @JsonAlias("country") String pais,
        @JsonAlias("admin1") String estado,
        @JsonAlias("timezone") String fusoHorario
) {
}