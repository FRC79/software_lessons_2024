// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

class ColorfulSquaresPanel extends JPanel {
    private final int rows;    // M rows
    private final int cols;    // N columns

    public ColorfulSquaresPanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int squareWidth = getWidth() / cols;    // Width of each square
        int squareHeight = getHeight() / rows;  // Height of each square

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                g.setColor(ColoringRule.apply(row, col));

                // Calculate x and y positions based on row and column indices
                int x = col * squareWidth;
                int y = row * squareHeight;

                // Draw the square
                g.fillRect(x, y, squareWidth, squareHeight);

                // Draw black border for the square
                g.setColor(Color.BLACK);
                g.drawRect(x, y, squareWidth, squareHeight);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Colorful Squares Grid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 720); // 720p resolution

        ColorfulSquaresPanel panel = new ColorfulSquaresPanel(ColoringRule.NUM_ROWS, ColoringRule.NUM_COLUMNS);
        frame.add(panel);

        frame.setVisible(true);
    }
}

