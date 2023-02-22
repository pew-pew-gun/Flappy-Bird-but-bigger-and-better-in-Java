public class Rock extends AbstractWeapon {

    private static final String ROCK_PNG = "res/level-1/rock.png";
    private static final int ACTIVE_PERIOD = 25; //frames

    /**
     * Constructor for the rocks used in level 1 of ShadowFlap.
     * @param ySpawn y-coordinate of where the rock spawns
     */
    public Rock(double ySpawn) {
        super(ROCK_PNG, ACTIVE_PERIOD, ySpawn);
    }
}
