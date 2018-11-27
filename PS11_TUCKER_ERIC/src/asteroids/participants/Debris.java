package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

public class Debris extends Participant
{
    /** Game controller */
    private Controller controller;

    /** The outline of the ship */
    private Shape outline;

    public Debris (int x, int y, Controller c)
    {
        this.controller = c;
        this.setPosition(x, y);
        this.setVelocity(MAXIMUM_LARGE_ASTEROID_SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        setOutline();

        new ParticipantCountdownTimer(this, 400);
    }

    private void setOutline ()
    {
        Ellipse2D.Double debris = new Ellipse2D.Double(0, 0, 1, 1);
        this.outline = debris;
    }

    @Override
    protected Shape getOutline ()
    {
        return this.outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // do nothing for debris collisions
    }
    
    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
        //controller.debrisExpire();
    }

}
