package asteroids.participants;

import asteroids.destroyers.AlienBulletDestroyer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipBulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;

/** Represents a Bullet shot by an Alien ship. Destroys Asteroids and Ships. */
public class AlienBullet extends Bullet implements AsteroidDestroyer, ShipDestroyer
{
    /** Constructs a new AlienBullet */
    public AlienBullet (int x, int y, double direction)
    {
        super(x, y, direction);
    }

    /** Expires AlienBullets from participant state when collide with other participants in game state */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof AlienBulletDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);
        }
    }

}