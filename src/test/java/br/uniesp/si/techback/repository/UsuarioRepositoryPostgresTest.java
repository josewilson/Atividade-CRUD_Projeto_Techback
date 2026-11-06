package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/** Roda as migrações Flyway + ddl-auto=validate contra um PostgreSQL real. */
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryPostgresTest {

    @Container
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        r.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    UsuarioRepository repo;

    @Test
    void salvaEBuscaPorEmail() {
        repo.save(Usuario.builder().username("u1").email("u1@x.com").password("p")
                .nomeCompleto("U Um").dataNascimento(LocalDate.of(2000, 1, 1))
                .perfil(Usuario.Perfil.USER)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build());

        assertThat(repo.findByEmail("u1@x.com")).isPresent();
        assertThat(repo.findByUsername("nope")).isEmpty();
    }
}
