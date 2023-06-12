package es.upm.etsisi.qhcube.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputDTO {
    private CoordinateDTO[] rowA = new CoordinateDTO[4];
    private CoordinateDTO[] rowB = new CoordinateDTO[4];


    // return "[[[x,y,z,i],[x,y,z,i],[x,y,z,i],[x,y,z,i]],[[x,y,z,i],[x,y,z,i],[x,y,z,i],[x,y,z,i]]]"
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[[");
        for (int i = 0; i < 4; i++) {
            str.append(rowA[i].toString()).append(",");
        }
        str.replace(str.length() - 1, str.length(), "");
        str.append("],[");
        for (int i = 0; i < 4; i++) {
            str.append(rowB[i].toString()).append(",");
        }
        str.replace(str.length() - 1, str.length(), "");
        str.append("]]");
        return str.toString();
    }

    public InputDTO setRandomInput() {
        for (int i = 0; i < 4; i++) {
            rowA[i] = new CoordinateDTO().setRandomCoordinate();
            rowB[i] = new CoordinateDTO().setRandomCoordinate();
        }
        return this;
    }

    public InputDTO setInitialPracticeInput() {

        // [[7, -2, 1, 0], [3, 6, 1, 0], [-1, -1, 7, -1], [0, 1, -1, 6]],[[2, -2, -1, 1], [0, 4, 1, -1], [-1, 1, 1, 3], [0, -1, 3, 4]]        ]

        rowA[0] = new CoordinateDTO(7, -2, 1, 0);
        rowA[1] = new CoordinateDTO(3, 6, 1, 0);
        rowA[2] = new CoordinateDTO(-1, -1, 7, -1);
        rowA[3] = new CoordinateDTO(0, 1, -1, 6);
        rowB[0] = new CoordinateDTO(2, -2, -1, 1);
        rowB[1] = new CoordinateDTO(0, 4, 1, -1);
        rowB[2] = new CoordinateDTO(-1, 1, 1, 3);
        rowB[3] = new CoordinateDTO(0, -1, 3, 4);
        return this;
    }

    public void exchange(int j, int k) {
        CoordinateDTO aux = this.getRowA()[j];
        this.getRowA()[j] = this.getRowA()[k];
        rowA[k] = aux;

        aux = this.getRowB()[j];
        this.getRowB()[k] = this.getRowB()[j];
        this.getRowB()[j] = aux;

    }
}
