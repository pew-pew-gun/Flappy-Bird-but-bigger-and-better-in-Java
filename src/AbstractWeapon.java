import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class AbstractWeapon extends AbstractObject {

    private final Image WEAPON_IMAGE;
    private final int WEAPON_RANGE; //pixels
    private static final int WEAPON_SPEED = 5; //pixels per frame
    private double weaponX = Window.getWidth();
    private double weaponY;
    private static final double NORMAL_PICKUP_SPEED = 3; //pixels per frame
    private static double pickupSpeed = NORMAL_PICKUP_SPEED;
    private boolean isActive = false; //has been shot by bird
    private boolean heldByBird = false;
    private Point activationPt;

    private static final int NO_WEAPON_TYPES = 2;

    /**
     * Constructor that assigns the correct bagel.util.Image to 'WEAPON_IMAGE', assigns the correct y-coordinate, and
     * assigns the correct maximum no of frames that a weapon is able to be active for whilst automatically calculating
     * and assigning the maximum no of pixels that that weapon can reach.
     * @param filename the correct file path to the weapon image
     * @param activePeriod maximum no of frames that weapon is able to be active for
     * @param ySpawn y-coordinate of the spawning location of the weapon
     */
    public AbstractWeapon(String filename, int activePeriod, double ySpawn) {
        WEAPON_IMAGE = new Image(filename);
        WEAPON_RANGE = activePeriod * WEAPON_SPEED;
        weaponY = ySpawn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getBoundingBox() {
        if (super.getBoundingBox() == null) {
            return WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
        } else {
            return super.getBoundingBox();
        }
    }

    /**
     * Moves a weapon pickup to the left, and any active/fired weapons to the right by performing a state update.
     * @param input allows method to determine when the player has interacted with the game
     */
    @Override
    public void update(Input input) {
        if (!heldByBird) {
            renderWeapon();
            weaponX -= pickupSpeed;
        } else { //is currently held by a bird
            if (input.wasPressed(Keys.S)) { //check if fired
                isActive = true;
            }
        }
        //travel along firing trajectory
        if (isActive) {
            weaponX += WEAPON_SPEED;
            renderWeapon();
        }
    }

    /**
     * Draws the weapon on screen at its coordinates.
     */
    public void renderWeapon() {
        WEAPON_IMAGE.draw(weaponX, weaponY);
        setBoundingBox(WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY)));
    }

    /**
     * Getter for the x-coordinate of the weapon.
     * @return x-coordinate of the weapon
     */
    public double getWeaponX() {
        return weaponX;
    }

    /**
     * Getter for the bagel.util.Image reference to the image used to depict the weapon.
     * @return bagel.util.Image reference to the image used to depict the weapon
     */
    public Image getWEAPON_IMAGE() {
        return WEAPON_IMAGE;
    }

    /**
     * Getter for the y-coordinate of the weapon.
     * @return y-coordinate of the weapon
     */
    public double getWeaponY() {
        return weaponY;
    }

    /**
     * Setter for the y-coordinate of the weapon.
     * @param weaponY new y-coordinate that you want the weapon to be at
     */
    public void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }

    /**
     * Setter for the x-coordinate of the weapon.
     * @param weaponX new x-coordinate that you want the weapon to be at
     */
    public void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }

    /**
     * Setter for the location at which the weapon is fired by the bird.
     * @param activationPt bagel.util.Point reference to the location at which the weapon is fired by the bird
     */
    public void setActivationPt(Point activationPt) {
        this.activationPt = activationPt;
    }

    /**
     * Getter to know if the weapon has been fired.
     * @return true if the weapon has been fired
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Getter for the location at which the weapon was fired.
     * @return bagel.util.Point reference to the location at which the weapon was fired
     */
    public Point getActivationPt() {
        return activationPt;
    }

    /**
     * Getter for the maximum number of pixels that the weapon can traverse.
     * @return maximum number of pixels that the weapon can traverse
     */
    public int getWEAPON_RANGE() {
        return WEAPON_RANGE;
    }

    /**
     * Setter to inform the weapon whether it is currently being held by a bird.
     * @param heldByBird true if the weapon is being held by a bird
     */
    public void setHeldByBird(boolean heldByBird) {
        this.heldByBird = heldByBird;
    }

    /**
     * Setter the control the speed at which the weapon pickups move to the left
     * @param pickupSpeed no of pixels per frame for weapon pickups to move at
     */
    public static void setPickupSpeed(double pickupSpeed) {
        AbstractWeapon.pickupSpeed = pickupSpeed;
    }

    /**
     * Getter for the base movement speed of the weapon pickups.
     * @return no of pixels per frame that weapon pickups move at (base movement speed)
     */
    public static double getNORMAL_PICKUP_SPEED() {
        return NORMAL_PICKUP_SPEED;
    }

    /**
     * Getter for the number of weapon types that exist in ShadowFlap.
     * @return number of weapon types that exist in ShadowFlap
     */
    public static int getNO_WEAPON_TYPES() {
        return NO_WEAPON_TYPES;
    }
}
