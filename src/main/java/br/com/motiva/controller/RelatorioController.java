package br.com.motiva.controller;

import br.com.motiva.dto.RelatorioResponse;
import br.com.motiva.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    /** Gera o relatório a partir dos trechos atuais e persiste no histórico. */
    @PostMapping
    public ResponseEntity<RelatorioResponse> gerar() {
        RelatorioResponse gerado = RelatorioResponse.from(service.gerar());
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(gerado.id()).toUri();
        return ResponseEntity.created(location).body(gerado);
    }

    @GetMapping
    public List<RelatorioResponse> historico() {
        return service.historico().stream().map(RelatorioResponse::from).toList();
    }

    @GetMapping("/periodo")
    public List<RelatorioResponse> porPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.porPeriodo(inicio, fim).stream().map(RelatorioResponse::from).toList();
    }
}
