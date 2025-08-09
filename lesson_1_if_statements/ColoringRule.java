// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

import java.awt.Color;

public class ColoringRule {

    // Number of rows and columns in the grid.
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLUMNS = 10;

    /*
     * Given a row index from i in [0, 1, ..., N-1] and column index from j in [0,
     * 1, ..., M-1], returns a color in RGB format. Recall that RGB values range
     * from [0->255] or [0->2^8 - 1] in other words, red in [0, 1, ... 255], green
     * in [0, 1, ... 255], blue in [0, 1, ..., 255]. Also recall that (0, 0, 0) ==
     * black and (255, 255, 255) is white. Use a website like
     * https://htmlcolorcodes.com/ to get color codes.
     */
    public static Color apply(int row_index, int column_index) {
        return applyAllRed(row_index, column_index);
        // return applyAllGreen(row_index, column_index);
        // return applyAllBlue(row_index, column_index);
        // return applyAllYellow(row_index, column_index);
        // return applyAllMagenta(row_index, column_index);
        // return applyAllCyan(row_index, column_index);
        // return applyAllWhite(row_index, column_index);
        // return applyAllBlack(row_index, column_index);
        // return applyVerticalPattern(row_index, column_index);
        // return applyHorizontalPattern(row_index, column_index);
        // return applyDiagonalPattern(row_index, column_index);
        // return applyCrossPattern(row_index, column_index);
        // return applyCheckerboardPattern(row_index, column_index);
        // return applyMonsterPattern(row_index, column_index);
        // return applyKForKrunch(row_index, column_index);
    }

    /* This function should make all the squares red. */
    public static Color applyAllRed(int row_index, int column_index) {
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares green. */
    public static Color applyAllGreen(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares blue. */
    public static Color applyAllBlue(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares yellow. */
    public static Color applyAllYellow(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares magenta. */
    public static Color applyAllMagenta(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares cyan. */
    public static Color applyAllCyan(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares white. */
    public static Color applyAllWhite(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make all the squares black. */
    public static Color applyAllBlack(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make every *even* numbered column index red
     * and every *odd* numbered column index blue. 
     * In other words: column 1 (index 0) is red, column 2 (index 1) is blue, column 3 (index 2) is red...
     * and so forth. 
     */
    public static Color applyVerticalPattern(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make every *even* numbered row index yellow
     * and every *odd* numbered row index magenta. 
     * In other words: row 1 (index 0) is yellow, row 2 (index 1) is magenta, row 3 (index 2) is yellow...
     * and so forth. 
     */
    public static Color applyHorizontalPattern(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make a diagonal line from the top-left of the screen to the
     * bottom-right of the screen. The line should be red, and the background should be black.
     * Can you figure it out???
     * Hint: Draw out the grid on paper, and write out the (row_index, column_index) pairs
     *   for the diagonal boxes. Do you notice a pattern?
     */
    public static Color applyDiagonalPattern(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make a red "X" in the middle of the screen.
     * Think about the diagonal red line from the last example. How do you need
     * to modify your previous strategy to account for a second line?
     * Hint: Sometimes to solve a hard problem at scale, we have to shrink it down
     * to make it easier to think about. Consider first a 3x3 grid. Draw out the
     * (row_index, column_index) in each cell. Think about what happens when you sum
     * row_index + column_index. Do any patterns arise?
     */
    public static Color applyCrossPattern(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should make the screen look like a checkerboard!
     * Here is an example: https://boxercrafthouse.com/products/checkerboard-1-2-black.
     * Can you figure it out???
     * Hint: You will need to think about both the row AND the column this time.
     */
    public static Color applyCheckerboardPattern(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function is the "Monster" pattern. It's a frankenstein of 4 other patterns
     * we've made already! Here's how it works:
     * We break up the grid into 4 quadrants
     * ----------------------------
     * |            |             |
     * |     1      |      2      |
     * |            |             |
     * |--------------------------|
     * |            |             |
     * |     3      |      4      |
     * |            |             |
     * ----------------------------
     *  In quadrant 1, we will draw using the applyVerticalPattern rule.
     *  In quadrant 2, we will draw using the applyHorizontalPattern rule.
     *  In quadrant 3, we will draw using the applyCheckerboardPattern rule.
     *  In quadrant 4, we will draw using the applyDiagonalPattern rule.
     *  
     *  Wow, that's a lot of code! If only there was a way to re-use what we already wrote...
     *  Hint: No copy/pasting large chunks of code!!!
     */
    public static Color applyMonsterPattern(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }

    /* This function should draw the letter "K" as in "Krunch Time!" 
     * It can be any K you want, as long as it's a "K". Your image
     * should also have the iconic Krunch Blue either as the background
     * or as the font color of the letter "K".
     * 
     * Hint: There is no mathematically elegant way to encode the letter "K".
     *  You might need quite a few lines to make this look the way you want.
     *  That's ok. In practice we use something called font rasterization
     *  to make the process easier https://en.wikipedia.org/wiki/Font_rasterization.
     *  Please do not implement this though until you're in college!!!
     *  (unless you're really bored, idk, I can't stop you *shrugs*)
    */
    public static Color applyKForKrunch(int row_index, int column_index) {
        // TODO: YOUR CODE SHOULD REPLACE THE BELOW.
        return new Color(255, 0, 0);
    }
}

