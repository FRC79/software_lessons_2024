// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

package code;

/**
 * The Robot interface defines the core functionalities of a robot
 * navigating a grid environment. This includes movement capabilities
 * and sensor status checks to interact with the surroundings.
 * Implementations of this interface should provide detailed logic
 * for each action and sensor check based on the specific type of robot
 * and the environment it operates in.
 */
public interface Robot {
    
    /**
     * Moves the robot one unit forward in the direction it is currently facing.
     * The movement can be blocked if there is an obstacle directly in front of the robot.
     */
    public void moveForward();

    /**
     * Rotates the robot 90 degrees to the left without changing its position.
     */
    public void rotateLeft();

    /**
     * Rotates the robot 90 degrees to the right without changing its position.
     */
    public void rotateRight();

    /**
     * Checks if the left sensor is tripped, which typically indicates the presence
     * of an obstacle or wall immediately to the robot's left side.
     *
     * @return true if the left sensor is tripped, false otherwise.
     */
    public boolean isLeftSensorTripped();

    /**
     * Checks if the right sensor is tripped, which typically indicates the presence
     * of an obstacle or wall immediately to the robot's right side.
     *
     * @return true if the right sensor is tripped, false otherwise.
     */
    public boolean isRightSensorTripped();

    /**
     * Checks if the front sensor is tripped, which typically indicates the presence
     * of an obstacle or wall directly in front of the robot.
     *
     * @return true if the front sensor is tripped, false otherwise.
     */
    public boolean isFrontSensorTripped();

    /**
     * Checks if the back sensor is tripped, which typically indicates the presence
     * of an obstacle or wall directly in back of the robot.
     *
     * @return true if the back sensor is tripped, false otherwise.
     */
    public boolean isBackSensorTripped();

    /**
     * Checks if a person is detected within the robot's immediate vicinity.
     * This function is crucial for search and rescue operations.
     *
     * @return true if a person is detected while the robot is on the same square as that person, false otherwise.
     */
    public boolean isPersonDetected();

    /**
     * Signals via wireless signals to the operator that a person has been found and requires rescue.
     */
    public void signalForHelp();
}
