package br.uniesp.si.techback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViaCepResponseDTO {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String regiao;


    @JsonProperty("erro")
    private Boolean erro;

}
