const areaMensagem = document.getElementById("mensagem");

const mensagemConteudo = document.getElementById("mensagem-conteudo");

const botaoBuscar = document.getElementById("botao-buscar");

const resultadoClima = document.getElementById("resultado-clima");

const carregandoClima = document.getElementById("carregando-clima");


export function mostrarMensagem(texto, tipo) {

    mensagemConteudo.textContent = texto;

    mensagemConteudo.className = `alert alert-${tipo}`;

    areaMensagem.classList.remove("d-none");
}


export function esconderMensagem() {

    areaMensagem.classList.add("d-none");
}


export function ativarCarregamento() {

    botaoBuscar.disabled = true;

    botaoBuscar.textContent = "Buscando...";
}


export function desativarCarregamento() {

    botaoBuscar.disabled = false;

    botaoBuscar.textContent = "Buscar";
}


export function ativarCarregamentoClima() {

    resultadoClima.classList.remove("d-none");

    carregandoClima.classList.remove("d-none");
}


export function desativarCarregamentoClima() {

    carregandoClima.classList.add("d-none");
}