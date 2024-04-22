package code;
import code.world.GridWorld;

public class Level3 {

    private static final String TITLE = "Level 3";
    private static final int SEED = 45;
    private static final int[][] HUMAN_POSITION = new int[][] { { 8, 3 } };

    public static void main(String args[]) {
        new GridWorld(TITLE, SEED, HUMAN_POSITION);
    }
}
