package br.uniesp.si.techback.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Fluxo JWT ponta a ponta")
class FluxoJwtIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("cadastro -> login -> token -> endpoint protegido")
    void fluxoCompleto() throws Exception {
        String cadastro = """
                {"username":"maria","password":"senha123","email":"maria@example.com"}""";
        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(cadastro))
                .andExpect(status().isCreated());

        String login = """
                {"username":"maria","password":"senha123"}""";
        String corpo = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(login))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(corpo);
        String token = json.get("token").asText();

        mockMvc.perform(get("/filmes").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(get("/filmes"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/filmes").header("Authorization", "Bearer token.invalido.x"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("login com senha errada retorna 401")
    void loginInvalido() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ninguem\",\"password\":\"x\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("usuário comum recebe 403 em /funcionarios")
    void usuarioComumNaoAcessaFuncionarios() throws Exception {
        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"joao\",\"password\":\"senha123\",\"email\":\"joao@example.com\"}"));
        String corpo = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"joao\",\"password\":\"senha123\"}"))
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(corpo).get("token").asText();

        mockMvc.perform(get("/funcionarios").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
