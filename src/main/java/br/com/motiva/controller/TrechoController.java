package br.com.motiva.controller;

import br.com.motiva.dto.TrechoRequest;
import br.com.motiva.dto.TrechoResponse;
import br.com.motiva.service.TrechoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/trechos")
public class TrechoController {

    private final TrechoService service;

    public TrechoController(TrechoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TrechoResponse> listar(@RequestParam(required = false) String tipo,
                                       @RequestParam(required = false) Double nivelMinimo) {
        return service.listar(tipo, nivelMinimo).stream().map(TrechoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TrechoResponse buscar(@PathVariable Long id) {
        return TrechoResponse.from(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<TrechoResponse> criar(@Valid @RequestBody TrechoRequest req) {
        TrechoResponse criado = TrechoResponse.from(service.criar(req));
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public TrechoResponse atualizar(@PathVariable Long id, @Valid @RequestBody TrechoRequest req) {
        return TrechoResponse.from(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
