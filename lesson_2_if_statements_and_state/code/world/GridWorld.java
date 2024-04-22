package code.world;

import javax.swing.*;

import code.StudentPolicy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

public class GridWorld extends JFrame {
    private static final int GRID_SIZE = 12;
    private static final int CELL_SIZE = 40; // Each cell is 40x40 pixels
    private static final int UPDATE_ENV_INTERVAL = 50;
    private static final int POLICY_INTERVAL = 500;
    private static final int FRAME_WIDTH_EXTRA = 80;
    private static final int FRAME_HEIGHT_EXTRA = 40;
    private static final int TEXT_OFFSET_Y = 10;
    private static final int TEXT_OFFSET_X = 120;
    private long randomSeed; // Fixed seed for reproducibility
    private CellState[][] grid;
    private int[][] humanPosition;
    private int roombaX = 1; // Start at bottom left, leave space for a border
    private int roombaY = 1;
    private Direction direction = Direction.NORTH;
    private Random random;
    private RobotImpl robot;
    private String title;
    private Policy policy = new StudentPolicy();
    private int numSteps = 0;

    // Sensor and detection states for display
    private boolean leftSensorTripped, rightSensorTripped, frontSensorTripped, backSensorTripped, personDetected,
            signaledForHelp, foundHuman, paused;

    enum CellState {
        FREE, WALL, PERSON
    }

    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    public GridWorld(String title, long randomSeed, int[][] humanPosition) {
        this.title = title;
        this.randomSeed = randomSeed;
        this.humanPosition = humanPosition;
        random = new Random(this.randomSeed); // Random generator with seed

        robot = new RobotImpl();

        grid = new CellState[GRID_SIZE][GRID_SIZE];
        generateMaze(humanPosition);
        setupFrame();
        new Timer(UPDATE_ENV_INTERVAL, e -> update()).start();
        new Timer(POLICY_INTERVAL, e -> runPolicy()).start();
    }

