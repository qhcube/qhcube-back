package es.upm.etsisi.qhcube.utils.action;

public class MoveValidator {
    private static final int MAX_MOVE_LENGTH = 100;

    public static boolean isValidMove(String move) {
        if (move == null || move.length() > MAX_MOVE_LENGTH) {
            return false;
        }
        if (!move.startsWith("[") || !move.endsWith("]")) {
            return false;
        }

        String[] parts = move.substring(1, move.length() - 1).split(",");
        if (parts.length != 4) {
            return false;
        }

        double[] values = new double[4];
        for (int i = 0; i < 4; i++) {
            try {
                values[i] = Double.parseDouble(parts[i]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (!Double.isFinite(values[i])) {
                return false;
            }
        }

        double type = values[0];
        if (type == Action.MOVE) {
            return true;                          // perspectiva: basta con tres números finitos
        }
        if (!isInteger(values)) {
            return false;                         // tipos 0-3: todo enteros
        }

        int elem = (int) values[1];
        int j = (int) values[2];
        int k = (int) values[3];
        if (elem != 1 && elem != 2) {
            return false;                         // 1 = filas, 2 = columnas
        }

        return switch ((int) type) {
            case Action.PERMUTE -> isIndex(j) && isIndex(k);
            case Action.EXCHANGE, Action.ROTATE -> isIndex(j) && k == -1;
            case Action.REDUCE -> j == -1 && k == -1;
            default -> false;                     // cualquier otro tipo, incluido el 5
        };
    }

    private static boolean isInteger(double[] values) {
        for (double v : values) {
            if (v != Math.floor(v)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isIndex(int value) {
        return value >= 0 && value <= 3;
    }
}
