package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FuncionarioController.class)
@DisplayName("Testes do FuncionarioController")
class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FuncionarioService funcionarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private FuncionarioDTO funcionarioDTO;
    private FuncionarioDTO funcionarioSalvoDTO;

    @BeforeEach
    void setUp() {
        funcionarioDTO = FuncionarioDTO.builder()
                .nome("João Silva")
                .cargo("Desenvolvedor")
                .build();

        funcionarioSalvoDTO = FuncionarioDTO.builder()
                .id(1L)
                .nome("João Silva")
                .cargo("Desenvolvedor")
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os funcionários")
    void deveListarTodosOsFuncionarios() throws Exception {
        List<FuncionarioDTO> funcionarios = Arrays.asList(funcionarioSalvoDTO);
        when(funcionarioService.listar()).thenReturn(funcionarios);

        mockMvc.perform(get("/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].cargo").value("Desenvolvedor"));
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID quando existir")
    void deveBuscarFuncionarioPorIdQuandoExistir() throws Exception {
        when(funcionarioService.buscarPorId(1L)).thenReturn(funcionarioSalvoDTO);

        mockMvc.perform(get("/funcionarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cargo").value("Desenvolvedor"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando buscar funcionário por ID inexistente")
    void deveRetornar404QuandoBuscarFuncionarioPorIdInexistente() throws Exception {
        when(funcionarioService.buscarPorId(999L)).thenThrow(new RuntimeException("Funcionário não encontrado com o ID: 999"));

        mockMvc.perform(get("/funcionarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar um novo funcionário")
    void deveCriarNovoFuncionario() throws Exception {
        when(funcionarioService.salvar(any(FuncionarioDTO.class))).thenReturn(funcionarioSalvoDTO);

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando criar funcionário com dados inválidos")
    void deveRetornar400QuandoCriarFuncionarioComDadosInvalidos() throws Exception {
        FuncionarioDTO funcionarioInvalido = FuncionarioDTO.builder()
                .nome("") // Nome vazio deve falhar na validação
                .cargo("")
                .build();

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar um funcionário existente")
    void deveAtualizarFuncionarioExistente() throws Exception {
        FuncionarioDTO funcionarioAtualizado = FuncionarioDTO.builder()
                .id(1L)
                .nome("João Silva Atualizado")
                .cargo("Desenvolvedor Sênior")
                .build();

        when(funcionarioService.atualizar(eq(1L), any(FuncionarioDTO.class))).thenReturn(funcionarioAtualizado);

        mockMvc.perform(put("/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"))
                .andExpect(jsonPath("$.cargo").value("Desenvolvedor Sênior"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando tentar atualizar funcionário inexistente")
    void deveRetornar404QuandoTentarAtualizarFuncionarioInexistente() throws Exception {
        when(funcionarioService.atualizar(eq(999L), any(FuncionarioDTO.class)))
                .thenThrow(new RuntimeException("Falha ao atualizar: funcionário não encontrado com o ID: 999"));

        mockMvc.perform(put("/funcionarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 400 quando atualizar funcionário com dados inválidos")
    void deveRetornar400QuandoAtualizarFuncionarioComDadosInvalidos() throws Exception {
        FuncionarioDTO funcionarioInvalido = FuncionarioDTO.builder()
                .nome("") // Nome vazio deve falhar na validação
                .cargo("")
                .build();

        mockMvc.perform(put("/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve excluir um funcionário existente")
    void deveExcluirFuncionarioExistente() throws Exception {
        mockMvc.perform(delete("/funcionarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 204 quando tentar excluir funcionário inexistente")
    void deveRetornar204QuandoTentarExcluirFuncionarioInexistente() throws Exception {
        mockMvc.perform(delete("/funcionarios/999"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve criar funcionário com dados válidos")
    void deveCriarFuncionarioComDadosValidos() throws Exception {
        when(funcionarioService.salvar(any(FuncionarioDTO.class))).thenReturn(funcionarioSalvoDTO);

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(header().exists("Location"));
    }
}
