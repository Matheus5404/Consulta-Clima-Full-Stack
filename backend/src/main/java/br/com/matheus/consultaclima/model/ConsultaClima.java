package br.com.matheus.consultaclima.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas_clima")
public class ConsultaClima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cidade;
    private String estado;
    private String pais;

    private Double latitude;
    private Double longitude;

    private Double temperatura;

    @Column(name = "sensacao_termica")
    private Double sensacaoTermica;

    private Integer umidade;

    @Column(name = "velocidade_vento")
    private Double velocidadeVento;

    @Column(name = "condicao_climatica")
    private String condicaoClimatica;

    @Column(name = "data_consulta")
    private LocalDateTime dataConsulta;

    public ConsultaClima() {
    }

    public ConsultaClima(
            DadosLocalizacao localizacao,
            DadosClimaAtual clima,
            String condicaoClimatica
    ) {
        this.cidade = localizacao.nome();
        this.estado = localizacao.estado();
        this.pais = localizacao.pais();
        this.latitude = localizacao.latitude();
        this.longitude = localizacao.longitude();

        this.temperatura = clima.temperatura();
        this.sensacaoTermica = clima.sensacaoTermica();
        this.umidade = clima.umidade();
        this.velocidadeVento = clima.velocidadeVento();

        this.condicaoClimatica = condicaoClimatica;
        this.dataConsulta = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getPais() {
        return pais;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public Double getSensacaoTermica() {
        return sensacaoTermica;
    }

    public Integer getUmidade() {
        return umidade;
    }

    public Double getVelocidadeVento() {
        return velocidadeVento;
    }

    public String getCondicaoClimatica() {
        return condicaoClimatica;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    @Override
    public String toString() {
        return "\nConsulta de clima" +
                "\nCidade: " + cidade +
                "\nEstado: " + (estado != null ? estado : "Não informado") +
                "\nPaís: " + pais +
                "\nTemperatura: " + temperatura + " °C" +
                "\nSensação térmica: " + sensacaoTermica + " °C" +
                "\nUmidade: " + umidade + "%" +
                "\nVelocidade do vento: " + velocidadeVento + " km/h" +
                "\nCondição climática: " + condicaoClimatica +
                "\nData da consulta: " + dataConsulta;
    }
}