    private void generateMaze(int[][] humanPosition) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = CellState.WALL; // Initialize all cells as walls
            }
        }

        // Start DFS from the bottom-left corner
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(1, 1));
        grid[1][1] = CellState.FREE;

        while (!stack.isEmpty()) {
            Point current = stack.peek();
            Point next = getNextCell(current);
            if (next != null) {
                grid[next.x][next.y] = CellState.FREE;
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        grid[humanPosition[0][0]][humanPosition[0][1]] = CellState.PERSON;
    }

    private Point getNextCell(Point current) {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }; // East, West, North, South
        Collections.shuffle(Arrays.asList(directions), random); // Randomize directions

        for (int[] direction : directions) {
            int nx = current.x + direction[0] * 2; // Move two steps in the direction
            int ny = current.y + direction[1] * 2; // Move two steps in the direction

            if (nx >= 1 && nx < GRID_SIZE - 1 && ny >= 1 && ny < GRID_SIZE - 1 && grid[nx][ny] == CellState.WALL) {
                grid[current.x + direction[0]][current.y + direction[1]] = CellState.FREE; // Knock down the wall
                                                                                           // between cells
                return new Point(nx, ny);
            }
        }
        return null;
    }

    private void setupFrame() {
        setTitle("Grid World");
        setSize(GRID_SIZE * CELL_SIZE + FRAME_WIDTH_EXTRA, GRID_SIZE * CELL_SIZE + FRAME_HEIGHT_EXTRA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridPanel panel = new GridPanel();
        getContentPane().add(panel);
        setupKeyBindings(panel);
        setVisible(true);
    }

    private void setupKeyBindings(JPanel panel) {
        // Define the key stroke for the space bar
        KeyStroke spaceKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);

        // Define the action to be performed when the space bar is pressed
        Action spaceAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = !paused;
            }
        };

        // Get the input map of the panel to map key strokes to action keys
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the action map of the panel to map action keys to actions
        ActionMap actionMap = panel.getActionMap();

        // Bind the space bar key stroke to an action key (identifier)
        inputMap.put(spaceKeyStroke, "spaceAction");
        // Bind the action key to the action
        actionMap.put("spaceAction", spaceAction);
    }

    private void update() {
        boolean[] sensorReadings = simulateSensors();
        personDetected = grid[roombaX][roombaY] == CellState.PERSON;
        leftSensorTripped = sensorReadings[0];
        rightSensorTripped = sensorReadings[1];
        frontSensorTripped = sensorReadings[2];
        backSensorTripped = sensorReadings[3];
        robot.updateSensors(leftSensorTripped, rightSensorTripped, frontSensorTripped, backSensorTripped, personDetected);
        repaint();
    }

    private void runPolicy() {
        if (paused) {
            return;
        }

        policy.execute(robot);

        if (!foundHuman) {
            numSteps++;
        }

        boolean atHumanPos = roombaX == humanPosition[0][0] && roombaY == humanPosition[0][1];
        if (robot.didSignalForHelp() && !signaledForHelp && atHumanPos) {
            foundHuman = true;
        }
        signaledForHelp = robot.didSignalForHelp();

        final Optional<RobotImpl.CommandType> command = robot.pop();
        if (command.isEmpty()) {
            return;
        }

        switch (command.get()) {
            case MOVE_FORWARD:
                moveForward();
                break;
            case ROTATE_LEFT:
                rotateLeft();
                break;
            case ROTATE_RIGHT:
                rotateRight();
                break;
            default:
                throw new RuntimeException("Unexpected case.");
        }
    }

    private boolean[] simulateSensors() {
        boolean leftSensorTripped = false, rightSensorTripped = false, frontSensorTripped = false, backSensorTripped = false;
        switch (direction) {
            case NORTH:
                leftSensorTripped = isWall(roombaX - 1, roombaY);
                rightSensorTripped = isWall(roombaX + 1, roombaY);
                frontSensorTripped = isWall(roombaX, roombaY + 1);
                backSensorTripped = isWall(roombaX, roombaY - 1);
                break;
            case EAST:
                leftSensorTripped = isWall(roombaX, roombaY + 1);
                rightSensorTripped = isWall(roombaX, roombaY - 1);
                frontSensorTripped = isWall(roombaX + 1, roombaY);
                backSensorTripped = isWall(roombaX - 1, roombaY);
                break;
            case SOUTH:
                leftSensorTripped = isWall(roombaX + 1, roombaY);
                rightSensorTripped = isWall(roombaX - 1, roombaY);
                frontSensorTripped = isWall(roombaX, roombaY - 1);
                backSensorTripped = isWall(roombaX, roombaY + 1);
                break;
            case WEST:
                leftSensorTripped = isWall(roombaX, roombaY - 1);
                rightSensorTripped = isWall(roombaX, roombaY + 1);
                frontSensorTripped = isWall(roombaX - 1, roombaY);
                backSensorTripped = isWall(roombaX + 1, roombaY);
                break;
        }
        return new boolean[] { leftSensorTripped, rightSensorTripped, frontSensorTripped, backSensorTripped };
    }

    private boolean isWall(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE && grid[x][y] == CellState.WALL;
    }

    private void moveForward() {
        int nextX = roombaX;
        int nextY = roombaY;
        switch (direction) {
            case NORTH -> nextY++;
            case EAST -> nextX++;
            case SOUTH -> nextY--;
            case WEST -> nextX--;
        }
        if (nextX >= 0 && nextX < GRID_SIZE && nextY >= 0 && nextY < GRID_SIZE
                && grid[nextX][nextY] != CellState.WALL) {
            roombaX = nextX;
            roombaY = nextY;
        }
    }

    private void rotateLeft() {
        direction = switch (direction) {
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
        };
    }

    private void rotateRight() {
        direction = switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }

    class GridPanel extends JPanel {
        public GridPanel() {
            setBackground(Color.BLACK); // Set the background color of the panel to black
        }

        public int getTextOffsetX() {
            return getWidth() - TEXT_OFFSET_X;
        }

        public int getTextOffsetY() {
            return TEXT_OFFSET_Y;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int drawY = (GRID_SIZE - 1 - j) * CELL_SIZE; // Invert y-axis for drawing
                    switch (grid[i][j]) {
                        case WALL -> {
                            g.setColor(Color.BLACK);
                            g.fillRect(i * CELL_SIZE, drawY, CELL_SIZE, CELL_SIZE);
                        }
                        case PERSON -> {
                            g.setColor(Color.WHITE);
                            g.fillRect(i * CELL_SIZE, drawY, CELL_SIZE, CELL_SIZE);

                            g.setColor(foundHuman ? Color.GREEN : Color.RED);
                            g.fillOval(i * CELL_SIZE, drawY, CELL_SIZE, CELL_SIZE);

                            // Draw black border around the cell
                            g.setColor(Color.BLACK);
                            g.drawRect(i * CELL_SIZE, drawY, CELL_SIZE, CELL_SIZE);
                        }
                        case FREE -> {
                            g.setColor(Color.WHITE);
                            g.fillRect(i * CELL_SIZE, drawY, CELL_SIZE, CELL_SIZE);

                            // Draw black border around the cell
                            g.setColor(Color.BLACK);
                            g.drawRect(i * CELL_SIZE, drawY, CELL_SIZE, CELL_SIZE);
                        }
                        default -> {
                            throw new RuntimeException("Reach invalid case.");
                        }
                    }
                }
            }
            // Draw roomba
            int roombaDrawY = (GRID_SIZE - 1 - roombaY) * CELL_SIZE; // Invert y-axis for Roomba
            g.setColor(Color.BLUE);
            g.fillOval(roombaX * CELL_SIZE, roombaDrawY, CELL_SIZE, CELL_SIZE);

            // Draw the pie wedges to indicate direction and sensor triggers
            int startAngle = calculateStartAngle();
            g.setColor(frontSensorTripped ? Color.GREEN : Color.RED); // Color for the pie wedge
            g.fillArc(roombaX * CELL_SIZE, roombaDrawY, CELL_SIZE, CELL_SIZE, startAngle, 90); // Draw 90-degree arc

            // Right sensor indicator
            g.setColor(rightSensorTripped ? Color.GREEN : Color.RED);
            g.fillArc(roombaX * CELL_SIZE, roombaDrawY, CELL_SIZE, CELL_SIZE, startAngle - 68, 45);

            // Left sensor indicator
            g.setColor(leftSensorTripped ? Color.GREEN : Color.RED);
            g.fillArc(roombaX * CELL_SIZE, roombaDrawY, CELL_SIZE, CELL_SIZE, startAngle + 112, 45);

            // Back sensor indicator
            g.setColor(backSensorTripped ? Color.GREEN : Color.RED);
            g.fillArc(roombaX * CELL_SIZE, roombaDrawY, CELL_SIZE, CELL_SIZE, startAngle + 202, 45);

            // Display sensor states
            g.setColor(Color.ORANGE);
            g.drawString(title, 20, 30);

            g.drawString("Left: " + (leftSensorTripped ? "Wall" : "Clear"), getTextOffsetX(), getTextOffsetY() + 20);
            g.drawString("Right: " + (rightSensorTripped ? "Wall" : "Clear"), getTextOffsetX(), getTextOffsetY() + 35);
            g.drawString("Front: " + (frontSensorTripped ? "Wall" : "Clear"), getTextOffsetX(), getTextOffsetY() + 50);
            g.drawString("Back: " + (backSensorTripped ? "Wall" : "Clear"), getTextOffsetX(), getTextOffsetY() + 65);
            g.drawString("Person Detected: " + (personDetected ? "Yes" : "No"), getTextOffsetX(),
                    getTextOffsetY() + 80);
            g.drawString("Direction: " + (direction.toString()), getTextOffsetX(), getTextOffsetY() + 95);
            g.drawString("Wireless Signal: " + signaledForHelp, getTextOffsetX(), getTextOffsetY() + 115);
            g.drawString("Person rescued: " + foundHuman, getTextOffsetX(), getTextOffsetY() + 130);
            g.drawString("Rescue Clock: " + (numSteps * POLICY_INTERVAL / 1000.0) + "s", getTextOffsetX(), getTextOffsetY() + 145);

            g.drawString("Paused: " + paused, getTextOffsetX(), getTextOffsetY() + 170);
            g.drawString("(spacebar)", getTextOffsetX(), getTextOffsetY() + 185);
        }

        private int calculateStartAngle() {
            return switch (direction) {
                case NORTH -> 45; // Up
                case EAST -> -45; // Right
                case SOUTH -> 225; // Down
                case WEST -> 135; // Left
            };
        }
    }
}
