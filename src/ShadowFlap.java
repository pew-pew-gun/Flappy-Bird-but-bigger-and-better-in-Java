import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please fill in your name below
 * @author Bi Ho Shin (1086159)
 *
 * Part of this code was modified from 'SWEN20003 Project 1 solutions' by Betty Lin
 *
 * The main class of the ShadowFlap game. It makes combines all the other classes to produce a working executable
 * program.
 */
public class ShadowFlap extends AbstractGame {

    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final String APPLICATION_NAME = "Shadow Flap";
    private static final int FONT_SIZE = 48;

    private final Font gameFont = new Font("res/font/slkscr.ttf", FONT_SIZE);

    private static final String WIN_MESSAGE = "CONGRATULATIONS!";
    private static final String LOSS_MESSAGE = "GAME OVER";
    private static final String FINAL_SCORE = "FINAL SCORE: ";
    private static final int MESSAGE_AND_SCORE_OFFSET = 75; //pixels
    private static final String START_MESSAGE = "PRESS SPACE TO START";
    private static final String SHOOTING_INSTRUCTIONS = "PRESS 'S' TO SHOOT";
    private static final int MESSAGE_AND_INSTRUCTION_OFFSET = 68; //pixels
    private static final String SCORE_TEXT = "SCORE: ";
    private static final String LVL_UP_MESSAGE = "LEVEL-UP!";
    private static final Point SCORE_LOCATION = new Point(100, 100);

    private final Random random = new Random();

    private final Level0 level0 = new Level0();
    private final Level1 level1 = new Level1();
    private final Lvl0Bird lvl0Bird = new Lvl0Bird();
    private final Lvl1Bird lvl1Bird = new Lvl1Bird();

    private boolean liveGame = false; //Has game started?
    private boolean isLevel0 = false;
    private boolean isLevel1 = false;
    private boolean transition = false;
    private boolean loss = false;
    private boolean win = false;

    private static final int MAX_TIMESCALE = 5;
    private static final int MIN_TIMESCALE = 1;
    private int timescale = 1;
    private static final double TIMESCALE_MULTIPLIER = 1.5; //increase speed by x1.5 for each timescale increase

    private int score = 0;
    private static final int LVL1_THRESHOLD = 10; //when score = 10
    private int transitionEnd;
    private static final int TRANSITION_PERIOD = 20; //frames
    private static final int WIN_THRESHOLD = 30; //winning score

    private int nthFrame = 0;
    private final int NORMAL_PIPE_INTERVAL = 100; //no of frames between each pipe spawn (base rate)
    private int pipeSpawningInterval = NORMAL_PIPE_INTERVAL; //no of frames between each pipe spawn
    private final int NORMAL_WEAPON_INTERVAL = 2 * NORMAL_PIPE_INTERVAL; //no of frames between each weapon spawn (base rate)
    private int weaponSpawningInterval = NORMAL_WEAPON_INTERVAL; //no of frames between each weapon spawn

    private final ArrayList<AbstractPipe> pipes = new ArrayList<>();
    private final ArrayList<AbstractWeapon> weapons = new ArrayList<>();

    private final int SPAWNING_RANGE_Y = 401; //+1 to include y=500
    private final int Y_SPAWN_OFFSET = 100;

