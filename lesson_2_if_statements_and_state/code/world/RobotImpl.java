// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

package code.world;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import code.Robot;

public class RobotImpl implements Robot {
    private boolean leftSensorTripped = false;
    private boolean rightSensorTripped = false;
    private boolean frontSensorTripped = false;
    private boolean backSensorTripped = false;
    private boolean personDetected = false;
    private boolean signaledForHelp = false;

    private static final int QUEUE_SIZE = 4;

    public enum CommandType {
        MOVE_FORWARD, ROTATE_LEFT, ROTATE_RIGHT
    };

    private Queue<CommandType> commandQueue;

    public RobotImpl() {
        commandQueue = new LinkedList<CommandType>();
    }

    public void add(CommandType cmd) {
        if (commandQueue.size() < QUEUE_SIZE) {
            commandQueue.add(cmd);
        }
    }

    @Override
    public void moveForward() {
        add(CommandType.MOVE_FORWARD);
    }

    @Override
    public void rotateLeft() {
        add(CommandType.ROTATE_LEFT);
    }

    @Override
    public void rotateRight() {
        add(CommandType.ROTATE_RIGHT);
    }

    @Override
    public boolean isLeftSensorTripped() {
        return leftSensorTripped;
    }

    @Override
    public boolean isRightSensorTripped() {
        return rightSensorTripped;
    }

    @Override
    public boolean isFrontSensorTripped() {
        return frontSensorTripped;
    }

    @Override
    public boolean isBackSensorTripped() {
        return backSensorTripped;
    }

    @Override
    public boolean isPersonDetected() {
        return personDetected;
    }

    @Override
    public void signalForHelp() {
        signaledForHelp = true;
    }

    public boolean didSignalForHelp() {
        return signaledForHelp;
    }

    public void updateSensors(
            boolean leftSensorTripped,
            boolean rightSensorTripped,
            boolean frontSensorTripped,
            boolean backSensorTripped,
            boolean personDetected) {
        this.leftSensorTripped = leftSensorTripped;
        this.rightSensorTripped = rightSensorTripped;
        this.frontSensorTripped = frontSensorTripped;
        this.backSensorTripped = backSensorTripped;
        this.personDetected = personDetected;
    }

    public Optional<CommandType> pop() {
        if (commandQueue.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(commandQueue.poll());
    }

    public String queueToString() {
        String s = "Commands:\n\n";
        if (commandQueue.size() == 0) {
            return s;
        }

        int i = 0;
        for (CommandType c : commandQueue) {
            s += i + ": " + c.toString() + "\n\n";
            i++;
        }
        return s;
    }
}
