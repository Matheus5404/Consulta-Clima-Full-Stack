import getDados from "./getDados.js";

import {mostrarMensagem, esconderMensagem} from "./ui.js";

const botaoVerMaisCidades = document.getElementById("botao-ver-mais-cidades");
const filtroHistorico = document.getElementById("filtro-historico");
const botaoFiltrarHistorico = document.getElementById("botao-filtrar-historico");
const botaoLimparFiltro = document.getElementById("botao-limpar-filtro");
const filtroTemperatura = document.getElementById("filtro-temperatura");
const botaoFiltrarTemperatura = document.getElementById("botao-filtrar-temperatura");
const botaoHistorico = document.getElementById("botao-historico");
const historicoCompleto = document.getElementById("historico-completo");
const cidadesMaisConsultadas = document.getElementById("cidades-mais-consultadas");
const ultimasConsultas = document.getElementById("ultimas-consultas");

let todasCidades = [];
let mostrarTodasCidades = false;

function formatarData(data) {

    const dataConsulta = new Date(data);

    return dataConsulta.toLocaleString(
        "pt-BR",
        {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }
    );
}


function carregarUltimasConsultas() {

    getDados("/clima/ultimas")
        .then(consultas => {

            ultimasConsultas.innerHTML = "";

            if (consultas.length === 0) {

                ultimasConsultas.innerHTML = `
                    <div class="col-12">

                        <div class="alert alert-secondary text-center">

                            Nenhuma consulta realizada ainda.

                        </div>

                    </div>
                `;

                return;
            }

            consultas.forEach(consulta => {

                const coluna = document.createElement("div");

                coluna.className = "col-md-6 col-lg-4";

                coluna.innerHTML = `
                    <div class="card h-100 shadow-sm">

                        <div class="card-body">

                            <div class="d-flex justify-content-between align-items-start">

                                <div>

                                    <h5 class="card-title mb-1">
                                        ${consulta.cidade}
                                    </h5>

                                    <p class="text-secondary mb-3">
                                        ${consulta.estado ?? "Não informado"} - ${consulta.pais}
                                    </p>

                                </div>

                                <span class="badge text-bg-primary">
                                    ${consulta.temperatura}°C
                                </span>

                            </div>

                            <p class="mb-2">
                                <strong>Condição:</strong>
                                ${consulta.condicaoClimatica}
                            </p>

                            <p class="mb-2">
                                <strong>Umidade:</strong>
                                ${consulta.umidade}%
                            </p>

                            <p class="mb-3">
                                <strong>Vento:</strong>
                                ${consulta.velocidadeVento} km/h
                            </p>

                            <small class="text-secondary">
                                Consultado em:
                                ${formatarData(consulta.dataConsulta)}
                            </small>

                        </div>

                    </div>
                `;

                ultimasConsultas.appendChild(coluna);
            });

        })
        .catch(erro => {

            console.error("Erro ao carregar últimas consultas:", erro);

            ultimasConsultas.innerHTML = `
                <div class="col-12">

                    <div class="alert alert-danger text-center">

                        Não foi possível carregar as últimas consultas.

                    </div>

                </div>
            `;

        });
}

function carregarCidadesMaisConsultadas() {

    getDados("/clima/mais-consultadas")
        .then(cidades => {

            todasCidades = cidades;

            if (cidades.length === 0) {

                cidadesMaisConsultadas.innerHTML = `
                    <div class="col-12">

                        <div class="alert alert-secondary text-center">

                            Ainda não existem cidades pesquisadas.

                        </div>

                    </div>
                `;

                botaoVerMaisCidades.classList.add("d-none");
                return;
            }

            exibirCidadesMaisConsultadas();

            if (cidades.length > 9) {
                botaoVerMaisCidades.classList.remove("d-none");
            } else {
                botaoVerMaisCidades.classList.add("d-none");
            }

        })
        .catch(erro => {

            console.error(
                "Erro ao carregar cidades mais consultadas:",
                erro
            );

            cidadesMaisConsultadas.innerHTML = `
                <div class="col-12">

                    <div class="alert alert-danger text-center">

                        Não foi possível carregar o ranking de cidades.

                    </div>

                </div>
            `;

        });
}

function exibirHistorico(consultas) {

    historicoCompleto.innerHTML = "";

    if (consultas.length === 0) {

        historicoCompleto.innerHTML = `
            <div class="col-12">

                <div class="alert alert-secondary text-center">

                    Nenhuma consulta encontrada.

                </div>

            </div>
        `;

        return;
    }

    consultas.forEach(consulta => {

        const coluna = document.createElement("div");

        coluna.className = "col-md-6 col-lg-4";

        coluna.innerHTML = `
            <div class="card h-100 shadow-sm">

                <div class="card-body">

                    <h5 class="card-title">
                        ${consulta.cidade}
                    </h5>

                    <p class="text-secondary">

                        ${consulta.estado ?? "Não informado"}
                        -
                        ${consulta.pais}

                    </p>

                    <h3>
                        ${consulta.temperatura}°C
                    </h3>

                    <p>
                        <strong>Condição:</strong>
                        ${consulta.condicaoClimatica}
                    </p>

                    <p>
                        <strong>Sensação:</strong>
                        ${consulta.sensacaoTermica}°C
                    </p>

                    <p>
                        <strong>Umidade:</strong>
                        ${consulta.umidade}%
                    </p>

                    <p>
                        <strong>Vento:</strong>
                        ${consulta.velocidadeVento} km/h
                    </p>

                    <small class="text-secondary">

                        ${formatarData(
                            consulta.dataConsulta
                        )}

                    </small>

                </div>

            </div>
        `;

        historicoCompleto.appendChild(coluna);
    });
}


