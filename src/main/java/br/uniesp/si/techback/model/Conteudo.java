package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "conteudo")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(nullable = false)
    private Short ano;

    @Column(name = "duracao_minutos", nullable = false)
    private Short duracaoMinutos;

    @Column(nullable = false)
    private Double relevancia;

    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @Column(name = "trailer_url", length = 500)
    private String trailerUrl;

    @Column(length = 50)
    private String genero;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public enum Tipo {
        FILME, SERIE
    }
}
