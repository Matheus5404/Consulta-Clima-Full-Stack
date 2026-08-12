# Consulta de Clima

Aplicação back-end desenvolvida em Java com Spring Boot para consultar informações climáticas atuais de cidades utilizando a API Open-Meteo.

## Funcionalidades

- Buscar uma cidade pelo nome
- Exibir diferentes localidades encontradas
- Consultar temperatura atual
- Consultar sensação térmica
- Consultar umidade do ar
- Consultar velocidade do vento
- Exibir a condição climática
- Traduzir códigos climáticos para português

## Tecnologias utilizadas

- Java
- Spring Boot
- Maven
- Jackson
- HttpClient
- API REST
- Records
- Streams e Lambdas
- IntelliJ IDEA

## API utilizada

O projeto utiliza duas APIs da Open-Meteo:

- API de geocodificação para localizar cidades
- API de previsão do tempo para consultar os dados climáticos

## Como funciona

1. O usuário digita o nome de uma cidade.
2. A aplicação consulta a API de geocodificação.
3. O sistema exibe as localidades encontradas.
4. O usuário escolhe uma localização.
5. A aplicação utiliza latitude e longitude para consultar o clima.
6. Os dados são exibidos no terminal.

## Exemplo de execução

```text
============================
     CONSULTA DE CLIMA
============================

Digite o nome da cidade: Sorocaba

Cidades encontradas:
1 - Sorocaba, São Paulo, Brasil

Escolha uma cidade: 1

============================
       CLIMA ATUAL
============================

Localização: Sorocaba, São Paulo, Brasil
Temperatura: 24.5 °C
Sensação térmica: 25.1 °C
Umidade: 70%
Velocidade do vento: 9.2 km/h
Condição climática: Parcialmente nublado
```

## Estrutura do projeto
```text
src/main/java/br/com/matheus/consultaclima
│
├── model
│   ├── DadosClimaAtual.java
│   ├── DadosLocalizacao.java
│   ├── ResultadoClima.java
│   └── ResultadoLocalizacoes.java
│
├── principal
│   └── Principal.java
│
├── service
│   ├── ConsumoApi.java
│   ├── ConverteDados.java
│   ├── IConverteDados.java
│   └── TradutorCodigoClima.java
│
└── ConsultaClimaApplication.java