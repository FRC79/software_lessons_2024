package code;
import code.world.GridWorld;

public class Level4 {

    private static final String TITLE = "Level 4";
    private static final int SEED = 79;
    private static final int[][] HUMAN_POSITION = new int[][] { { 3, 7 } };

    public static void main(String args[]) {
        new GridWorld(TITLE, SEED, HUMAN_POSITION);
    }
}
