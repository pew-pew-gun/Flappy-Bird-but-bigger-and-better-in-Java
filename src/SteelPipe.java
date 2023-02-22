import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SteelPipe extends AbstractPipe {

    private static final String STEEL_PIPE_PNG = "res/level-1/steelPipe.png";
    private static final Image FLAME = new Image("res/level-1/flame.png");
    private static final int FLAME_INTERVAL = 20; //frames
    private static final int FLAME_LENGTH = 3; //frames
    private int frameCount = 0;
    private boolean flameOn = false;

    /**
     * Constructor for the steel pipes seen in ShadowFlap.
     * @param gapY y-coordinate of where the start of the gap in the pipes should be
     */
    public SteelPipe(double gapY) {
        super(STEEL_PIPE_PNG, gapY);
    }

    /**
     * Performs a state update and moves the pipes to the left. Also draws the flames for 'FLAME_LENGTH' no of frames
     * every 'FLAME_INTERVAL' no of frames.
     */
    @Override
    public void updatePipe() {
        super.updatePipe();
        //flame rendering logic
        ++frameCount;
        if (frameCount % (FLAME_INTERVAL + FLAME_LENGTH) <= (FLAME_LENGTH - 1)) {
            flameOn = true;

            Point topFlame = getTopBox().bottomLeft();
            Point bottomFlame = new Point(getBottomBox().topLeft().x, getBottomBox().topLeft().y - FLAME.getHeight());

            FLAME.drawFromTopLeft(topFlame.x, topFlame.y);
            FLAME.drawFromTopLeft(bottomFlame.x, bottomFlame.y, getROTATOR());
        } else {
            flameOn = false;
        }
    }

    /**
     * Method that creates a bagel.util.Rectangle object defined by the flame image, located so that it sits
     * right below the top pipe.
     * @return bagel.util.Rectangle reference to the Rectangle defined by the flame image, located so that it sits
     * right below the top pipe.
     */
    public Rectangle getTopFlameBox() {
        double centreX = getTopBox().bottomLeft().x + (FLAME.getWidth() / 2.0);
        double centreY = getTopBox().bottomLeft().y + (FLAME.getHeight() / 2.0);
        return FLAME.getBoundingBoxAt(new Point(centreX, centreY));
    }

    /**
     * Method that creates a bagel.util.Rectangle object defined by the flame image, located so that it sits
     * right above the bottom pipe.
     * @return bagel.util.Rectangle reference to the Rectangle defined by the flame image, located so that it sits
     * right above the bottom pipe.
     */
    public Rectangle getBottomFlameBox() {
        double centreX = getBottomBox().topLeft().x + (FLAME.getWidth() / 2.0);
        double centreY = getBottomBox().topLeft().y - (FLAME.getHeight() / 2.0);
        return FLAME.getBoundingBoxAt(new Point(centreX, centreY));
    }

    /**
     * Determines if an object (of type AbstractObject) overlaps with the flames of a pipe. Automatically takes into
     * account whether the flames are on or not.
     * @param object object in question
     * @return true if the object in question overlaps with either the top or bottom flames of a pipe
     */
    public boolean flameCollision(AbstractObject object) {
        if (flameOn) {
            Rectangle objectBox = object.getBoundingBox();
            return objectBox.intersects(getBottomFlameBox()) || objectBox.intersects(getTopFlameBox());
        } else {
            return false;
        }
    }
}
