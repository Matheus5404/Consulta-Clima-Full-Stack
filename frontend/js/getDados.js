const URL_BASE = "http://localhost:8080";

function getDados(endpoint) {

    return fetch(`${URL_BASE}${endpoint}`)
        .then(response => {

            if (!response.ok) {
                throw new Error("Erro ao buscar dados.");
            }

            return response.json();

        });

}

export default getDados;