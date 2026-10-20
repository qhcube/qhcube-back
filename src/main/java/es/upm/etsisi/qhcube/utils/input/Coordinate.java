package es.upm.etsisi.qhcube.utils.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    // Coorndenadas en formato (x,y,z,i)
    private int[] coordinates = new int[4];

    public Coordinate(String coordinates) {
        //log.debug("Coordinates: " + coordinates);
        coordinates = coordinates.replace("[", "");
        coordinates = coordinates.replace("]", "");
        String[] aux = coordinates.split(",");
        for (int i = 0; i < 4; i++) {
            this.coordinates[i] = Integer.parseInt(aux[i]);
        }
    }

    // return "[x,y,z,i]"
    @Override
    public String toString() {
        return "[" + coordinates[0] + "," + coordinates[1] + "," + coordinates[2] + "," + coordinates[3] + "]";
    }

    public void setRandomCoordinate() {
        for (int i = 0; i < 4; i++) {
            this.coordinates[i] = (int) (Math.random() * 8);
        }
    }
}
