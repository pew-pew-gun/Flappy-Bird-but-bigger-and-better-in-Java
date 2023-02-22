import bagel.*;
import bagel.util.Rectangle;

public abstract class AbstractObject {

    private Rectangle boundingBox;

    /**
     * Performs a state update.
     * @param input allows method to determine when the player has interacted with the game
     */
    public abstract void update(Input input);

    /**
     * Getter for the boundingBox attribute common to all subclasses of AbstractObject.
     * @return bagel.util.Rectangle defined by the object/image attribute of the object
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Determines if an object (of type AbstractObject) overlaps with the calling object.
     * @param object object to check intersection with
     * @return true if the object in question overlaps with the calling object
     */
    public boolean collisionWith(AbstractObject object) {
        return this.boundingBox.intersects(object.boundingBox);
    }

    /**
     * Setter for the boundingBox of an AbstractObject
     * @param boundingBox reference to a bagel.util.Rectangle that encloses the AbstractObject
     */
    protected void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }
}
