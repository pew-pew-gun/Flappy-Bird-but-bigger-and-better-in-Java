public class PlasticPipe extends AbstractPipe {

    private static final String PLASTIC_PIPE_PNG = "res/level/plasticPipe.png";

    /**
     * Constructor for the plastic pipes seen in ShadowFlap.
     * @param gapY y-coordinate of where the start of the gap in the pipes should be
     */
    public PlasticPipe(double gapY) {
        super(PLASTIC_PIPE_PNG, gapY);

    }

}
