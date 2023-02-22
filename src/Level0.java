/**
 * Concrete class that holds specific information for level 0 of ShadowFlap.
 */
public class Level0 extends AbstractLevel {
    private static final String LV0_BACKGROUND_FILENAME = "res/level-0/background.png";

    private static final int HIGH_GAP = 100;
    private static final int MID_GAP = 300;
    private static final int LOW_GAP = 500;
    private static final int NO_PLASTIC_PIPE_TYPES = 3;

    /**
     * Constructor for Level0 that uses the AbstractLevel constructor to assign the correct image to the
     * background for level 0 in game.
     */
    public Level0() {
        super(LV0_BACKGROUND_FILENAME);

    }

    /**
     * Getter that returns the starting y-coordinate of the pipe gap for high gap plastic pipes.
     * @return int the starting y-coordinate of the pipe gap for high gap plastic pipes
     */
    public static int getHIGH_GAP() {
        return HIGH_GAP;
    }

    /**
     * Getter that returns the starting y-coordinate of the pipe gap for mid gap plastic pipes.
     * @return int the starting y-coordinate of the pipe gap for mid gap plastic pipes
     */
    public static int getMID_GAP() {
        return MID_GAP;
    }

    /**
     * Getter that returns the starting y-coordinate of the pipe gap for low gap plastic pipes.
     * @return int the starting y-coordinate of the pipe gap for low gap plastic pipes
     */
    public static int getLOW_GAP() {
        return LOW_GAP;
    }

    /**
     * Getter that returns the number of different plastic pipe types in level 0.
     * @return int number of different plastic pipe types in level 0
     */
    public static int getNO_PLASTIC_PIPE_TYPES() {
        return NO_PLASTIC_PIPE_TYPES;
    }
}