    /**
     * Constructs the game in a window of size 1024x768
     */
    public ShadowFlap() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, APPLICATION_NAME);
    }
    /**
     * The entry point for the program. It executes the game.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }
    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        //Exit the game if ESC is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        //Game won
        if (win) {
            liveGame = false;
            loss = false;
            drawWinScreen();
        }
        //Game lost
        if (loss) {
            liveGame = false;
            win = false;
            drawLossScreen();
        }
        //If game has not started and has never been started
        if (!liveGame && nthFrame == 0 && !loss && !win) {
            displayLv0StartingScreen();

            //Checks if game has begun
            if (input.wasPressed(Keys.SPACE)) {
                liveGame = true;
                isLevel0 = true;
            }
        }
        //Logic specific to level 0
        if (liveGame && isLevel0 && !loss && !win) {
            level0.drawBackground();

            //Depicts bird flight behaviour (renders the bird) by performing a state update
            lvl0Bird.update(input);

            //Spawn pipe every 'pipeSpawningInterval' no of frames
            if (nthFrame % pipeSpawningInterval == 0) {
                //Spawn a lvl 0 pipe and add to the ArrayList of pipes
                spawnLvl0Pipes();
            }
        }
        //Lvl 0 completed and lvl 1 initiated but game has not started yet
        if (isLevel1 && !liveGame && !loss && !win) {
            displayLv1StartingScreen(input);
        }
        //Logic specific to level 1
        if (liveGame && isLevel1 && !loss && !win) {
            level1.drawBackground();

            //Depicts bird flight behaviour and renders any held weapons by performing a state update
            lvl1Bird.update(input);

            //Spawn pipe every 'pipeSpawningInterval' no of frames
            if (nthFrame % pipeSpawningInterval == 0) {
                //Spawn a lvl 1 pipe (according to Lvl 1 logic) and add to the ArrayList of pipes
                spawnLvl1Pipes();
            }

            //Spawn a weapon pickup every 2 pipes (NB. weaponSpawningInterval = 2 * pipeSpawningInterval) with an
            //offset of (weaponSpawningInterval/4) to ensure weapon pickups spawn only in the middle between any 2 pipes.
            //(and so pipes and weapons never spawn on top of each other)
            if (nthFrame % weaponSpawningInterval == weaponSpawningInterval/4) {
                //Spawn weapon and add to the ArrayList of weapons
                spawnWeapons();
            }

            //Weapons in level 1
            for(int i=weapons.size() - 1; i>=0; i--) {

                AbstractWeapon weapon = weapons.get(i);
                //Move pickups to the left or move activated/fired weapons to the right by performing a state update
                weapon.update(input);

                //Carry out all checks necessary for an active/live/fired weapon
                if (weapon.isActive()) {
                    //Check if active weapon has exceeded its range
                    if (weapon.getWeaponX() >= (weapon.getActivationPt().x + weapon.getWEAPON_RANGE())) {
                        weapons.remove(i);
                    } else {
                        //Check for collision with a pipe
                        for (int j = pipes.size() - 1; j >= 0; j--) {
                            AbstractPipe pipe = pipes.get(j);
                            //If a collision between a pipe and a weapon of some sort occurs
                            if (pipeCollision(pipe, weapon.getBoundingBox())) {
                                if (weapon instanceof Bomb || pipe instanceof PlasticPipe) {
                                    //Destroy pipe when necessary conditions are satisfied
                                    pipes.remove(j);
                                    //Increment score
                                    score++;
                                }
                                //Weapons are always destroyed upon collision with any type of pipe
                                weapons.remove(i);
                                //No need to check the rest of the pipes since only 1 instance of a weapon exists at a time
                                break;
                            }
                        }
                    }
                //If not active AND if bird has no weapon, check for collision between bird and weapon pickups
                } else if (lvl1Bird.getHeldWeapon() == null && lvl1Bird.collisionWith(weapon)) {
                    //Notify both objects
                    weapon.setHeldByBird(true);
                    lvl1Bird.setHeldWeapon(weapon);
                }
            }
        }
        //General logic for a live game
        if (liveGame && !loss && !win) {
            timescaleControl(input);
            //Keep count of the number of frames passed so far
            nthFrame++;
            for(int i=pipes.size() - 1; i>=0; i--) {
                AbstractPipe pipe = pipes.get(i);
                //Move pipes to the left (and render flames if needed) by performing state update
                pipe.updatePipe();

                //Check for collision between bird and pipe
                if (pipeCollision(pipe, getBirdBox())) {
                    //Destroy pipe and lose a life
                    pipes.remove(i);
                    int currentLives = getCorrectBird().getLives();
                    --currentLives;
                    getCorrectBird().setLives(currentLives);
                }

                //Check for collision between bird and flame
                if (pipe instanceof SteelPipe) {
                    if (((SteelPipe) pipe).flameCollision(getCorrectBird())) {
                        //Destroy pipe and lose a life
                        pipes.remove(i);
                        int currentLives = getCorrectBird().getLives();
                        --currentLives;
                        getCorrectBird().setLives(currentLives);
                    }
                }
                //Update score everytime a bird passes a pipe
                score += scoreUpdater(getCorrectBird(), pipe);
            }

            drawScoreCounter();
            drawLifeBar();
            checkLoss();
            checkWin();
            checkOutOfBounds();

            //Remove pipes once they reach the LHS of window to save resources
            for(int i=pipes.size() - 1; i>=0; i--) {
                if (pipes.get(i) != null && pipes.get(i).getPipeX() < -pipes.get(i).getPIPE_IMAGE().getWidth()) {
                    pipes.remove(i);
                }
            }

            //Remove weapon pickups once they reach the LHS of window to save resources
            for(int i=weapons.size() - 1; i>=0; i--) {
                if (weapons.get(i) != null && weapons.get(i).getWeaponX() < -weapons.get(i).getWEAPON_IMAGE().getWidth()) {
                    weapons.remove(i);
                }
            }
        }
        //When score threshold is reached at the end of level 0
        if (score == LVL1_THRESHOLD && isLevel0 && !loss && !win) {
            transition = true;
            liveGame = false;
            isLevel0 = false;
            pipes.clear();
            transitionEnd = nthFrame + TRANSITION_PERIOD;
            resetTimescaleAndScore();
        }
        //During level-up transition/intermission
        if (transition && !loss && !win) {
            displayTransitionScreen();

            //Display level-up screen for 20 frames
            if (nthFrame == transitionEnd) {
                transition = false;
                isLevel1 = true;
            }
            nthFrame++;
        }
    }
    /**
     * Checks if the bird is out of bounds.
     * If so, deduct a life and respawn bird back at original spawning location.
     */
    public void checkOutOfBounds() {
        AbstractBird bird = getCorrectBird();
        if (bird.getBirdY() > Window.getHeight() || bird.getBirdY() < 0) {
            bird.setBirdY(bird.getBIRD_SPAWN_Y());
            bird.setLives(bird.getLives() - 1);
        }
    }
    /**
     * Checks the game state to see if a win condition is met.
     * So far there is only 1 win condition and that is when the score reaches 30.
     */
    public void checkWin() {
        if (score >= WIN_THRESHOLD) {
            win = true;
        }
    }
    /**
     * Checks the game state to see if a loss condition is met.
     * So far there is only 1 loss condition and that is when the bird has 0 lives remaining.
     */
    public void checkLoss() {
        if (getCorrectBird().getLives() <= 0) {
            loss = true;
        }
    }
    /**
     * Resets the timescale back to 1 and the score back to 0.
     * Only used when the game changes/updates from level 0 to level 1.
     */
    public void resetTimescaleAndScore() {
        AbstractPipe.setPipeSpeed(AbstractPipe.getNORMAL_PIPE_SPEED());
        AbstractWeapon.setPickupSpeed(AbstractWeapon.getNORMAL_PICKUP_SPEED());
        pipeSpawningInterval = NORMAL_PIPE_INTERVAL;
        weaponSpawningInterval = NORMAL_WEAPON_INTERVAL;
        score = 0;
    }
    /**
     * Changes the timescale which affects the movement speeds and spawning rates of any entity that moves from right
     * to left in the game (i.e. all pipes and weapon pickups).
     * The spawning rates are adjusted accordingly to ensure a consistent gap between all affected entities across
     * all possible timescales.
     * Each increment of the timescale speeds up the movement of all entities by a factor of 1.5.
     * @param input allows method to determine if user has changed the timescale. L increases and K decreases
     *              the timescale by 1, up to a maximum timescale of 5 and minimum timescale of 1 inclusive.
     */
    public void timescaleControl(Input input) {
        boolean update = false;
        //Max, min timescale is 5, 1 inclusive
        if (input.wasPressed(Keys.L)) { //increase timescale by 1
            timescale = Math.min(++timescale, MAX_TIMESCALE);
            update = true;
        }
        if (input.wasPressed(Keys.K)) { //decrease timescale by 1
            timescale = Math.max(--timescale, MIN_TIMESCALE);
            update = true;
        }
        if (update) {
            double multiplier = Math.pow(TIMESCALE_MULTIPLIER, timescale - 1);
            //Increase movement speeds of entities by a factor of 1.5. This is equivalent to multiplying
            //the base movement speed by (1.5^(timescale-1)).
            AbstractPipe.setPipeSpeed(AbstractPipe.getNORMAL_PIPE_SPEED() * multiplier);
            AbstractWeapon.setPickupSpeed(AbstractWeapon.getNORMAL_PICKUP_SPEED() * multiplier);
            //Modify spawning rate to ensure consistent gaps across timescales
            pipeSpawningInterval = Math.toIntExact(Math.round(NORMAL_PIPE_INTERVAL / multiplier));
            weaponSpawningInterval = 2 * pipeSpawningInterval; //want weapons spawning every 2 pipes
        }
    }
    /**
     * Draws the score counter for the game in the correct location on screen.
     */
    public void drawScoreCounter() {
        gameFont.drawString(SCORE_TEXT + score, SCORE_LOCATION.x, SCORE_LOCATION.y);
    }
    /**
     * Draws the correct life bar for the game in the correct location on screen.
     */
    public void drawLifeBar() {
        if (liveGame && !transition) {
            if (isLevel0) {
                lvl0Bird.drawLifeBar();
            }
            if (isLevel1){
                lvl1Bird.drawLifeBar();
            }
        }
    }
    /**
     * Spawns a random weapon pickup between y-coordinates 100-500 inclusive, only if it does not overlap with an
     * existing pipe, and adds to the ArrayList of weapons.
     */
    public void spawnWeapons() {
        AbstractWeapon newWeapon;
        //Random choice between rock and bomb
        int rockVsBomb = random.nextInt(AbstractWeapon.getNO_WEAPON_TYPES());
        int yCoord = Y_SPAWN_OFFSET + random.nextInt(SPAWNING_RANGE_Y);
        if (rockVsBomb == 0) { //Spawn a rock
            newWeapon = new Rock(yCoord);
        } else { //Spawn a bomb
            newWeapon = new Bomb(yCoord);
        }
        //Only add a weapon if it doesn't intersect with a pipe
        if (noOverlapWithExistingPipes(newWeapon.getBoundingBox())) {
            weapons.add(newWeapon);
        }
    }
    /**
     * Spawns a random type of plastic pipe according to the rules of level 0, only if it does not overlap with an
     * existing pipe, and adds to the ArrayList of pipes.
     */
    public void spawnLvl0Pipes() {
        AbstractPipe newPipe;
        int randomInt = random.nextInt(Level0.getNO_PLASTIC_PIPE_TYPES());
        if (randomInt == 0) { //Spawn a High gap plastic pipe
            newPipe = new PlasticPipe(Level0.getHIGH_GAP());
        } else if (randomInt == 1) { //Spawn a Mid gap plastic pipe
            newPipe = new PlasticPipe(Level0.getMID_GAP());
        } else { //Spawn a Low gap plastic pipe
            newPipe = new PlasticPipe(Level0.getLOW_GAP());
        }
        //Only add a new pipe if it doesn't intersect anything else
        if (noOverlapWithExistingPipes(newPipe.getTopBox())) {
            pipes.add(newPipe);
        }
    }
    /**
     * Spawns a random type of pipe with a gap starting at a randomly chosen y-value between 100 and 500 inclusive
     * according to the rules of level 1, only if it does not overlap with an existing pipe, and adds to the
     * ArrayList of pipes.
     */
    public void spawnLvl1Pipes() {
        AbstractPipe newPipe;
        //Randomly choose between plastic or steel
        int plasticVsSteel = random.nextInt(Level1.getNO_PIPE_TYPES());
        int yCoord = Y_SPAWN_OFFSET + random.nextInt(SPAWNING_RANGE_Y);
        if (plasticVsSteel == 0) { //Spawn plastic pipe
            newPipe = new PlasticPipe(yCoord);
        } else { //Spawn steel pipe
            newPipe = new SteelPipe(yCoord);
        }
        //Only add a new pipe if it doesn't intersect anything else
        if (noOverlapWithExistingPipes(newPipe.getTopBox())) {
            pipes.add(newPipe);
        }
    }
    /**
     * Checks whether an object (of type Bagel.util.Rectangle) overlaps with any of the existing pipes.
     * @param box object in question
     * @return true if the object in question does not overlap with any of the existing pipes
     */
    public boolean noOverlapWithExistingPipes(Rectangle box) {
        for (AbstractPipe pipe: pipes) {
            if (box.intersects(pipe.getTopBox())) {
                return false;
            }
            if (box.intersects(pipe.getBottomBox())) {
                return false;
            }
        }
        return true;
    }
    /**
     * Determines the current bird instance used in game and creates a Bagel.util.Rectangle as defined by the image of
     * the current bird, centred about the centre of the bird.
     * @return an instance of bagel.util.Rectangle centred about the centre of the bird. Otherwise, if the game is not
     * in either level 0 or level 1, return null.
     */
    public Rectangle getBirdBox() {
        if (isLevel0) {
            return lvl0Bird.getBoundingBox();
        } else if (isLevel1) {
            return lvl1Bird.getBoundingBox();
        } else {
            return null;
        }
    }
    /**
     * Determines the current bird instance used in game.
     * @return AbstractBird reference to the current bird instance used in game. Otherwise, if the game is not in
     * either level 0 or level 1, return null.
     */
    public AbstractBird getCorrectBird() {
        if (isLevel0) {
            return lvl0Bird;
        } else if (isLevel1) {
            return lvl1Bird;
        } else {
            return null;
        }
    }
    /**
     * Determines if an object (of type bagel.util.Rectangle) is overlapping with a pipe.
     * @param pipe AbstractPipe reference to an instance of a pipe
     * @param box object in question
     * @return true if the object in question overlaps with either the top pipe or the bottom pipe
     */
    public boolean pipeCollision(AbstractPipe pipe, Rectangle box) {
        Rectangle topPipeBox = pipe.getTopBox();
        Rectangle bottomPipeBox = pipe.getBottomBox();
        return box.intersects(topPipeBox) || box.intersects(bottomPipeBox);

    }
    /**
     * Determines whether the bird has just recently passed the pipe (i.e. whether the state of the bird has changed
     * with respect to the pipe in question in the current update loop). If the pipe was already passed by the bird
     * earlier or if the pipe is yet to be passed (and is still yet to be passed) then no state change has occurred.
     * @param bird AbstractBird reference to an instance of a bird
     * @param pipe AbstractPipe reference to an instance of a pipe
     * @return 1 if the bird has just recently passed the pipe (this will allow the score to increment by 1).
     * Otherwise, return 0.
     */
    public int scoreUpdater(AbstractBird bird, AbstractPipe pipe) {
        if (bird != null) {
            //If bird has never passed the pipe so far, but has just recently passed it (in this current update loop)
            if (!pipe.isBirdHasPassed() && Double.compare(bird.getBIRD_X(), pipe.getTopBox().right()) > 0) {
                pipe.setBirdHasPassed(true);
                return 1;
            }
        }
        return 0; //No state change wrt bird's location and pipe's location has occurred
    }
    /**
     * Renders the starting message for the game in the centre of the window.
     */
    public void displayLv0StartingScreen() {
        level0.drawBackground();
        /* Have to adjust by (- gameFont.getWidth() / 2, + FONT_SIZE / 2) to centre the message in the window,
           since drawString() draws from the bottom-left corner */
        double correctedX = (Window.getWidth() / 2.0) - (gameFont.getWidth(START_MESSAGE) / 2.0);
        double correctedY = (Window.getHeight() / 2.0) + (FONT_SIZE / 2.0);
        gameFont.drawString(START_MESSAGE, correctedX, correctedY);
    }
    /**
     * Renders the starting message for level 1 of the game in the centre of the window and determines if the player
     * has pressed SPACE (i.e. is ready to start level 1).
     * @param input allows method to determine if the player has pressed SPACE to indicate to program that they are
     *              ready to start level 1
     */
    public void displayLv1StartingScreen(Input input) {
        level1.drawBackground();
        double correctedCentreX = (Window.getWidth() / 2.0) - (gameFont.getWidth(START_MESSAGE) / 2.0);
        double correctedCentreY = (Window.getHeight() / 2.0) + (FONT_SIZE / 2.0);
        double correctedX = (Window.getWidth() / 2.0) - (gameFont.getWidth(SHOOTING_INSTRUCTIONS) / 2.0);
        double correctedY = correctedCentreY + MESSAGE_AND_INSTRUCTION_OFFSET;
        gameFont.drawString(START_MESSAGE, correctedCentreX, correctedCentreY);
        gameFont.drawString(SHOOTING_INSTRUCTIONS,correctedX, correctedY);

        if (input.wasPressed(Keys.SPACE)) {
            liveGame = true;
        }
    }
    /**
     * Renders the Level-up message in the centre of the window on screen.
     */
    public void displayTransitionScreen() {
        level0.drawBackground();
        double correctedX = (Window.getWidth() / 2.0) - (gameFont.getWidth(LVL_UP_MESSAGE) / 2.0);
        double correctedY = (Window.getHeight() / 2.0) + (FONT_SIZE / 2.0);
        gameFont.drawString(LVL_UP_MESSAGE, correctedX, correctedY);
    }
    /**
     * Renders the Game Over message in the centre of the window with the final score displayed below it.
     */
    public void drawLossScreen() {
        double correctedCentreX = (Window.getWidth() / 2.0) - (gameFont.getWidth(LOSS_MESSAGE) / 2.0);
        double correctedCentreY = (Window.getHeight() / 2.0) + (FONT_SIZE / 2.0);
        double correctedX = (Window.getWidth() / 2.0) - (gameFont.getWidth(FINAL_SCORE + score) / 2.0);
        double correctedY = correctedCentreY + MESSAGE_AND_SCORE_OFFSET;

        //Draw correct background wrt correct level
        if (isLevel0) { //level 0
            level0.drawBackground();
        } else { // level 1
            level1.drawBackground();
        }
        gameFont.drawString(LOSS_MESSAGE, correctedCentreX, correctedCentreY);
        gameFont.drawString(FINAL_SCORE + score, correctedX, correctedY);
        }
    /**
     * Renders the Congratulations message in the centre of the window with the final score displayed below it.
     */
    public void drawWinScreen() {
        double correctedCentreX = (Window.getWidth() / 2.0) - (gameFont.getWidth(WIN_MESSAGE) / 2.0);
        double correctedCentreY = (Window.getHeight() / 2.0) + (FONT_SIZE / 2.0);
        double correctedX = (Window.getWidth() / 2.0) - (gameFont.getWidth(FINAL_SCORE + score) / 2.0);
        double correctedY = correctedCentreY + MESSAGE_AND_SCORE_OFFSET;

        //draw correct background wrt correct level
        if (isLevel0) { // level 0
            level0.drawBackground();
        } else { //level 1
            level1.drawBackground();
        }
        gameFont.drawString(WIN_MESSAGE, correctedCentreX, correctedCentreY);
        gameFont.drawString(FINAL_SCORE + score, correctedX, correctedY);
    }
}

