package br.com.matheus.consultaclima.principal;

import br.com.matheus.consultaclima.model.ConsultaClima;
import br.com.matheus.consultaclima.model.DadosClimaAtual;
import br.com.matheus.consultaclima.model.DadosLocalizacao;
import br.com.matheus.consultaclima.model.ResultadoClima;
import br.com.matheus.consultaclima.model.ResultadoLocalizacoes;
import br.com.matheus.consultaclima.repository.ConsultaClimaRepository;
import br.com.matheus.consultaclima.service.ConsumoApi;
import br.com.matheus.consultaclima.service.ConverteDados;
import br.com.matheus.consultaclima.service.TradutorCodigoClima;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private final ConsultaClimaRepository repository;

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private static final String ENDERECO_GEOCODIFICACAO =
            "https://geocoding-api.open-meteo.com/v1/search";

    private static final String ENDERECO_CLIMA =
            "https://api.open-meteo.com/v1/forecast";

    public Principal(ConsultaClimaRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("""
                    
                    ============================
                         CONSULTA DE CLIMA
                    ============================
                    
                    1 - Consultar clima atual
                    2 - Listar histórico
                    3 - Buscar histórico por cidade
                    4 - Listar últimas 5 consultas
                    5 - Buscar por temperatura mínima
                    6 - Listar cidades mais consultadas
                    0 - Sair
                    """);

            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(leitura.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nDigite apenas números.");
                continue;
            }

            switch (opcao) {
                case 1 -> consultarClima();
                case 2 -> listarHistorico();
                case 3 -> buscarHistoricoPorCidade();
                case 4 -> listarUltimasConsultas();
                case 5 -> buscarPorTemperaturaMinima();
                case 6 -> listarCidadesMaisConsultadas();
                case 0 -> System.out.println("\nAplicação encerrada!");
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }

    private void consultarClima() {

        System.out.print("\nDigite o nome da cidade: ");

        String cidadeDigitada = leitura.nextLine().trim();

        if (cidadeDigitada.isBlank()) {
            System.out.println("O nome da cidade não pode ficar vazio.");
            return;
        }

        String cidadeCodificada =
                URLEncoder.encode(
                        cidadeDigitada,
                        StandardCharsets.UTF_8
                );

        String enderecoLocalizacao =
                ENDERECO_GEOCODIFICACAO
                        + "?name=" + cidadeCodificada
                        + "&count=10"
                        + "&language=pt"
                        + "&format=json";

        String jsonLocalizacao;

        try {
            jsonLocalizacao = consumo.obterDados(enderecoLocalizacao);
        } catch (RuntimeException e) {
            System.out.println(
                    "Não foi possível consultar as cidades.");
            return;
        }
        ResultadoLocalizacoes resposta;

        try {
            resposta = conversor.obterDados(
                    jsonLocalizacao,
                    ResultadoLocalizacoes.class
            );
        } catch (RuntimeException e) {
            System.out.println(
                    "Erro ao processar os dados de localização.");
            return;
        }

        if (resposta.resultados() == null
                || resposta.resultados().isEmpty()) {

            System.out.println("Nenhuma cidade encontrada.");
            return;
        }

        List<DadosLocalizacao> cidades =
                resposta.resultados();

        System.out.println("\nCidades encontradas:");

        for (int i = 0; i < cidades.size(); i++) {

            DadosLocalizacao cidade =
                    cidades.get(i);

            System.out.printf(
                    "%d - %s, %s, %s%n",
                    i + 1,
                    cidade.nome(),
                    cidade.estado() != null
                            ? cidade.estado()
                            : "Estado não informado",
                    cidade.pais()
            );
        }

        System.out.print(
                "\nEscolha uma cidade: "
        );

        int opcaoCidade;

        try {
            opcaoCidade = Integer.parseInt(
                    leitura.nextLine().trim()
            );
        } catch (NumberFormatException e) {
            System.out.println(
                    "Digite apenas o número da cidade."
            );
            return;
        }

        if (opcaoCidade < 1
                || opcaoCidade > cidades.size()) {

            System.out.println("Opção inválida.");
            return;
        }

        DadosLocalizacao cidadeSelecionada =
                cidades.get(opcaoCidade - 1);

        String enderecoClima =
                ENDERECO_CLIMA
                        + "?latitude="
                        + cidadeSelecionada.latitude()
                        + "&longitude="
                        + cidadeSelecionada.longitude()
                        + "&current="
                        + "temperature_2m,"
                        + "apparent_temperature,"
                        + "relative_humidity_2m,"
                        + "wind_speed_10m,"
                        + "weather_code"
                        + "&timezone=auto";

        String jsonClima;

        try {
            jsonClima =
                    consumo.obterDados(enderecoClima);
        } catch (RuntimeException e) {
            System.out.println(
                    "Não foi possível consultar o clima."
            );
            return;
        }

        ResultadoClima resultadoClima;

        try {
            resultadoClima =
                    conversor.obterDados(
                            jsonClima,
                            ResultadoClima.class
                    );
        } catch (RuntimeException e) {
            System.out.println(
                    "Erro ao processar os dados climáticos."
            );
            return;
        }

        if (resultadoClima.climaAtual() == null) {
            System.out.println(
                    "Não foi possível obter os dados climáticos."
            );
            return;
        }

        DadosClimaAtual clima =
                resultadoClima.climaAtual();

        String condicaoClimatica =
                TradutorCodigoClima.traduzir(
                        clima.codigoClima()
                );

        exibirClima(
                cidadeSelecionada,
                clima,
                condicaoClimatica
        );

        ConsultaClima consulta =
                new ConsultaClima(
                        cidadeSelecionada,
                        clima,
                        condicaoClimatica
                );

        repository.save(consulta);

        System.out.println(
                "\nConsulta salva no banco de dados com sucesso!"
        );
    }

    private void exibirClima(
            DadosLocalizacao cidade,
            DadosClimaAtual clima,
            String condicaoClimatica
    ) {

        System.out.println("""
                
                ============================
                      CLIMA ATUAL
                ============================
                """);

        System.out.printf(
                "Localização: %s, %s, %s%n",
                cidade.nome(),
                cidade.estado() != null
                        ? cidade.estado()
                        : "Estado não informado",
                cidade.pais()
        );

        System.out.printf("Temperatura: %.1f °C%n",
                clima.temperatura());

        System.out.printf("Sensação térmica: %.1f °C%n",
                clima.sensacaoTermica());

        System.out.printf("Umidade: %d%%%n", clima.umidade());

        System.out.printf("Velocidade do vento: %.1f km/h%n",
                clima.velocidadeVento());

        System.out.println("Condição climática: " + condicaoClimatica);
    }

    private void listarHistorico() {

        List<ConsultaClima> consultas = repository.findAll();

        if (consultas.isEmpty()) {
            System.out.println("\nNenhuma consulta foi salva.");
            return;
        }

        System.out.println("\n===== HISTÓRICO DE CONSULTAS =====");

        consultas.forEach(System.out::println);
    }

    private void buscarHistoricoPorCidade() {

        System.out.print("\nDigite o nome da cidade: ");

        String cidade =
                leitura.nextLine().trim();

        if (cidade.isBlank()) {
            System.out.println("O nome da cidade não pode ficar vazio.");
            return;
        }

        List<ConsultaClima> consultas =
                repository.findByCidadeContainingIgnoreCase(cidade);

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada para essa cidade.");
            return;
        }

        System.out.println("\n===== CONSULTAS ENCONTRADAS =====");

        consultas.forEach(System.out::println
        );
    }

    private void listarUltimasConsultas() {

        List<ConsultaClima> consultas = repository.findTop5ByOrderByDataConsultaDesc();

        if (consultas.isEmpty()) {
            System.out.println("\nNenhuma consulta foi salva.");
            return;
        }

        System.out.println("\n===== ÚLTIMAS 5 CONSULTAS =====");

        consultas.forEach(System.out::println);
    }

    private void buscarPorTemperaturaMinima() {

        System.out.print(
                "\nInforme a temperatura mínima: "
        );

        double temperatura;

        try {
            temperatura = Double.parseDouble(
                    leitura.nextLine()
                            .trim()
                            .replace(",", ".")
            );
        } catch (NumberFormatException e) {
            System.out.println("Temperatura inválida.");
            return;
        }

        List<ConsultaClima> consultas = repository
                .findByTemperaturaGreaterThanEqual(temperatura);

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada.");
            return;
        }

        System.out.println("\n===== CONSULTAS ENCONTRADAS =====");

        consultas.forEach(System.out::println);
    }

    private void listarCidadesMaisConsultadas() {

        List<Object[]> resultados =
                repository
                        .buscarCidadesMaisConsultadas();

        if (resultados.isEmpty()) {
            System.out.println(
                    "\nNenhuma consulta foi salva."
            );
            return;
        }

        System.out.println(
                "\n===== CIDADES MAIS CONSULTADAS ====="
        );

        resultados.forEach(resultado -> {

            String cidade =
                    (String) resultado[0];

            Long quantidade =
                    (Long) resultado[1];

            System.out.printf(
                    "%s - %d consulta(s)%n",
                    cidade,
                    quantidade
            );
        });
    }
}