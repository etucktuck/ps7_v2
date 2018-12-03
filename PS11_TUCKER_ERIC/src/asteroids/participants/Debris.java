package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

/** Represents debris after the destruction of a participant */
public class Debris extends Participant
{
    /** The outline of the ship */
    private Shape outline;

    public Debris (int x, int y)
    {
        this.setPosition(x, y);
        this.setVelocity(MAXIMUM_LARGE_ASTEROID_SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        setOutline();

        // sets a new countdown timer for 500 msec
        new ParticipantCountdownTimer(this, 500);
    }

    /** Sets the shape of Debris */
    private void setOutline ()
    {
        Ellipse2D.Double debris = new Ellipse2D.Double(0, 0, 1, 1);
        this.outline = debris;
    }

    /** Returns the shape of Debris */
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

    /** Expires debris after 500 msec */
    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
    }

}
