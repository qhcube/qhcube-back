package es.upm.etsisi.qhcube.utils.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * This class represents an action taken by the player during a game.
 * These are the possible actions obtained from the javascript code, so everytiime the player invoke
 * one of these actions, the javascript code will send a request to the server with the action taken:
 * <p>
 *      <ul>
 *          <li>function Permutar(elem, j, k) { [...] }</li>
 *          <li>function Intercambiar(elem, j) { [...] }</li>
 *          <li>function Reducir(elem) { [...] }</li>
 *          <li>function Rotar(elem, j) { [...] }</li>
 *      </ul>
 * </p>
 *
 * In addition, everytime the player change the cube position in its canvas, the javascript code will
 * send a request to the server with the new position of the cube so it can be replayed later.
 *
 * @see this.move
 */
@Slf4j
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    /**
     * This array represents the action taken by the player during a game.
     * <p>
     *     The format of this array is the following: <b>[m, p1, p2, p3]</b>, where 'm' stands for kind
     *     of action taken by the player, and 'p1', 'p2' and 'p3' stands for the parameters of the action.
     * </p>
     *
     * <p>
     *     Possible values for 'm' are:
     *     <ul>
     *         <li>0: Permutar</li>
     *         <li>1: Intercambiar</li>
     *         <li>2: Reducir</li>
     *         <li>3: Rotar</li>
     *         <li>4: Mover</li>
     *     </ul>
     * </p>
     *
     * <p>
     *     Params will be those that the action needs, leaving as 0 those that are not needed. Just in case
     *     a moving cube action is taken, the params have to be dooubles from 0 to 1. For the rest of the actions,
     *     params have to be casted to integers.
     * </p>
     */
    private double[] action = new double[4];

    /** This constructor is used to create a Move object from a string.
     * @param action String representing the move in the format [m, p1, p2, p3]
     */
    public Action(String action) {
        action = action.trim();

        if (action.charAt(0) == '[' && action.endsWith("]")) {
            action = action.substring(1, action.length() - 1);
        } else {
            log.error("Move string does not start with '[' or end with ']'. MOVE is: ---" + action + "---");
        }

        String[] acctionArray = action.split(",");
        for (int i = 0; i < acctionArray.length; i++) {
            this.action[i] = Double.parseDouble(acctionArray[i]);
        }
    }

    public Action(double actionDesc, double param1, double param2, double param3) {
        this.action[0] = actionDesc;
        this.action[1] = param1;
        this.action[2] = param2;
        this.action[3] = param3;
    }

    @Override
    public String toString() {
        if (this.getKindOfAction() == 4)
            return "[" + (int) action[0] + "," + action[1] + "," + action[2] + "," + action[3] + ']';
        else
            return "[" + (int) action[0] + "," + (int) action[1] + "," + (int) action[2] + "," + (int) action[3] + ']';
    }

    public int getKindOfAction() {
        return (int) action[0];
    }
}
