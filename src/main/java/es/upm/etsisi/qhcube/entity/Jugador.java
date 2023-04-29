package es.upm.etsisi.qhcube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Jugador {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "nombre")
    private String nombre;

    @Basic
    @Column(name = "apellidos")
    private String apellidos;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "jwttoken")
    private String jwttoken;

    @OneToMany(mappedBy = "jugadorByJugadorId")
    private Collection<Partida> partidasById;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jugador jugador = (Jugador) o;

        if (id != jugador.id) return false;
        if (nombre != null ? !nombre.equals(jugador.nombre) : jugador.nombre != null) return false;
        if (apellidos != null ? !apellidos.equals(jugador.apellidos) : jugador.apellidos != null) return false;
        if (email != null ? !email.equals(jugador.email) : jugador.email != null) return false;
        if (jwttoken != null ? !jwttoken.equals(jugador.jwttoken) : jugador.jwttoken != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (jwttoken != null ? jwttoken.hashCode() : 0);
        return result;
    }

    public Collection<Partida> getPartidasById() {
        return partidasById;
    }

    public void setPartidasById(Collection<Partida> partidasById) {
        this.partidasById = partidasById;
    }
}
