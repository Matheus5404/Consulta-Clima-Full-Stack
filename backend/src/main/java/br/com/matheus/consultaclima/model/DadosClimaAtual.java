package br.com.matheus.consultaclima.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosClimaAtual(

        @JsonAlias("temperature_2m")
        Double temperatura,

        @JsonAlias("apparent_temperature")
        Double sensacaoTermica,

        @JsonAlias("relative_humidity_2m")
        Integer umidade,

        @JsonAlias("wind_speed_10m")
        Double velocidadeVento,

        @JsonAlias("weather_code")
        Integer codigoClima
) {
}