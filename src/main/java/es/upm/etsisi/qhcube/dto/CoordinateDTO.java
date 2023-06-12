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
    private int x;
    private int y;
    private int z;
    private int i;



    // return "[x,y,z,i]"
    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "," + i + "]";
    }

    public CoordinateDTO setRandomCoordinate() {
        this.x = (int) (Math.random() * 8);
        this.y = (int) (Math.random() * 8);
        this.z = (int) (Math.random() * 8);
        this.i = (int) (Math.random() * 8);
        return this;
    }
}
