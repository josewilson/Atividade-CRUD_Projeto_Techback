package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes do FuncionarioRepository")
class FuncionarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private Funcionario funcionarioTeste;

    @BeforeEach
    void setUp() {
        funcionarioTeste = Funcionario.builder()
                .nome("João Silva")
                .cargo("Desenvolvedor")
                .build();
    }

    @Test
    @DisplayName("Deve salvar um funcionário com sucesso")
    void deveSalvarFuncionario() {
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioTeste);

        assertThat(funcionarioSalvo).isNotNull();
        assertThat(funcionarioSalvo.getId()).isNotNull();
        assertThat(funcionarioSalvo.getNome()).isEqualTo(funcionarioTeste.getNome());
        assertThat(funcionarioSalvo.getCargo()).isEqualTo(funcionarioTeste.getCargo());
    }

    @Test
    @DisplayName("Deve encontrar funcionário por ID quando existir")
    void deveEncontrarFuncionarioPorId() {
        Funcionario funcionarioSalvo = entityManager.persistAndFlush(funcionarioTeste);

        Optional<Funcionario> funcionarioEncontrado = funcionarioRepository.findById(funcionarioSalvo.getId());

        assertThat(funcionarioEncontrado).isPresent();
        assertThat(funcionarioEncontrado.get().getId()).isEqualTo(funcionarioSalvo.getId());
        assertThat(funcionarioEncontrado.get().getNome()).isEqualTo(funcionarioTeste.getNome());
    }

    @Test
    @DisplayName("Deve retornar vazio quando buscar por ID inexistente")
    void deveRetornarVazioQuandoBuscarPorIdInexistente() {
        Optional<Funcionario> funcionarioEncontrado = funcionarioRepository.findById(999L);

        assertThat(funcionarioEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve listar todos os funcionários")
    void deveListarTodosOsFuncionarios() {
        entityManager.persistAndFlush(funcionarioTeste);

        Funcionario funcionario2 = Funcionario.builder()
                .nome("Maria Santos")
                .cargo("Analista")
                .build();
        entityManager.persistAndFlush(funcionario2);

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).hasSize(2);
        assertThat(funcionarios).extracting(Funcionario::getNome)
                .containsExactlyInAnyOrder(funcionarioTeste.getNome(), funcionario2.getNome());
    }

    @Test
    @DisplayName("Deve verificar se funcionário existe por ID")
    void deveVerificarSeFuncionarioExistePorId() {
        Funcionario funcionarioSalvo = entityManager.persistAndFlush(funcionarioTeste);

        boolean existe = funcionarioRepository.existsById(funcionarioSalvo.getId());
        boolean naoExiste = funcionarioRepository.existsById(999L);

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    @DisplayName("Deve deletar funcionário por ID")
    void deveDeletarFuncionarioPorId() {
        Funcionario funcionarioSalvo = entityManager.persistAndFlush(funcionarioTeste);

        funcionarioRepository.deleteById(funcionarioSalvo.getId());

        Optional<Funcionario> funcionarioDeletado = funcionarioRepository.findById(funcionarioSalvo.getId());
        assertThat(funcionarioDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve contar total de funcionários")
    void deveContarTotalDeFuncionarios() {
        entityManager.persistAndFlush(funcionarioTeste);

        Funcionario funcionario2 = Funcionario.builder()
                .nome("Maria Santos")
                .cargo("Analista")
                .build();
        entityManager.persistAndFlush(funcionario2);

        long totalFuncionarios = funcionarioRepository.count();

        assertThat(totalFuncionarios).isEqualTo(2);
    }
}
