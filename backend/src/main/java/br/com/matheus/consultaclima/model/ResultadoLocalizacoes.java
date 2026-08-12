package br.com.matheus.consultaclima.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadoLocalizacoes(
        @JsonAlias("results") List<DadosLocalizacao> resultados
) {
}