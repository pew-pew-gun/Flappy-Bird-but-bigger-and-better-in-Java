import bagel.*;

/**
 * Abstract class that holds the shared attributes and methods for the individual levels of ShadowFlap.
 */
public abstract class AbstractLevel {
    private final Image background;

    /**
     * Constructor to assign the correct bagel.util.Image to the 'background' attribute for its subclasses.
     * @param filename the correct file path to the background image written as a String
     */
    public AbstractLevel (String filename) {
        this.background = new Image(filename);
    }

    /**
     * Method that draws the background centred with the window.
     */
    public void drawBackground() {
        this.background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }
}
