package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

/** Represents a generic Bullet in the Asteroids game. To be extended by Alien and Ship Bullets */
public class Bullet extends Participant
{
    /** The outline of the ship */
    private Shape outline;

    /** Constructs a new bullet */
    public Bullet (int x, int y, double direction)
    {
        setOutline();
        this.setPosition(x, y);
        this.setVelocity(BULLET_SPEED, direction);

        new ParticipantCountdownTimer(this, BULLET_DURATION);
    }

    /** Creates the outline of the bullet */
    private void setOutline ()
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

    /** Expires the bullet after BULLET_DURATION */
    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
    }

    /** To be overridden by Ship or AlienBullet */
    @Override
    public void collidedWith (Participant p)
    {
    }
}
