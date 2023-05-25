package es.upm.etsisi.qhcube.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player")
public class Player {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "nombre")
    private String nombre;

    @Basic
    @Column(name = "apellidos")
    private String apellidos;

    @Basic
    @Column(name = "email", unique = true)
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "jwttoken")
    private String jwttoken;

    @Basic
    @Column(name = "enabled")
    private boolean enabled;

    @Basic
    @Column(name = "vericode")
    private String vericode;

    @OneToMany(mappedBy = "player")
    private List<Game> games;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        if (!Objects.equals(nombre, player.nombre)) return false;
        if (!Objects.equals(apellidos, player.apellidos)) return false;
        if (!Objects.equals(email, player.email)) return false;
        if (!Objects.equals(vericode, player.vericode)) return false;
        return Objects.equals(jwttoken, player.jwttoken);
    }

    @Override
    public int hashCode() {
        int result = Long.valueOf(id).intValue();
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (jwttoken != null ? jwttoken.hashCode() : 0);
        result = 31 * result + (vericode != null ? vericode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "nombre = " + nombre + ", " +
                "apellidos = " + apellidos + ", " +
                "email = " + email + ", " +
                "password = " + password + ", " +
                "jwttoken = " + jwttoken  + ", " +
                "vericode = " + vericode + ")";
    }
}
