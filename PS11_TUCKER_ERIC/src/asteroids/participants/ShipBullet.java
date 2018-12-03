package asteroids.participants;

import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipBulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;

public class ShipBullet extends Bullet implements AsteroidDestroyer, AlienDestroyer
{
    private Controller c;

    public ShipBullet (int x, int y, double direction, Controller c)
    {
        super(x, y, direction, c);
        this.c = c;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipBulletDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);
        }
    }

}
