package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O cargo é obrigatório")
    private String cargo;
}
