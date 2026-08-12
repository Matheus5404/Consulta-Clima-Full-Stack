import getDados from "./getDados.js";

import {
    mostrarMensagem,
    esconderMensagem,
    ativarCarregamento,
    desativarCarregamento,
    ativarCarregamentoClima,
    desativarCarregamentoClima
} from "./ui.js";

import {
    carregarUltimasConsultas,
    carregarCidadesMaisConsultadas
} from "./historico.js";

const iconeClima = document.getElementById("icone-clima");
const resultadoClima = document.getElementById("resultado-clima");
const campoCidade = document.getElementById("cidade");
const botaoBuscar = document.getElementById("botao-buscar");
const areaCidades = document.getElementById("area-cidades");
const listaCidades = document.getElementById("lista-cidades");
const nomeCidade = document.getElementById("nome-cidade");
const localizacaoTexto = document.getElementById("localizacao");
const temperatura = document.getElementById("temperatura");
const condicao = document.getElementById("condicao");
const sensacaoTermica = document.getElementById("sensacao-termica");
const umidade = document.getElementById("umidade");
const vento =document.getElementById("vento");

function buscarCidades() {

    const cidade = campoCidade.value.trim();

    if (cidade === "") {

        mostrarMensagem("Digite o nome de uma cidade.","warning");

        return;
    }

    ativarCarregamento();

    getDados(
        `/clima/cidades?nome=${encodeURIComponent(cidade)}`
    )
        .then(dados => {

            if (dados.length === 0) {

                mostrarMensagem("Nenhuma cidade encontrada.","warning");

                areaCidades.classList.add("d-none");

                resultadoClima.classList.add("d-none");

                return;
            }

            esconderMensagem();

            listaCidades.innerHTML = "";

            const opcaoInicial = document.createElement("option");

            opcaoInicial.textContent = "Selecione uma localização";

            opcaoInicial.value = "";

            listaCidades.appendChild(opcaoInicial);

            dados.forEach(localizacao => {

                const opcao = document.createElement("option");

                opcao.textContent =
                    `${localizacao.nome} - `
                    + `${localizacao.estado ?? "Não informado"} - `
                    + `${localizacao.pais}`;

                opcao.value = JSON.stringify(localizacao);

                listaCidades.appendChild(opcao);

            });

            areaCidades.classList.remove("d-none");

        })
        .catch(erro => {

            console.error(erro);

            mostrarMensagem("Não foi possível consultar o servidor.", "danger");

        })
        .finally(() => {

            desativarCarregamento();

        });
}

function buscarClima(localizacao) {

    ativarCarregamentoClima();

    const endpoint =
        `/clima/atual`
        + `?cidade=${encodeURIComponent(localizacao.nome)}`
        + `&estado=${encodeURIComponent(localizacao.estado ?? "")}`
        + `&pais=${encodeURIComponent(localizacao.pais)}`
        + `&latitude=${localizacao.latitude}`
        + `&longitude=${localizacao.longitude}`;

    getDados(endpoint)
        .then(clima => {

            nomeCidade.textContent = localizacao.nome;

            localizacaoTexto.textContent =
                `${localizacao.estado ?? "Não informado"} - ${localizacao.pais}`;

            temperatura.textContent = `${clima.temperatura}°C`;

            condicao.textContent = clima.condicaoClimatica;

            iconeClima.textContent = obterIconeClima(clima.condicaoClimatica);

            aplicarTemaClima(clima.condicaoClimatica);

            sensacaoTermica.textContent = `${clima.sensacaoTermica}°C`;

            umidade.textContent = `${clima.umidade}%`;

            vento.textContent = `${clima.velocidadeVento} km/h`;

            resultadoClima.classList.remove("d-none");

            esconderMensagem();
            carregarUltimasConsultas();
            carregarCidadesMaisConsultadas();
        })
        .catch(erro => {
            console.error(erro);
            mostrarMensagem("Não foi possível consultar o clima.", "danger");
        })
        .finally(() => {
            desativarCarregamentoClima();
        });
}

botaoBuscar.addEventListener("click", buscarCidades);

campoCidade.addEventListener("keydown",
    function (evento) {

        if (evento.key === "Enter") {

            buscarCidades();

        }

    }
);


listaCidades.addEventListener("change",
    function () {

        if (listaCidades.value === "") {
            return;
        }

        const localizacao = JSON.parse(listaCidades.value);

        buscarClima(localizacao);

    }
);

function obterIconeClima(condicaoClimatica) {

    if (!condicaoClimatica) {
        return "🌤️";
    }

    const condicao = condicaoClimatica.toLowerCase();

    if (condicao.includes("tempestade")) {
        return "⛈️";
    }

    if (condicao.includes("chuva") || condicao.includes("garoa")) {
        return "🌧️";
    }

    if (condicao.includes("neve")) {
        return "❄️";
    }

    if (condicao.includes("nublado") || condicao.includes("nuvens")) {
        return "☁️";
    }

    if (condicao.includes("limpo") || condicao.includes("ensolarado")) {
        return "☀️";
    }

    return "🌤️";
}

function aplicarTemaClima(condicaoClimatica) {

    const card = resultadoClima.querySelector(".card");

    card.classList.remove(
        "clima-limpo",
        "clima-nublado",
        "clima-chuva",
        "clima-tempestade",
        "clima-neve"
    );

    const condicao = condicaoClimatica.toLowerCase();

    if (condicao.includes("tempestade")) {
        card.classList.add("clima-tempestade");
        return;
    }

    if (condicao.includes("chuva") || condicao.includes("garoa")) {
        card.classList.add("clima-chuva");
        return;
    }

    if (condicao.includes("neve")) {
        card.classList.add("clima-neve");
        return;
    }

    if (condicao.includes("nublado") || condicao.includes("nuvens")) {
        card.classList.add("clima-nublado");
        return;
    }

    card.classList.add("clima-limpo");
}

carregarUltimasConsultas();
carregarCidadesMaisConsultadas();