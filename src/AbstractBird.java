import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Abstract class that holds the shared attributes and methods for both types of birds in ShadowFlap.
 */
public abstract class AbstractBird extends AbstractObject {
    private final Image WING_DOWN;
    private final Image WING_UP;
    private final Image FULL_HEART = new Image("res/level/fullLife.png");
    private final Image EMPTY_HEART = new Image("res/level/noLife.png");
    private static final int HEART_SPACING = 50;

    private static final double ACCEL_FREE_FALL = 0.4; //pixels per frame^2
    private static final double MAX_BIRD_SPEED_Y = 10;
    private static final double BIRD_X = 200;
    private static final double BIRD_SPAWN_Y = 350;
    private double birdY = BIRD_SPAWN_Y;
    private double birdSpeedY = 0; //pixels per frame, +ve is downwards

    private final Point lifeBarLocation = new Point(100, 15); //Top left coordinate
    private final int MAX_LIVES;
    private int lives;
    private int nthframe = 1;

    /**
     * Constructor that assigns the correct bagel.util.Images to the 'wingUpFile' and 'wingDownFile' attributes and the
     * number of maximum lives each bird has for its subclasses.
     * @param wingUpFile the correct file path to the image that depicts a bird with its wings up
     * @param wingDownFile the correct file path to the image that depicts a bird with its wings down
     * @param lives number of maximum lives a bird has
     */
    public AbstractBird(String wingUpFile, String wingDownFile, int lives) {
        WING_UP = new Image(wingUpFile);
        WING_DOWN = new Image(wingDownFile);
        setBoundingBox(WING_DOWN.getBoundingBoxAt(new Point(BIRD_X, birdY)));
        MAX_LIVES = lives;
        this.lives = MAX_LIVES;
    }
    //Draws the bird with 'wing flap' logic implemented
    private void draw() {
        if (!(nthframe % 10 == 0)) {
            WING_DOWN.draw(BIRD_X, birdY);
            setBoundingBox(WING_DOWN.getBoundingBoxAt(new Point(BIRD_X, birdY)));
        } else {
            //Bird flaps every 10 frames
            WING_UP.draw(BIRD_X, birdY);
            setBoundingBox(WING_UP.getBoundingBoxAt(new Point(BIRD_X, birdY)));
        }
        nthframe++;
    }
    //Draws the bird whilst simulating behaviour under free fall
    private void fall() {
        birdSpeedY = Math.min(birdSpeedY + ACCEL_FREE_FALL, MAX_BIRD_SPEED_Y); //pixels per frame, +ve is downwards
        birdY += birdSpeedY;
        this.draw();
    }
    //Draws the bird whilst simulating flight behaviour
    private void fly() {
        birdSpeedY = -6; //6 pixels per frame, -ve is upwards
        birdY += birdSpeedY;
        this.draw();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Input input) {
        if (input.wasPressed(Keys.SPACE)) {
            fly();
        } else {
            fall();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getBoundingBox() {
        return super.getBoundingBox();
    }

    /**
     * Draws the life bar in the correct location on screen
     */
    public void drawLifeBar() {
        double topLeftOfNthHeart = lifeBarLocation.x;
        double heartY = lifeBarLocation.y;
        //Draw the full hearts
        for(int i = 0; i<lives; i++) {
            drawFullHeart(topLeftOfNthHeart, heartY);
            topLeftOfNthHeart += HEART_SPACING;
        }
        //Draw the empty hearts
        for(int i=0; i<(MAX_LIVES - lives); i++) {
            drawEmptyHeart(topLeftOfNthHeart, heartY);
            topLeftOfNthHeart += HEART_SPACING;
        }
    }
    //Draws a full heart at a given x and y coordinate
    private void drawFullHeart(double x, double y) {
        FULL_HEART.drawFromTopLeft(x, y);
    }
    //Draws an empty heart at a given x and y coordinate
    private void drawEmptyHeart(double x, double y) {
        EMPTY_HEART.drawFromTopLeft(x, y);
    }

    /**
     * Getter for the x-coordinate of the bird.
     * @return double Bird's x-coordinate
     */
    public double getBIRD_X() {
        return BIRD_X;
    }

    /**
     * Getter for the y-coordinate of the bird.
     * @return double Bird's y-coordinate
     */
    public double getBirdY() {
        return birdY;
    }

    /**
     * Setter for the y-coordinate of the bird.
     * @param birdY value that you want to change the bird's y-coordinate to
     */
    public void setBirdY(double birdY) {
        this.birdY = birdY;
    }

    /**
     * Getter for the number of lives remaining that a bird has.
     * @return int Number of remaining lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Setter for the number of lives remaining that a bird has.
     * @param lives change the number of lives remaining to this value
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Getter for the y-coordinate of the bird's initial spawning position.
     * @return y-coordinate of the bird's initial spawning position
     */
    public double getBIRD_SPAWN_Y() {
        return BIRD_SPAWN_Y;
    }
}
