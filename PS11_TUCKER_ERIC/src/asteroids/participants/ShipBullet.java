package asteroids.participants;

import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipBulletDestroyer;
import asteroids.game.Participant;

/** Represents a Bullet fired by the Ship. Destroys Asteroids and Aliens */
public class ShipBullet extends Bullet implements AsteroidDestroyer, AlienDestroyer
{
    /** Constructs a new ShipBullet */
    public ShipBullet (int x, int y, double direction)
    {
        super(x, y, direction);
    }

    /** Expires ShipBullets when collides with other participants in the game */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipBulletDestroyer)
        {
            // Expire the ship bullet from the game
            Participant.expire(this);
        }
    }

}
