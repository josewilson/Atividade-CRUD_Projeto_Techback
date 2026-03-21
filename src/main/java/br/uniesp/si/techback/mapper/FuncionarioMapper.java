package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.model.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.getId());
        funcionario.setNome(dto.getNome());
        funcionario.setCargo(dto.getCargo());

        return funcionario;
    }

    public FuncionarioDTO toDTO(Funcionario entity) {
        if (entity == null) {
            return null;
        }

        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCargo(entity.getCargo());

        return dto;
    }
}
