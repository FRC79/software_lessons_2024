// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

package code;
import code.world.GridWorld;

public class Level1 {

    private static final String TITLE = "Level 1";
    private static final int SEED = 12346;
    private static final int[][] HUMAN_POSITION = new int[][] { { 3, 1 } };

    public static void main(String args[]) {
        new GridWorld(TITLE, SEED, HUMAN_POSITION);
    }
}
