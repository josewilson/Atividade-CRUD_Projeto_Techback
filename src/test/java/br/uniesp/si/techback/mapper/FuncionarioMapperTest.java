package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.model.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do FuncionarioMapper")
class FuncionarioMapperTest {

    private FuncionarioMapper funcionarioMapper;
    private Funcionario funcionario;
    private FuncionarioDTO funcionarioDTO;

    @BeforeEach
    void setUp() {
        funcionarioMapper = new FuncionarioMapper();

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
    @DisplayName("Deve converter Entity para DTO")
    void deveConverterEntityParaDTO() {
        FuncionarioDTO resultado = funcionarioMapper.toDTO(funcionario);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(funcionario.getId());
        assertThat(resultado.getNome()).isEqualTo(funcionario.getNome());
        assertThat(resultado.getCargo()).isEqualTo(funcionario.getCargo());
    }

    @Test
    @DisplayName("Deve converter DTO para Entity")
    void deveConverterDTOParaEntity() {
        Funcionario resultado = funcionarioMapper.toEntity(funcionarioDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(funcionarioDTO.getId());
        assertThat(resultado.getNome()).isEqualTo(funcionarioDTO.getNome());
        assertThat(resultado.getCargo()).isEqualTo(funcionarioDTO.getCargo());
    }

    @Test
    @DisplayName("Deve retornar null quando converter Entity null para DTO")
    void deveRetornarNullQuandoConverterEntityNullParaDTO() {
        FuncionarioDTO resultado = funcionarioMapper.toDTO(null);

        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando converter DTO null para Entity")
    void deveRetornarNullQuandoConverterDTONullParaEntity() {
        Funcionario resultado = funcionarioMapper.toEntity(null);

        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve manter consistência na conversão bidirecional")
    void deveManterConsistenciaNaConversaoBidirecional() {
        FuncionarioDTO dtoConvertido = funcionarioMapper.toDTO(funcionario);
        Funcionario entityReconvertida = funcionarioMapper.toEntity(dtoConvertido);

        assertThat(entityReconvertida.getId()).isEqualTo(funcionario.getId());
        assertThat(entityReconvertida.getNome()).isEqualTo(funcionario.getNome());
        assertThat(entityReconvertida.getCargo()).isEqualTo(funcionario.getCargo());
    }
}