function carregarHistoricoCompleto() {

    getDados("/clima/historico")
        .then(consultas => {

            exibirHistorico(consultas);
        })
        .catch(erro => {

            console.error("Erro ao carregar histórico:",erro);

            historicoCompleto.innerHTML = `
                <div class="col-12">

                    <div class="alert alert-danger text-center">

                        Não foi possível carregar o histórico.

                    </div>

                </div>
            `;

        });
}


function filtrarHistorico() {

    const cidade = filtroHistorico.value.trim();

    if (cidade === "") {

        mostrarMensagem("Digite uma cidade para filtrar o histórico.", "warning");

        return;
    }

    getDados(
        `/clima/historico/cidade?nome=${encodeURIComponent(cidade)}`
    )
        .then(consultas => {

            esconderMensagem();

            exibirHistorico(consultas);

            historicoCompleto.classList.remove("d-none");

            botaoHistorico.textContent = "Ocultar histórico";
        })
        .catch(erro => {

            console.error("Erro ao filtrar histórico:", erro);

            mostrarMensagem("Não foi possível filtrar o histórico.", "danger"
            );
        });
}


function filtrarPorTemperatura() {

    const minima = filtroTemperatura.value.trim();

    if (minima === "") {
        mostrarMensagem("Digite uma temperatura mínima.", "warning");
        return;
    }

    getDados(
        `/clima/temperatura?minima=${encodeURIComponent(minima)}`
    )
        .then(consultas => {

            esconderMensagem();

            exibirHistorico(consultas);

            historicoCompleto.classList.remove("d-none");

            botaoHistorico.textContent = "Ocultar histórico";

        })
        .catch(erro => {

            console.error("Erro ao filtrar por temperatura:",erro);

            mostrarMensagem("Não foi possível filtrar por temperatura.","danger");
        });
}


/* =========================
   EVENTOS
========================= */


botaoHistorico.addEventListener(
    "click",
    function () {

        const estaEscondido = historicoCompleto
                .classList
                .contains("d-none");

        if (estaEscondido) {

            carregarHistoricoCompleto();

            historicoCompleto.classList.remove("d-none");

            botaoHistorico.textContent = "Ocultar histórico";

        } else {

            historicoCompleto.classList.add("d-none");

            botaoHistorico.textContent = "Ver histórico";

        }

    }
);


botaoFiltrarHistorico.addEventListener("click",filtrarHistorico);


filtroHistorico.addEventListener("keydown",
    function (evento) {

        if (evento.key === "Enter") {

            filtrarHistorico();

        }

    }
);


botaoFiltrarTemperatura.addEventListener("click", filtrarPorTemperatura);


filtroTemperatura.addEventListener("keydown",
    function (evento) {

        if (evento.key === "Enter") {

            filtrarPorTemperatura();

        }

    }
);


botaoLimparFiltro.addEventListener("click",
    function () {

        filtroHistorico.value = "";

        filtroTemperatura.value = "";

        esconderMensagem();

        carregarHistoricoCompleto();

    }
);

function exibirCidadesMaisConsultadas() {

    cidadesMaisConsultadas.innerHTML = "";

    const cidadesParaExibir = mostrarTodasCidades
            ? todasCidades
            : todasCidades.slice(0, 9);

    cidadesParaExibir.forEach(
        (cidade, indice) => {

            const coluna = document.createElement("div");

            coluna.className = "col-md-6 col-lg-4";

            coluna.innerHTML = `
                <div class="card h-100 shadow-sm">

                    <div class="card-body">

                        <div class="d-flex justify-content-between align-items-center">

                            <div>

                                <h5 class="mb-1">
                                    ${indice + 1}º - ${cidade.cidade}
                                </h5>

                                <p class="text-secondary mb-0">
                                    ${cidade.quantidade} consulta(s)
                                </p>

                            </div>

                            <span class="badge text-bg-primary">
                                ${cidade.quantidade}
                            </span>

                        </div>

                    </div>

                </div>
            `;

            cidadesMaisConsultadas.appendChild(coluna);

        }
    );
}

botaoVerMaisCidades.addEventListener("click",
    function () {
        mostrarTodasCidades = !mostrarTodasCidades;
        exibirCidadesMaisConsultadas();

        if (mostrarTodasCidades) {
            botaoVerMaisCidades.textContent = "Mostrar somente Top 9";
        } else {
            botaoVerMaisCidades.textContent = "Ver histórico de cidades";
        }
    }
);

/* =========================
   EXPORTAÇÕES
========================= */

export {
    carregarUltimasConsultas,
    carregarCidadesMaisConsultadas
};