package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.mapper.FuncionarioMapper;
import br.uniesp.si.techback.model.Funcionario;
import br.uniesp.si.techback.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do FuncionarioService")
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private Funcionario funcionario;
    private FuncionarioDTO funcionarioDTO;

    @BeforeEach
    void setUp() {
        funcionario = Funcionario.builder()
                .id(1L)
                .nome("João Silva")
                .cargo("Desenvolvedor")
                .build();

        funcionarioDTO = FuncionarioDTO.builder()
                .id(1L)
                .nome("João Silva")
                .cargo("Desenvolvedor")
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os funcionários")
    void deveListarTodosOsFuncionarios() {
        List<Funcionario> funcionarios = Arrays.asList(funcionario);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);
        when(funcionarioMapper.toDTO(funcionario)).thenReturn(funcionarioDTO);

        List<FuncionarioDTO> result = funcionarioService.listar();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo("João Silva");
        verify(funcionarioRepository).findAll();
        verify(funcionarioMapper).toDTO(funcionario);
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID quando existir")
    void deveBuscarFuncionarioPorId() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioMapper.toDTO(funcionario)).thenReturn(funcionarioDTO);

        FuncionarioDTO result = funcionarioService.buscarPorId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("João Silva");
        verify(funcionarioRepository).findById(1L);
        verify(funcionarioMapper).toDTO(funcionario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar funcionário por ID inexistente")
    void deveLancarExcecaoAoBuscarFuncionarioPorIdInexistente() {
        when(funcionarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Funcionário não encontrado com o ID: 999");
        verify(funcionarioRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar um novo funcionário")
    void deveSalvarNovoFuncionario() {
        FuncionarioDTO novoFuncionarioDTO = FuncionarioDTO.builder()
                .nome("Maria Santos")
                .cargo("Analista")
                .build();

        Funcionario novoFuncionario = Funcionario.builder()
                .nome("Maria Santos")
                .cargo("Analista")
                .build();

        Funcionario funcionarioSalvo = Funcionario.builder()
                .id(2L)
                .nome("Maria Santos")
                .cargo("Analista")
                .build();

        FuncionarioDTO funcionarioSalvoDTO = FuncionarioDTO.builder()
                .id(2L)
                .nome("Maria Santos")
                .cargo("Analista")
                .build();

        when(funcionarioMapper.toEntity(novoFuncionarioDTO)).thenReturn(novoFuncionario);
        when(funcionarioRepository.save(novoFuncionario)).thenReturn(funcionarioSalvo);
        when(funcionarioMapper.toDTO(funcionarioSalvo)).thenReturn(funcionarioSalvoDTO);

        FuncionarioDTO result = funcionarioService.salvar(novoFuncionarioDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getNome()).isEqualTo("Maria Santos");
        verify(funcionarioRepository).save(novoFuncionario);
        verify(funcionarioMapper).toEntity(novoFuncionarioDTO);
        verify(funcionarioMapper).toDTO(funcionarioSalvo);
    }

    @Test
    @DisplayName("Deve atualizar funcionário existente")
    void deveAtualizarFuncionarioExistente() {
        FuncionarioDTO funcionarioAtualizadoDTO = FuncionarioDTO.builder()
                .id(1L)
                .nome("João Silva Atualizado")
                .cargo("Desenvolvedor Sênior")
                .build();

        Funcionario funcionarioAtualizado = Funcionario.builder()
                .id(1L)
                .nome("João Silva Atualizado")
                .cargo("Desenvolvedor Sênior")
                .build();

        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioMapper.toEntity(funcionarioAtualizadoDTO)).thenReturn(funcionarioAtualizado);
        when(funcionarioRepository.save(funcionarioAtualizado)).thenReturn(funcionarioAtualizado);
        when(funcionarioMapper.toDTO(funcionarioAtualizado)).thenReturn(funcionarioAtualizadoDTO);

        FuncionarioDTO result = funcionarioService.atualizar(1L, funcionarioAtualizadoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("João Silva Atualizado");
        assertThat(result.getCargo()).isEqualTo("Desenvolvedor Sênior");
        verify(funcionarioRepository).findById(1L);
        verify(funcionarioRepository).save(funcionarioAtualizado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar funcionário inexistente")
    void deveLancarExcecaoAoAtualizarFuncionarioInexistente() {
        FuncionarioDTO funcionarioAtualizadoDTO = FuncionarioDTO.builder()
                .nome("João Silva Atualizado")
                .cargo("Desenvolvedor Sênior")
                .build();

        when(funcionarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.atualizar(999L, funcionarioAtualizadoDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Falha ao atualizar: funcionário não encontrado com o ID: 999");
        verify(funcionarioRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve excluir funcionário existente")
    void deveExcluirFuncionarioExistente() {
        when(funcionarioRepository.existsById(1L)).thenReturn(true);

        funcionarioService.excluir(1L);

        verify(funcionarioRepository).existsById(1L);
        verify(funcionarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir funcionário inexistente")
    void deveLancarExcecaoAoExcluirFuncionarioInexistente() {
        when(funcionarioRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> funcionarioService.excluir(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Falha ao excluir: funcionário não encontrado com o ID: 999");
        verify(funcionarioRepository).existsById(999L);
        verify(funcionarioRepository, never()).deleteById(999L);
    }
}
