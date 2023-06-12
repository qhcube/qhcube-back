package es.upm.etsisi.qhcube.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateDTO {
    // Coorndenadas en formato (x,y,z,i)
    private int[] coordinates = new int[4];

    // return "[x,y,z,i]"
    @Override
    public String toString() {
        return "[" + coordinates[0] + "," + coordinates[1] + "," + coordinates[2] + "," + coordinates[3] + "]";
    }

    public CoordinateDTO setRandomCoordinate() {
        for (int i = 0; i < 4; i++) {
            this.coordinates[i] = (int) (Math.random() * 8);
        }
        return this;
    }

    public void exchange(int j, int k) {
        int aux = this.coordinates[j];
        this.coordinates[j] = this.coordinates[k];
        this.coordinates[k] = aux;
    }
}
