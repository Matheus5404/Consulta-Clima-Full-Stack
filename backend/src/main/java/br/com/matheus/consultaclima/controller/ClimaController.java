package br.com.matheus.consultaclima.controller;

import br.com.matheus.consultaclima.dto.CidadeConsultadaDTO;
import br.com.matheus.consultaclima.dto.ConsultaClimaDTO;
import br.com.matheus.consultaclima.dto.ClimaDTO;
import br.com.matheus.consultaclima.dto.LocalizacaoDTO;
import br.com.matheus.consultaclima.service.ClimaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clima")
public class ClimaController {

    private final ClimaService servico;

    public ClimaController(ClimaService servico) {
        this.servico = servico;
    }

    @GetMapping("/cidades")
    public List<LocalizacaoDTO> buscarCidades(
            @RequestParam String nome
    ) {
        return servico.buscarCidades(nome);
    }

    @GetMapping("/atual")
    public ClimaDTO buscarClima(
            @RequestParam String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam String pais,
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ) {

        return servico.buscarClima(
                cidade,
                estado,
                pais,
                latitude,
                longitude
        );
    }

    @GetMapping("/historico")
    public List<ConsultaClimaDTO> listarHistorico() {
        return servico.listarHistorico();
    }

    @GetMapping("/historico/cidade")
    public List<ConsultaClimaDTO> buscarHistoricoPorCidade(@RequestParam String nome) {
        return servico.buscarHistoricoPorCidade(nome);
    }

    @GetMapping("/ultimas")
    public List<ConsultaClimaDTO> listarUltimasConsultas() {
        return servico.listarUltimasConsultas();
    }

    @GetMapping("/temperatura")
    public List<ConsultaClimaDTO> buscarPorTemperaturaMinima(@RequestParam Double minima) {
        return servico.buscarPorTemperaturaMinima(minima);
    }

    @GetMapping("/mais-consultadas")
    public List<CidadeConsultadaDTO> listarCidadesMaisConsultadas() {
        return servico.listarCidadesMaisConsultadas();
    }
}