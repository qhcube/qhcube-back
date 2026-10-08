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
@Table(name = "game")
public class Game {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "initial_time")
    private Timestamp initialTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Timestamp endTime;

    @Basic
    @Column(name = "movs_list", columnDefinition = "LONGTEXT")
    private String movsList;

    @Basic
    @Column(name = "input")
    private String input;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game that = (Game) o;
        return id == that.id
                && player.getId() == that.player.getId()
                && Objects.equals(initialTime, that.initialTime)
                && Objects.equals(endTime, that.endTime)
                && Objects.equals(movsList, that.movsList)
                && Objects.equals(input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, initialTime, endTime, player, movsList, input);
    }
}
