package es.upm.etsisi.qhcube.utils.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    // Coorndenadas en formato (x,y,z,i)
    private int[] coordinates = new int[4];

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
