package br.com.motiva.controller;

import br.com.motiva.dto.IntervencaoRequest;
import br.com.motiva.dto.IntervencaoResponse;
import br.com.motiva.service.IntervencaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/intervencoes")
public class IntervencaoController {

    private final IntervencaoService service;

    public IntervencaoController(IntervencaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<IntervencaoResponse> listar(@RequestParam(required = false) String status,
                                            @RequestParam(required = false) Long trechoId) {
        return service.listar(status, trechoId).stream().map(IntervencaoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public IntervencaoResponse buscar(@PathVariable Long id) {
        return IntervencaoResponse.from(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<IntervencaoResponse> criar(@Valid @RequestBody IntervencaoRequest req) {
        IntervencaoResponse criada = IntervencaoResponse.from(service.criar(req));
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public IntervencaoResponse atualizar(@PathVariable Long id, @Valid @RequestBody IntervencaoRequest req) {
        return IntervencaoResponse.from(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
