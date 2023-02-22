import bagel.*;
import bagel.util.Point;

public class Lvl1Bird extends AbstractBird {

    private static final String LVL1_BIRD_WING_UP = "res/level-1/birdWingUp.png";
    private static final String LVL1_BIRD_WING_DOWN = "res/level-1/birdWingDown.png";
    private static final int MAX_LIVES = 6;
    private AbstractWeapon heldWeapon = null;

    /**
     * Constructor for the bird used in level 1 of ShadowFlap.
     */
    public Lvl1Bird() {
        super(LVL1_BIRD_WING_UP, LVL1_BIRD_WING_DOWN, MAX_LIVES);
    }

    /**
     * Getter for the AbstractWeapon reference to the weapon that the bird is currently holding.
     * @return AbstractWeapon reference to the weapon that the bird is currently holding
     */
    public AbstractWeapon getHeldWeapon() {
        return heldWeapon;
    }

    /**
     * Setter for 'heldWeapon' attribute of the bird.
     * @param heldWeapon AbstractWeapon reference to the weapon that you want the bird to now hold.
     */
    public void setHeldWeapon(AbstractWeapon heldWeapon) {
        this.heldWeapon = heldWeapon;
    }

    /**
     * Performs a state update.
     * Additionally, renders the held weapon at the bird's beak.
     * @param input allows method to determine when the player has interacted with the game
     */
    @Override
    public void update(Input input) {
        super.update(input);
        if (heldWeapon != null) {
            //draw the weapon at beak
            heldWeapon.setWeaponY(getBirdY());
            heldWeapon.setWeaponX(getBoundingBox().right());
            heldWeapon.renderWeapon();

            //check for button press 'S'
            if (input.wasPressed(Keys.S)) {//weapon has been fired
                heldWeapon.setActivationPt(new Point(heldWeapon.getWeaponX(), heldWeapon.getWeaponY()));
                heldWeapon = null;
            }
        }
    }
}
