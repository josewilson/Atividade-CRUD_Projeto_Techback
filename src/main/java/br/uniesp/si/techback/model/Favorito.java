package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "favorito")
@IdClass(Favorito.FavoritoId.class)
public class Favorito {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Id
    @Column(name = "conteudo_id")
    private Long conteudoId;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conteudo_id", insertable = false, updatable = false)
    private Conteudo conteudo;

    public static class FavoritoId implements java.io.Serializable {
        private Long usuarioId;
        private Long conteudoId;

        public FavoritoId() {}

        public FavoritoId(Long usuarioId, Long conteudoId) {
            this.usuarioId = usuarioId;
            this.conteudoId = conteudoId;
        }

        // getters and setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public Long getConteudoId() { return conteudoId; }
        public void setConteudoId(Long conteudoId) { this.conteudoId = conteudoId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavoritoId that = (FavoritoId) o;
            return usuarioId.equals(that.usuarioId) && conteudoId.equals(that.conteudoId);
        }

        @Override
        public int hashCode() {
            return usuarioId.hashCode() + conteudoId.hashCode();
        }
    }
}
