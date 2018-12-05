package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, AlienBulletDestroyer, AlienDestroyer
{
    /** The outline of the ship */
    private Shape outline;

    /** Game controller */
    private Controller controller;

    /** On and off thruster every other time ship accelerates */
    private boolean thruster;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        this.thruster = false;
        setPosition(x, y);
        setRotation(direction);
        setOutline();
    }

    /**
     * Sets the shape of the ship. Default ship is drawn with no thruster. If thruster is true, then thruster shape is
     * also drawn
     */
    public void setOutline ()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();

        // if thruster on, draws thruster
        if (this.thruster)
        {
            poly.moveTo(-15, -5);
            poly.lineTo(-25, 0);
            poly.lineTo(-15, 5);
        }
        outline = poly;
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /** Fires a ShipBullet */
    public void fireBullet ()
    {
        if (controller.getBulletCount() < BULLET_LIMIT)
        {
            // create a new ship bullet with starting x and y location of ships nose and ships rotation
            ShipBullet b = new ShipBullet((int) this.getXNose(), (int) this.getYNose(), this.getRotation());

            // adds ship to pstate
            controller.addParticipant(b);

            // plays bullet sound
            getSounds().playSound("fire");
        }
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    /** Returns the outline of the ship */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

    /**
     * Releases the thruster ending thruster sound loop and setting coutndown timer for 100 msec to flip thruster flame
     * to off
     */
    public void thrustRelease ()
    {
        // tells sounds to end thruster loop
        getSounds().turnOffThrust();

        // sets 100 msec cdown timer to turn thruster strobe off
        new ParticipantCountdownTimer(this, "thrust", 100);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        // alternates thruster visual on/off every time ship accelerates
        this.thruster = !this.thruster;
        setOutline();

        // begings thruster sound loop
        getSounds().playSound("thrust");

        // accelerates ship
        accelerate(SHIP_ACCELERATION);
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Plays ship explosion sound
            getSounds().playSound("shipDestroyed");

            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.shipDestroyed(this);
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // turns thruster strobe off
        if (payload.equals("thrust"))
        {
            this.thruster = false;
            this.setOutline();
        }
    }
}
