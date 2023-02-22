public class Lvl0Bird extends AbstractBird {

    private static final String LVL0_BIRD_WING_UP = "res/level-0/birdWingUp.png";
    private static final String LVL0_BIRD_WING_DOWN = "res/level-0/birdWingDown.png";
    private static final int MAX_LIVES = 3;

    /**
     * Constructor for the bird used in level 0 of ShadowFlap.
     */
    public Lvl0Bird() {
        super(LVL0_BIRD_WING_UP, LVL0_BIRD_WING_DOWN, MAX_LIVES);
    }
}
