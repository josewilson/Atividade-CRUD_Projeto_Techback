package br.uniesp.si.techback.config;

import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/** Cria o admin inicial a partir de variáveis de ambiente; nenhuma senha fica no repositório. */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String email;

    @Value("${app.admin.password:}")
    private String password;

    @Override
    public void run(String... args) {
        if (password.isBlank() || usuarioRepository.findByUsername("admin").isPresent()) {
            return;
        }
        LocalDateTime agora = LocalDateTime.now();
        usuarioRepository.save(Usuario.builder()
                .username("admin")
                .email(email)
                .password(passwordEncoder.encode(password))
                .nomeCompleto("Administrador")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .perfil(Usuario.Perfil.ADMIN)
                .roles(Set.of("ADMIN"))
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build());
        log.info("Usuário admin inicial criado");
    }
}
