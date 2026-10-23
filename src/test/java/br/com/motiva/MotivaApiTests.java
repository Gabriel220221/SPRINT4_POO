package br.com.motiva;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MotivaApiTests {

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;

    static final String TRECHO = """
            {"rodovia":"SP-150","kmInicial":10.0,"kmFinal":15.5,"tipo":"acostamento","nivelVegetacaoCm":85.0}""";

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void crudTrechoComCodigosHttpCorretos() throws Exception {
        mvc.perform(post("/api/trechos").contentType(MediaType.APPLICATION_JSON).content(TRECHO))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.tipo").value("ACOSTAMENTO"));

        mvc.perform(get("/api/trechos")).andExpect(status().isOk());
        mvc.perform(get("/api/trechos/999999")).andExpect(status().isNotFound());
        mvc.perform(put("/api/trechos/999999").contentType(MediaType.APPLICATION_JSON).content(TRECHO))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/trechos/999999")).andExpect(status().isNotFound());
    }

    @Test
    void nivelNegativoRetorna400() throws Exception {
        String invalido = """
                {"rodovia":"SP-150","kmInicial":1,"kmFinal":2,"tipo":"pista","nivelVegetacaoCm":-5}""";
        mvc.perform(post("/api/trechos").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void relatorioClassificaEPersisteNoHistorico() throws Exception {
        mvc.perform(post("/api/trechos").contentType(MediaType.APPLICATION_JSON).content(TRECHO))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/relatorios"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.urgentes").isNumber());

        mvc.perform(get("/api/relatorios")).andExpect(status().isOk());
        mvc.perform(get("/api/relatorios/periodo?inicio=2000-01-01&fim=2100-01-01")).andExpect(status().isOk());
        mvc.perform(get("/api/relatorios/periodo?inicio=2100-01-01&fim=2000-01-01")).andExpect(status().isBadRequest());
    }
}
