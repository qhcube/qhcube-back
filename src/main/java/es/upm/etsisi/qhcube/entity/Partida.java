package es.upm.etsisi.qhcube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Partida {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "timepo_inicial")
    private Timestamp timepoInicial;

    @Basic
    @Column(name = "tiempo_fin")
    private Timestamp tiempoFin;

    @ManyToOne
    @JoinColumn(name = "jugador_id", referencedColumnName = "id", nullable = false)
    private Jugador jugadorByJugadorId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partida partida = (Partida) o;

        if (id != partida.id) return false;
        if (timepoInicial != null ? !timepoInicial.equals(partida.timepoInicial) : partida.timepoInicial != null)
            return false;
        if (tiempoFin != null ? !tiempoFin.equals(partida.tiempoFin) : partida.tiempoFin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (timepoInicial != null ? timepoInicial.hashCode() : 0);
        result = 31 * result + (tiempoFin != null ? tiempoFin.hashCode() : 0);
        return result;
    }

    public Jugador getJugadorByJugadorId() {
        return jugadorByJugadorId;
    }

    public void setJugadorByJugadorId(Jugador jugadorByJugadorId) {
        this.jugadorByJugadorId = jugadorByJugadorId;
    }
}
