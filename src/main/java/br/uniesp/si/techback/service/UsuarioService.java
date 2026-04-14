package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        log.info("Listando todos os usuários");
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

            return converterParaDTO(usuario);

        } catch (Exception e) {
            log.error("Erro ao buscar usuário com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        try {
            if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
                throw new RuntimeException("Username já existe: " + usuarioDTO.getUsername());
            }

            if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
                throw new RuntimeException("Email já existe: " + usuarioDTO.getEmail());
            }

            Usuario usuario = converterParaEntidade(usuarioDTO);
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            return converterParaDTO(usuarioSalvo);

        } catch (Exception e) {
            log.error("Erro ao salvar usuário: {}", e.getMessage(), e);
            throw e;
        }
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
        try {
            Usuario usuarioExistente = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

            usuarioRepository.findByUsername(usuarioDTO.getUsername()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new RuntimeException("Username já existe: " + usuarioDTO.getUsername());
                }
            });

            usuarioRepository.findByEmail(usuarioDTO.getEmail()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new RuntimeException("Email já existe: " + usuarioDTO.getEmail());
                }
            });

            usuarioExistente.setUsername(usuarioDTO.getUsername());
            usuarioExistente.setEmail(usuarioDTO.getEmail());
            usuarioExistente.setRoles(usuarioDTO.getRoles());

            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                usuarioExistente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }

            Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

            return converterParaDTO(usuarioAtualizado);

        } catch (Exception e) {
            log.error("Erro ao atualizar usuário ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void excluir(Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

            usuarioRepository.delete(usuario);

        } catch (Exception e) {
            log.error("Erro ao excluir usuário com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    UsuarioDTO converterParaDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setRoles(usuario.getRoles());
        return dto;
    }

    Usuario converterParaEntidade(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRoles(usuarioDTO.getRoles());
        usuario.setNomeCompleto(usuarioDTO.getUsername());
        usuario.setDataNascimento(LocalDate.now());
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setAtualizadoEm(LocalDateTime.now());
        usuario.setPerfil(Usuario.Perfil.USER);
        return usuario;
    }
}
