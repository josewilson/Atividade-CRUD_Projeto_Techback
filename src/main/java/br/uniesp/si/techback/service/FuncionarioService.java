package br.uniesp.si.techback.service;

import br.uniesp.si.techback.client.ViaCepClient;
import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.dto.ViaCepResponseDTO;
import br.uniesp.si.techback.mapper.FuncionarioMapper;
import br.uniesp.si.techback.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;
    private final ViaCepClient viaCepClient;

    public List<FuncionarioDTO> listar() {
        return funcionarioRepository.findAll().stream().map(funcionarioMapper::toDTO).toList();
    }

    /** Cria o funcionário preenchendo o endereço pelo CEP (ViaCEP). */
    public FuncionarioDTO incluir(FuncionarioDTO dto) {
        ViaCepResponseDTO endereco = viaCepClient.buscarEnderecoPorCep(dto.getCep());
        if (endereco == null || endereco.getCep() == null) {
            throw new RuntimeException("CEP inválido ou não encontrado");
        }
        dto.setLogradouro(endereco.getLogradouro());
        dto.setBairro(endereco.getBairro());
        dto.setLocalidade(endereco.getLocalidade());
        dto.setUf(endereco.getUf());
        return salvar(dto);
    }

    public FuncionarioDTO buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .map(funcionarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));
    }

    public FuncionarioDTO salvar(FuncionarioDTO dto) {
        return funcionarioMapper.toDTO(funcionarioRepository.save(funcionarioMapper.toEntity(dto)));
    }

    public FuncionarioDTO atualizar(Long id, FuncionarioDTO dto) {
        funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Falha ao atualizar: funcionário não encontrado com o ID: " + id));
        var entidade = funcionarioMapper.toEntity(dto);
        entidade.setId(id);
        return funcionarioMapper.toDTO(funcionarioRepository.save(entidade));
    }

    public void excluir(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new RuntimeException("Falha ao excluir: funcionário não encontrado com o ID: " + id);
        }
        funcionarioRepository.deleteById(id);
    }
}
