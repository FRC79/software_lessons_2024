// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

package code;
import code.world.GridWorld;

public class Level5 {

    private static final String TITLE = "Level 5";
    private static final int SEED = 5465;
    private static final int[][] HUMAN_POSITION = new int[][] { { 3, 7 } };

    public static void main(String args[]) {
        new GridWorld(TITLE, SEED, HUMAN_POSITION);
    }
}
