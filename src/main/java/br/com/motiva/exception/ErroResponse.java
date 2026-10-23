package br.com.motiva.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponse(int status, String erro, String mensagem, LocalDateTime timestamp, Map<String, String> campos) {
    public static ErroResponse of(int status, String erro, String mensagem) {
        return new ErroResponse(status, erro, mensagem, LocalDateTime.now(), null);
    }
}
