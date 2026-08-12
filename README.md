# 🌦️ Consulta de Clima - Full Stack

Aplicação Full Stack desenvolvida para consultar informações climáticas de cidades utilizando uma API externa.

O projeto possui um backend desenvolvido com **Java e Spring Boot** e um frontend desenvolvido com **HTML, CSS, Bootstrap e JavaScript**.

Além da consulta do clima atual, a aplicação armazena as consultas realizadas em um banco de dados **PostgreSQL**, permitindo visualizar histórico, últimas consultas e cidades mais pesquisadas.

---

## 🚀 Tecnologias utilizadas

### Backend

- Java
- Spring Boot
- Spring Data JPA
- API REST
- PostgreSQL
- Maven
- DTO
- Repository
- Service
- CORS
- Consumo de API externa

### Frontend

- HTML5
- CSS3
- Bootstrap
- JavaScript
- Fetch API
- JavaScript Modules

---

## 📁 Estrutura do projeto

```text
Consulta-Clima-Full-Stack
│
├── backend
│   ├── src
│   └── pom.xml
│
├── frontend
│   ├── index.html
│   │
│   ├── css
│   │   └── style.css
│   │
│   └── js
│       ├── getDados.js
│       ├── historico.js
│       ├── index.js
│       └── ui.js
│
├── .gitignore
└── README.md
```

---

## ⚙️ Funcionamento

O frontend realiza requisições para a API REST desenvolvida com Spring Boot.

O fluxo principal da aplicação é:

```text
Frontend
   ↓
JavaScript / Fetch
   ↓
API REST
   ↓
Controller
   ↓
Service
   ↓
API externa / Repository
   ↓
PostgreSQL
```

---

## 🌤️ Funcionalidades

- Pesquisa de cidades
- Seleção da localização encontrada
- Consulta do clima atual
- Temperatura atual
- Sensação térmica
- Umidade
- Velocidade do vento
- Condição climática
- Ícones de acordo com a condição do clima
- Histórico de consultas
- Últimas consultas realizadas
- Ranking das cidades mais pesquisadas
- Top 9 cidades mais pesquisadas
- Filtro do histórico por cidade
- Filtro por temperatura mínima
- Tratamento de erros no frontend
- Indicadores de carregamento

---

## 🗄️ Banco de dados

O projeto utiliza **PostgreSQL** para armazenar as consultas climáticas realizadas.

As informações armazenadas permitem gerar:

- Histórico completo
- Últimas consultas
- Ranking das cidades mais pesquisadas
- Filtros por cidade
- Filtros por temperatura

---

## 🔗 API externa

A aplicação consome serviços externos para obter informações de localização e clima.

O backend é responsável por realizar essas consultas e disponibilizar os dados ao frontend através da API REST.

---

## 📚 Objetivo do projeto

Este projeto foi desenvolvido com o objetivo de colocar em prática conhecimentos de desenvolvimento Full Stack, principalmente:

- Desenvolvimento de APIs REST com Spring Boot
- Arquitetura em camadas
- DTO
- Service
- Repository
- Spring Data JPA
- PostgreSQL
- Consumo de APIs externas
- CORS
- Integração entre frontend e backend
- JavaScript
- Fetch API
- Bootstrap
- Git e GitHub

---

## 👨‍💻 Autor

**Matheus Duarte**

Projeto desenvolvido para estudo e prática de desenvolvimento Full Stack.
