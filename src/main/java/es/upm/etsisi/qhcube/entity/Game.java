package es.upm.etsisi.qhcube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "partida")
public class Game {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timepo_inicial")
    private Timestamp tiempoInicial;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tiempo_fin")
    private Timestamp tiempoFinal;

    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Player jugador;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game partida = (Game) o;

        if (id != partida.id) return false;
        if (!Objects.equals(tiempoInicial, partida.tiempoInicial))
            return false;
        return Objects.equals(tiempoFinal, partida.tiempoFinal);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (tiempoInicial != null ? tiempoInicial.hashCode() : 0);
        result = 31 * result + (tiempoFinal != null ? tiempoFinal.hashCode() : 0);
        return result;
    }
}
