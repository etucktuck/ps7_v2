package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

public class Bullet extends Participant implements AsteroidDestroyer
{
    /** Game controller */
    private Controller controller;
    
    /** The outline of the ship */
    private Shape outline;

    /** Constructs a new bullet */
    public Bullet (int x, int y, double direction, Controller c)
    {
        this.controller = c;
        setOutline();
        this.setPosition(x, y);
        this.setVelocity(BULLET_SPEED, direction);
        
        new ParticipantCountdownTimer(this, BULLET_DURATION);
    }

    private void setOutline()
    {
        Ellipse2D.Double bullet = new Ellipse2D.Double(0, 0, 1, 1);
        this.outline = bullet;
    }
    
    /** Outline shape of single bullet */
    @Override
    protected Shape getOutline ()
    {
        return this.outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the bullet was destroyed
            controller.bulletDestroyed();
        }
    }
    
    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
        controller.bulletDestroyed();
    }
}
