package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Ship;
import asteroids.participants.Debris;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener
{
    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    /** The Alien ship (if one is active) or null (otherwise) */
    private AlienShip alien;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;

    /** Stores list of keyboard presses to be executed every FRAME_INTERAVAL */
    private Set<Integer> keyboardPresses;

    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** The game display */
    private Display display;

    /** Keeps track of users score */
    private int score;

    /** Keeps track which level the user is on */
    private int level;

    /** Number of lives left */
    private int lives;

    /** Represents msec between beats */
    private int beat;

    private long beatTransition;

    private boolean alienShip;

    private boolean beat1;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // sounds = new SoundDemo();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        beatTransition = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Initialize empty set containing user key presses
        keyboardPresses = new HashSet<>();

        level = 1;

        alienShip = false;

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();
    }

    /** Returns the current users score */
    public int getScore ()
    {
        return this.score;
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);

        addParticipant(ship);
        display.setLegend("");
    }

    /**
     * Places 4 + current level asteroids on the screen in alternating corners.
     */
    private void placeAsteroids ()
    {
        for (int i = 0; i < 3 + this.level; i++)
        {
            int corner = i % 4;
            int x = 0;
            int y = 0;

            // random integer position. Range: EDGEOFFSET/2 through EDGE_OFFSET
            int rand = RANDOM.nextInt((EDGE_OFFSET - EDGE_OFFSET / 2) + 1) + EDGE_OFFSET / 2;

            // upper left corner
            if (corner == 0)
            {
                x = rand;
                y = rand;
            }
            // upper right corner
            else if (corner == 1)
            {
                x = SIZE - rand;
                y = rand;
            }
            // bottom left corner
            else if (corner == 2)
            {
                x = rand;
                y = SIZE - rand;
            }
            // bottom right corner
            else
            {
                x = SIZE - rand;
                y = SIZE - rand;
            }
            addParticipant(new Asteroid(RANDOM.nextInt(4), 2, x, y, 3, this));
        }
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
        Participant.expire(ship);
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Reset statistics
        lives = 3;
        score = 0;
        level = 1;

        // Place asteroids
        placeAsteroids();

        // Place the ship
        placeShip();

        // sets beat timer
        this.beat = INITIAL_BEAT;
        this.beat1 = true;
        setBeatTimer(beat);

        // Updates current score to be displayed
        display.setScore("" + this.score);

        // Updates current level to be displayed
        display.setLevel("" + this.level);

        // Updates current lives to be displayed
        display.setLives(lives);

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    public void setBeatTimer (int m)
    {
        beatTransition = System.currentTimeMillis() + m;
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed (Participant p)
    {
        // create debris surrounding ship destruction
        createDebris(p);

        // clear keyBoard presses
        this.keyboardPresses.clear();

        // Null out the ship
        ship = null;

        // Display a legend
        display.setLegend("Ouch!");

        // Decrement lives
        lives--;

        // Updates current score to be displayed
        display.setLives(this.lives);

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed (Participant p)
    {
        // creates debris surrounding destruction of asteroid p
        createDebris(p);

        // cast p as an asteroid
        Asteroid a = (Asteroid) p;

        int newSpeed = 0;

        // based on size of destroyed asteroid, gets speed of new smaller asteroid
        if (a.getSize() == 2)
        {
            // increments score 20 points for destroying large asteroid
            score += 20;

            // A medium-sized asteroid has a randomly chosen speed that lies between slow and medium
            newSpeed = RANDOM.nextInt((MAXIMUM_MEDIUM_ASTEROID_SPEED - MAXIMUM_LARGE_ASTEROID_SPEED) + 1)
                    + MAXIMUM_LARGE_ASTEROID_SPEED;
        }
        else if (a.getSize() == 1)
        {
            // increments score 50 points for destroying medium asteroid
            score += 50;

            // A small-size asteroid has a randomly chosen speed that lies between slow and fast.
            newSpeed = RANDOM.nextInt((MAXIMUM_SMALL_ASTEROID_SPEED - MAXIMUM_LARGE_ASTEROID_SPEED) + 1)
                    + MAXIMUM_LARGE_ASTEROID_SPEED;
        }
        else
        {
            // increments score 100 points for destroying small asteroid
            score += 100;
        }

        // Updates current score to be displayed
        display.setScore("" + this.score);

        // if asteroid destroyed is not the smallest asteroid then create 2 smaller asteroids
        if (a.getSize() > 0)
        {
            for (int i = 0; i < 2; i++)
            {
                // create new asteroid 1 size smaller than destroyed and at current location
                Asteroid newAsteroid = new Asteroid(RANDOM.nextInt(4), a.getSize() - 1, a.getX(), a.getY(), newSpeed,
                        this);

                // add new asteroid into pstate
                pstate.addParticipant(newAsteroid);
            }
        }

        // if all asteroids destroyed, begins sequence for next level
        if (pstate.countAsteroids() == 0)
        {
            // increases level and updates display
            this.level++;
            display.setLevel("" + this.level);

            // reset beat counter for next level
            this.beat = INITIAL_BEAT;
            this.setBeatTimer(beat);

            // schedules transition for next level to being
            scheduleTransition(END_DELAY);
        }
    }

    /** Create 4 debris at location of p */
    private void createDebris (Participant p)
    {
        for (int i = 0; i < 4; i++)
        {
            Debris d = new Debris((int) p.getX(), (int) p.getY());
            pstate.addParticipant(d);
        }
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Execute
            executeKeyPresses();

            // Refresh screen
            display.refresh();
        }
    }

    public void placeAlien (int variety)
    {
        int x = -10;
        int y = RANDOM.nextInt(SIZE);

        double initialDirection = (x < 0) ? 0 : Math.PI;

        if (RANDOM.nextInt(2) == 0)
        {
            x = SIZE + 10;
        }
        alien = new AlienShip(x, y, initialDirection, this, variety);
        addParticipant(alien);
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                finalScreen();
            }
            else
            {
                // if all asteroids are destroyed
                if (pstate.countAsteroids() == 0)
                {
                    placeAsteroids();
                }
                // if ship has been destroyed
                else if (this.ship == null)
                {
                    placeShip();
                }
                if (level >= 2)
                {
                    if (this.alienShip)
                    {
                        placeAlien((this.level > 2) ? 1 : 2);
                        this.alienShip = false;
                    }
                    else
                    {
                        this.scheduleTransition(ALIEN_DELAY);
                        this.alienShip = true;
                    }
                }
            }
        }

        if (this.beatTransition <= System.currentTimeMillis())
        {
            if (this.beat1)
            {
                Participant.getSounds().playSound("beat1");
            }
            else
            {
                Participant.getSounds().playSound("beat2");
            }
            this.beat1 = !this.beat1;
            this.beatTransition = Long.MAX_VALUE;
            this.beat = Math.max(FASTEST_BEAT, this.beat - BEAT_DELTA);
            this.setBeatTimer(beat);
        }
    }

    /**
     * If a key of interest is pressed adds to queue of keys be executed
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        this.keyboardPresses.add(e.getKeyCode());
    }

    /**
     * When a key is released, removes from queue of keys to be executed
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        this.keyboardPresses.remove(e.getKeyCode());

        // if thruster is released, then tell ship to turn off sound
        if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && ship != null)
        {
            this.ship.thrustRelease();
        }
    }

    /** Executes all of the valid key presses held in keyboardPreses */
    private void executeKeyPresses ()
    {
        if (keyboardPresses.size() > 0)
        {
            for (Integer keyCode : keyboardPresses)
            {
                if ((keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) && ship != null)
                {
                    this.ship.turnRight();
                }
                if ((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) && ship != null)
                {
                    this.ship.turnLeft();
                }
                if ((keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) && ship != null)
                {
                    this.ship.accelerate();
                }
                if ((keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_DOWN) && ship != null)
                {
                    ship.fireBullet();
                }
            }
        }
    }

    /* Returns the number of active ShipBullet participants */
    public int getBulletCount ()
    {
        return pstate.countBullets();
    }

    public void alienDestroyed (Participant p, int variety)
    {
        createDebris(p);
        if (variety == 1)
        {
            this.score += 200;
        }
        else
        {
            this.score += 1000;
        }

        // Refresh screen
        display.refresh();

        // Places a new Alien in game state
        this.alienShip = false;

        if (ship != null)
            this.scheduleTransition(ALIEN_DELAY);
    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {
    }
}
