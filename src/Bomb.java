public class Bomb extends AbstractWeapon {

    private static final String BOMB_PNG = "res/level-1/bomb.png";
    private static final int ACTIVE_PERIOD = 50; //frames

    /**
     * Constructor for the bombs used in level 1 of ShadowFlap.
     * @param ySpawn y-coordinate of where the bomb spawns
     */
    public Bomb(double ySpawn) {
        super(BOMB_PNG, ACTIVE_PERIOD, ySpawn);
    }
}
