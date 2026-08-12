package br.com.matheus.consultaclima.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadoClima(

        @JsonAlias("current")
        DadosClimaAtual climaAtual
) {
}