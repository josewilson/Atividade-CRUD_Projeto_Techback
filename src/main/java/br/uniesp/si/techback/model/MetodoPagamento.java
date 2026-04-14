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
@Table(name = "metodo_pagamento")
public class MetodoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 20)
    private String bandeira;

    @Column(name = "ultimos4", nullable = false, length = 4)
    private String ultimos4;

    @Column(name = "mes_exp", nullable = false)
    private Short mesExp;

    @Column(name = "ano_exp", nullable = false)
    private Short anoExp;

    @Column(name = "nome_portador", nullable = false, length = 150)
    private String nomePortador;

    @Column(name = "token_gateway", nullable = false, length = 120)
    private String tokenGateway;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}
