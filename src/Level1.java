/**
 * Concrete class that holds specific information for level 1 of ShadowFlap.
 */
public class Level1 extends AbstractLevel {
    private static final String LV1_BACKGROUND_FILENAME = "res/level-1/background.png";
    private static final int NO_PIPE_TYPES = 2;

    /**
     * Constructor for Level1 that uses the AbstractLevel constructor to assign the correct image to the
     * background for level 1 in game.
     */
    public Level1() {
        super(LV1_BACKGROUND_FILENAME);
    }

    /**
     * Getter that tells you how many pipe types exist in level 1 of ShadowFlap.
     * @return int number of pipe types in level 1
     */
    public static int getNO_PIPE_TYPES() {
        return NO_PIPE_TYPES;
    }
}
