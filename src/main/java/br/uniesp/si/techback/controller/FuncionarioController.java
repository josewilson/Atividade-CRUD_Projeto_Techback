package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
@Slf4j
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping
    public List<FuncionarioDTO> listar() {
        log.info("Listando todos os funcionários");
        List<FuncionarioDTO> funcionarios = funcionarioService.listar();
        log.debug("Total de funcionários encontrados: {}", funcionarios.size());
        return funcionarios;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> buscarPorId(@PathVariable Long id) {
        try {
            FuncionarioDTO funcionario = funcionarioService.buscarPorId(id);
            log.debug("Funcionário encontrado: {}", funcionario);
            return ResponseEntity.ok(funcionario);
        } catch (Exception e) {
            log.error("Erro ao buscar funcionário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> criar(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        log.info("Recebida requisição para criar novo funcionário");
        try {
            FuncionarioDTO funcionarioSalvo = funcionarioService.salvar(funcionarioDTO);
            log.info("Funcionário criado com sucesso. ID: {}", funcionarioSalvo.getId());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(funcionarioSalvo.getId())
                    .toUri();

            log.debug("URI de localização do novo funcionário: {}", location);

            return ResponseEntity.created(location).body(funcionarioSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar funcionário: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        log.info("Atualizando funcionário com ID {}: {}", id, funcionarioDTO);
        try {
            FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizar(id, funcionarioDTO);
            log.debug("Funcionário ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar funcionário ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Excluindo funcionário com ID: {}", id);
        try {
            funcionarioService.excluir(id);
            log.debug("Funcionário com ID {} excluído com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao excluir funcionário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}