package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import asteroids.participants.Asteroid;
import asteroids.participants.Ship;
import asteroids.participants.ShipBullet;
import asteroids.participants.Bullet;
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

    /** Number of lives left */
    private int lives;

    /** The game display */
    private Display display;

    /** Number of bullets currently in play */
    private int bulletCount;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Initialize empty set containing user key presses
        keyboardPresses = new HashSet<>();

        // Record the display object
        display = new Display(this);

        bulletCount = 0;

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
        placeAsteroids();
        placeAsteroids();
        placeAsteroids();
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
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids ()
    {
        addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, 3, this));
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids();

        // Place the ship
        placeShip();

        // Reset statistics
        lives = 3;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
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
        createDebris(p);

        // Null out the ship
        ship = null;

        // Display a legend
        display.setLegend("Ouch!");

        // Decrement lives
        lives--;

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed (Participant p)
    {
        createDebris(p);

        // cast p to an asteroid
        Asteroid a = (Asteroid) p;

        // if asteroid destroyed is not the smallest asteroid then create smaller asteroids
        if (a.getSize() > 0)
        {
            // create 2 new smaller asteroids for every single asteroid destroyed
            for (int i = 0; i < 2; i++)
            {
                int newSpeed = 0;

                // based on size of destroyed asteroid, gets speed of new smaller asteroid
                if (a.getSize() == 2)
                {
                    newSpeed = MAXIMUM_MEDIUM_ASTEROID_SPEED;
                }
                else if (a.getSize() == 1)
                {
                    newSpeed = MAXIMUM_SMALL_ASTEROID_SPEED;
                }

                // create new asteroid 1 size smaller than destroyed and at current location
                Asteroid newAsteroid = new Asteroid(RANDOM.nextInt(4), a.getSize() - 1, a.getX(), a.getY(), newSpeed,
                        this);

                // add new asteroid into pstate
                pstate.addParticipant(newAsteroid);
            }
        }

        // If all the asteroids are gone, schedule a transition
        if (pstate.countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);
        }
    }

    /** Create 4 debris at location of p */
    private void createDebris (Participant p)
    {
        for (int i = 0; i < 4; i++)
        {
            Debris d = new Debris((int) p.getX(), (int) p.getY(), this);
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

            // Execute
            executeKeyPresses();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
        }
    }

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
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W && ship != null)
                {
                    this.ship.accelerate();
                }
                if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_DOWN && ship != null)
                {
                    if (bulletCount < BULLET_LIMIT)
                    {
                        fireBullet();
                    }
                }
            }
        }
    }

    /** When a bullet is removed, decrement bulleCount until 0 */
    public void bulletDestroyed ()
    {
        if (this.bulletCount > 0)
        {
            bulletCount--;
        }
    }

    /** Fires a bullet in the ships current location and direction */
    private void fireBullet ()
    {
        // create a new ship bullet with starting x and y location of ships nose and ships rotation
        ShipBullet b = new ShipBullet((int) this.ship.getXNose(), (int) this.ship.getYNose(), this.ship.getRotation(), this);

        // adds ship to pstate
        pstate.addParticipant(b);

        // increments bullet count
        this.bulletCount++;
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
     * These events are ignored.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * When a key is released, removes from queue of keys to be executed
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        this.keyboardPresses.remove(e.getKeyCode());
    }
}
