package br.com.matheus.consultaclima.service;

import br.com.matheus.consultaclima.dto.CidadeConsultadaDTO;
import br.com.matheus.consultaclima.dto.ConsultaClimaDTO;
import br.com.matheus.consultaclima.model.ConsultaClima;
import br.com.matheus.consultaclima.repository.ConsultaClimaRepository;
import br.com.matheus.consultaclima.dto.LocalizacaoDTO;
import br.com.matheus.consultaclima.model.DadosClimaAtual;
import br.com.matheus.consultaclima.model.ResultadoClima;
import br.com.matheus.consultaclima.model.DadosLocalizacao;
import br.com.matheus.consultaclima.model.ResultadoLocalizacoes;
import org.springframework.stereotype.Service;
import br.com.matheus.consultaclima.dto.ClimaDTO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ClimaService {

    private final ConsultaClimaRepository repository;
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private ConsultaClimaDTO converterParaDTO(ConsultaClima consulta) {
        return new ConsultaClimaDTO(
                consulta.getId(),
                consulta.getCidade(),
                consulta.getEstado(),
                consulta.getPais(),
                consulta.getTemperatura(),
                consulta.getSensacaoTermica(),
                consulta.getUmidade(),
                consulta.getVelocidadeVento(),
                consulta.getCondicaoClimatica(),
                consulta.getDataConsulta()
        );
    }

    public ClimaService(ConsultaClimaRepository repository) {
        this.repository = repository;
    }

    private static final String ENDERECO_GEOCODIFICACAO =
            "https://geocoding-api.open-meteo.com/v1/search";

    public List<LocalizacaoDTO> buscarCidades(String cidade) {

        String cidadeCodificada = URLEncoder.encode(
                cidade,
                StandardCharsets.UTF_8
        );

        String endereco =
                ENDERECO_GEOCODIFICACAO
                        + "?name=" + cidadeCodificada
                        + "&count=10"
                        + "&language=pt"
                        + "&format=json";

        String json = consumo.obterDados(endereco);

        ResultadoLocalizacoes resposta =
                conversor.obterDados(
                        json,
                        ResultadoLocalizacoes.class
                );

        if (resposta.resultados() == null) {
            return List.of();
        }

        return resposta.resultados()
                .stream()
                .map(localizacao -> new LocalizacaoDTO(
                        localizacao.nome(),
                        localizacao.latitude(),
                        localizacao.longitude(),
                        localizacao.pais(),
                        localizacao.estado(),
                        localizacao.fusoHorario()
                ))
                .toList();
    }

    private static final String ENDERECO_CLIMA = "https://api.open-meteo.com/v1/forecast";

    public ClimaDTO buscarClima(
            String cidade,
            String estado,
            String pais,
            Double latitude,
            Double longitude
    ) {

        String endereco =
                ENDERECO_CLIMA
                        + "?latitude=" + latitude
                        + "&longitude=" + longitude
                        + "&current="
                        + "temperature_2m,"
                        + "apparent_temperature,"
                        + "relative_humidity_2m,"
                        + "wind_speed_10m,"
                        + "weather_code"
                        + "&timezone=auto";

        String json = consumo.obterDados(endereco);

        ResultadoClima resultado =
                conversor.obterDados(
                        json,
                        ResultadoClima.class
                );

        DadosClimaAtual clima = resultado.climaAtual();

        String condicao =
                TradutorCodigoClima.traduzir(
                        clima.codigoClima()
                );

        DadosLocalizacao localizacao =
                new DadosLocalizacao(
                        cidade,
                        latitude,
                        longitude,
                        pais,
                        estado,
                        null
                );

        ConsultaClima consulta =
                new ConsultaClima(
                        localizacao,
                        clima,
                        condicao
                );

        repository.save(consulta);

        return new ClimaDTO(
                clima.temperatura(),
                clima.sensacaoTermica(),
                clima.umidade(),
                clima.velocidadeVento(),
                condicao
        );
    }

    public List<ConsultaClimaDTO> listarHistorico() {

        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ConsultaClimaDTO> buscarHistoricoPorCidade(String cidade) {
        return repository
                .findByCidadeContainingIgnoreCase(cidade)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ConsultaClimaDTO> listarUltimasConsultas() {
        return repository
                .findTop5ByOrderByDataConsultaDesc()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ConsultaClimaDTO> buscarPorTemperaturaMinima(Double temperatura) {
        return repository
                .findByTemperaturaGreaterThanEqual(temperatura)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<CidadeConsultadaDTO> listarCidadesMaisConsultadas() {

        return repository.buscarCidadesMaisConsultadas()
                .stream()
                .map(resultado -> new CidadeConsultadaDTO(
                        (String) resultado[0],
                        (Long) resultado[1]
                ))
                .toList();
    }
}