package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "plano")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(name = "limite_diario", nullable = false)
    private Short limiteDiario;

    @Column(name = "streams_simultaneos", nullable = false)
    private Short streamsSimultaneos;
}
