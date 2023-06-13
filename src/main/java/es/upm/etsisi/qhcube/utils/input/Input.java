package es.upm.etsisi.qhcube.utils.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Input {
    private Coordinate[] rowA = new Coordinate[4];
    private Coordinate[] rowB = new Coordinate[4];


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

    public int[][][] getVarCoordinates() {
        int[][][] varCoordinates = new int[2][4][4];
        for (int i = 0; i < 4; i++) {
            varCoordinates[0][i] = rowA[i].getCoordinates();
            varCoordinates[1][i] = rowB[i].getCoordinates();
        }
        return varCoordinates;
    }

    public void setVarCoordinates(int[][][] varCoordinates) {
        for (int i = 0; i < 4; i++) {
            rowA[i].setCoordinates(varCoordinates[0][i]);
            rowB[i].setCoordinates(varCoordinates[1][i]);
        }
    }

    public void setInitialPracticeInput() {

        // [[7, -2, 1, 0], [3, 6, 1, 0], [-1, -1, 7, -1], [0, 1, -1, 6]],[[2, -2, -1, 1], [0, 4, 1, -1], [-1, 1, 1, 3], [0, -1, 3, 4]]        ]

        rowA[0] = new Coordinate(new int[]{7, -2, 1, 0});
        rowA[1] = new Coordinate(new int[]{3, 6, 1, 0});
        rowA[2] = new Coordinate(new int[]{-1, -1, 7, -1});
        rowA[3] = new Coordinate(new int[]{0, 1, -1, 6});
        rowB[0] = new Coordinate(new int[]{2, -2, -1, 1});
        rowB[1] = new Coordinate(new int[]{0, 4, 1, -1});
        rowB[2] = new Coordinate(new int[]{-1, 1, 1, 3});
        rowB[3] = new Coordinate(new int[]{0, -1, 3, 4});
    }

    public void permute(int elem, int j, int k) {
        int var_k, var_aux;
        int [][][] var_coor = this.getVarCoordinates();

        if (elem == 1) {
            for (var_k = 0; var_k < 4; ++var_k) {
                var_aux = var_coor[0][j][var_k];
                var_coor[0][j][var_k] = var_coor[0][k][var_k];
                var_coor[0][k][var_k] = var_aux;

                var_aux = var_coor[1][j][var_k];
                var_coor[1][j][var_k] = var_coor[1][k][var_k];
                var_coor[1][k][var_k] = var_aux;
            }
        } else {
            for (var_k = 0; var_k < 4; ++var_k) {
                var_aux = var_coor[0][var_k][j];
                var_coor[0][var_k][j] = var_coor[0][var_k][k];
                var_coor[0][var_k][k] = var_aux;

                var_aux = var_coor[1][var_k][j];
                var_coor[1][var_k][j] = var_coor[1][var_k][k];
                var_coor[1][var_k][k] = var_aux;
            }
        }

        this.setVarCoordinates(var_coor);
    }

    public void exchange(int elem, int j) {
        int var_k, var_aux;
        int [][][] var_coor = this.getVarCoordinates();

        if (elem == 1) {
            for (var_k = 0; var_k < 4; ++var_k) {
                var_aux = var_coor[0][j][var_k];
                var_coor[0][j][var_k] = -var_coor[1][j][var_k];
                var_coor[1][j][var_k] = var_aux;
            }
        } else {
            for (var_k = 0; var_k < 4; ++var_k) {
                var_aux = var_coor[0][var_k][j];
                var_coor[0][var_k][j] = -var_coor[1][var_k][j];
                var_coor[1][var_k][j] = var_aux;
            }
        }

        this.setVarCoordinates(var_coor);
    }

    public void rotateRows1and2() {
        int i, x0, y0, x1, y1;
        int[][][] var_coor = this.getVarCoordinates();

        for (i = 0; i < 4; ++i) {
            x0 = var_coor[0][0][i] + var_coor[0][1][i] - var_coor[1][0][i] + var_coor[1][1][i];
            y0 = var_coor[0][0][i] - var_coor[0][1][i] + var_coor[1][0][i] + var_coor[1][1][i];
            x1 = var_coor[0][0][i] - var_coor[0][1][i] - var_coor[1][0][i] - var_coor[1][1][i];
            y1 = var_coor[0][0][i] + var_coor[0][1][i] + var_coor[1][0][i] - var_coor[1][1][i];
            var_coor[0][0][i] = x0;
            var_coor[1][0][i] = y0;
            var_coor[0][1][i] = x1;
            var_coor[1][1][i] = y1;
            x0 = 2 * var_coor[0][2][i];
            y0 = 2 * var_coor[1][2][i];
            x1 = 2 * var_coor[0][3][i];
            y1 = 2 * var_coor[1][3][i];
            var_coor[0][2][i] = x0;
            var_coor[1][2][i] = y0;
            var_coor[0][3][i] = x1;
            var_coor[1][3][i] = y1;
        }

        this.setVarCoordinates(var_coor);
    }

    public void rotateColumns1and2() {
        int i, x0, y0, x1, y1;
        int[][][] var_coor = this.getVarCoordinates();

        for (i = 0; i < 4; ++i) {
            x0 = var_coor[0][i][0] + var_coor[0][i][1] - var_coor[1][i][0] + var_coor[1][i][1];
            y0 = var_coor[0][i][0] - var_coor[0][i][1] + var_coor[1][i][0] + var_coor[1][i][1];
            x1 = var_coor[0][i][0] - var_coor[0][i][1] - var_coor[1][i][0] - var_coor[1][i][1];
            y1 = var_coor[0][i][0] + var_coor[0][i][1] + var_coor[1][i][0] - var_coor[1][i][1];
            var_coor[0][i][0] = x0;
            var_coor[1][i][0] = y0;
            var_coor[0][i][1] = x1;
            var_coor[1][i][1] = y1;
            x0 = 2 * var_coor[0][i][2];
            y0 = 2 * var_coor[1][i][2];
            x1 = 2 * var_coor[0][i][3];
            y1 = 2 * var_coor[1][i][3];
            var_coor[0][i][2] = x0;
            var_coor[1][i][2] = y0;
            var_coor[0][i][3] = x1;
            var_coor[1][i][3] = y1;
        }

        this.setVarCoordinates(var_coor);
    }

    public void rotate(int elem, int j) {
        if (elem == 1) {
            if (j == 1) {
                this.permute(1, 0, 1);
                this.permute(1, 1, 2);
            }
            if (j == 2) {
                this.permute(1, 0, 2);
                this.permute(1, 1, 3);
            }
            this.rotateRows1and2();
            if (j == 1) {
                this.permute(1, 1, 2);
                this.permute(1, 0, 1);
            }
            if (j == 2) {
                this.permute(1, 1, 3);
                this.permute(1, 0, 2);
            }
        } else {
            if (j == 1) {
                this.permute(2, 0, 1);
                this.permute(2, 1, 2);
            }
            if (j == 2) {
                this.permute(2, 0, 2);
                this.permute(2, 1, 3);
            }
            if (j == 3) {
                this.permute(2, 0, 3);
                this.permute(2, 1, 3);
            }
            this.rotateColumns1and2();
            if (j == 1) {
                this.permute(2, 1, 2);
                this.permute(2, 0, 1);
            }
            if (j == 2) {
                this.permute(2, 1, 3);
                this.permute(2, 0, 2);
            }
            if (j == 3) {
                this.permute(2, 1, 3);
                this.permute(2, 0, 3);
            }
        }
    }

    public void reduce(int elem) {
        int var_k, x, y;
        int[][][] var_coor = this.getVarCoordinates();

        if (elem == 1) {
            for (var_k = 0; var_k < 4; ++var_k) {
                x = var_coor[0][0][var_k];
                y = var_coor[0][1][var_k];
                var_coor[0][0][var_k] = x + y;
                var_coor[0][1][var_k] = x - y;
                x = var_coor[1][0][var_k];
                y = var_coor[1][1][var_k];
                var_coor[1][0][var_k] = x + y;
                var_coor[1][1][var_k] = x - y;

                x = var_coor[0][2][var_k];
                y = var_coor[0][3][var_k];
                var_coor[0][2][var_k] = x + y;
                var_coor[0][3][var_k] = x - y;
                x = var_coor[1][2][var_k];
                y = var_coor[1][3][var_k];
                var_coor[1][2][var_k] = x + y;
                var_coor[1][3][var_k] = x - y;
            }
        } else {
            for (var_k = 0; var_k < 4; ++var_k) {
                x = var_coor[0][var_k][0];
                y = var_coor[0][var_k][1];
                var_coor[0][var_k][0] = x + y;
                var_coor[0][var_k][1] = x - y;
                x = var_coor[1][var_k][0];
                y = var_coor[1][var_k][1];
                var_coor[1][var_k][0] = x + y;
                var_coor[1][var_k][1] = x - y;

                x = var_coor[0][var_k][2];
                y = var_coor[0][var_k][3];
                var_coor[0][var_k][2] = x + y;
                var_coor[0][var_k][3] = x - y;
                x = var_coor[1][var_k][2];
                y = var_coor[1][var_k][3];
                var_coor[1][var_k][2] = x + y;
                var_coor[1][var_k][3] = x - y;
            }
        }

        this.setVarCoordinates(var_coor);
    }

    public boolean isReductible() {
        int var_j, var_k;
        boolean result = true;
        int[][][] var_coor = this.getVarCoordinates();

        for (var_j = 0; var_j < 4; ++var_j) {
            for (var_k = 0; var_k < 4; ++var_k) {
                if (var_coor[0][var_j][var_k] % 2 == 1) result = false;
                if (var_coor[1][var_j][var_k] % 2 == 1) result = false;
            }
        }

        return (result);
    }

    public int reduceLevel() {
        int var_j, var_k, x, nivel;
        int[][][] var_coor = this.getVarCoordinates();

        for (var_j = 0; var_j < 4; ++var_j) {
            for (var_k = 0; var_k < 4; ++var_k) {
                x = (int) Math.floor((double) var_coor[0][var_j][var_k] / 2);
                var_coor[0][var_j][var_k] = x;
                x = (int) Math.floor((double) var_coor[1][var_j][var_k] / 2);
                var_coor[1][var_j][var_k] = x;
            }
        }
        nivel = 0;
        for (var_k = 0; var_k < 4; ++var_k) {
            nivel += var_coor[0][0][var_k] * var_coor[0][0][var_k] + var_coor[1][0][var_k] * var_coor[1][0][var_k];
        }
        this.setVarCoordinates(var_coor);
        return (nivel);
    }
}

