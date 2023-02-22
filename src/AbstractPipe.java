import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class AbstractPipe {

    private final Image PIPE_IMAGE;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private static final int PIPE_GAP = 168;
    private static final double NORMAL_PIPE_SPEED = 3;
    private static double pipeSpeed = NORMAL_PIPE_SPEED;
    private double pipeX = Window.getWidth();
    private final double GAP_START_Y;
    private final double TOP_PIPE_SPAWN_Y;
    private final double BOTTOM_PIPE_SPAWN_Y;
    private boolean birdHasPassed = false;

    /**
     * Constructor that assigns the correct bagel.util.Image to 'PIPE_IMAGE' and the correct y-coordinates for the
     * top pipe, bottom pipe, and the gap between the pipes.
     * @param filename the correct file path to the pipe image
     * @param gapY y-coordinate of where the start of the gap in the pipes should be
     */
    public AbstractPipe (String filename, double gapY) {
        PIPE_IMAGE = new Image(filename);
        GAP_START_Y = gapY;
        TOP_PIPE_SPAWN_Y = topPipeYFromGapY(GAP_START_Y);
        BOTTOM_PIPE_SPAWN_Y = TOP_PIPE_SPAWN_Y + PIPE_IMAGE.getHeight() + PIPE_GAP;
    }

    //finds the top pipe y coordinate as a function of the gap's starting y coordinate
    private double topPipeYFromGapY(double gapStartY) {
        return gapStartY - PIPE_IMAGE.getHeight() / 2.0;
    }

    //Draws both the pipes with the correct gap wrt gap's starting y coordinate
    private void renderPipes() {
        //Draw top pipe
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_SPAWN_Y);
        //Draw bottom pipe
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_SPAWN_Y, ROTATOR);
    }

    /**
     * Performs a state update and moves the pipes to the left.
     */
    public void updatePipe() {
        renderPipes();
        pipeX -= pipeSpeed;
    }

    /**
     * Getter for 'PIPE_IMAGE'
     * @return bagel.util.Image reference to the pipe image
     */
    public Image getPIPE_IMAGE() {
        return PIPE_IMAGE;
    }

    /**
     * Getter for the x-coordinate of the pipe
     * @return x-coordinate of the pipe
     */
    public double getPipeX() {
        return pipeX;
    }

    /**
     * Method that creates a bagel.util.Rectangle defined by the pipe image centred about the centre of the top pipe.
     * @return bagel.util.Rectangle reference to the Rectangle defined by the pipe image centred about
     * the centre of the top pipe
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_SPAWN_Y));
    }

    /**
     * Method that creates a Rectangle defined by the pipe image centred about the centre of the bottom pipe.
     * @return bagel.util.Rectangle reference to the Rectangle defined by the pipe image centred about
     * the centre of the bottom pipe
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_SPAWN_Y));
    }

    /**
     * Getter for the DrawOptions object used to rotate bagel.util.Images when rendered on screen
     * @return DrawOptions reference to the 'ROTATOR' used to rotate bagel.util.Images when rendered on screen
     */
    public DrawOptions getROTATOR() {
        return ROTATOR;
    }

    /**
     * Getter for the base movement speed of a pipe.
     * @return double Number of pixels per frame at which the pipe moves at (base movement speed)
     */
    public static double getNORMAL_PIPE_SPEED() {
        return NORMAL_PIPE_SPEED;
    }

    /**
     * Setter for the movement speed of a pipe
     * @param pipeSpeed Number of pixels per frame at which you want to set all pipes to move at
     */
    public static void setPipeSpeed(double pipeSpeed) {
        AbstractPipe.pipeSpeed = pipeSpeed;
    }

    /**
     * Getter to determine if the bird has flown past this pipe.
     * @return true if the bird has passed this pipe already
     */
    public boolean isBirdHasPassed() {
        return birdHasPassed;
    }

    /**
     * Setter to inform pipe object of whether the bird has flown past it yet.
     * @param birdHasPassed set to true if the bird has passed this pipe
     */
    public void setBirdHasPassed(boolean birdHasPassed) {
        this.birdHasPassed = birdHasPassed;
    }
}
