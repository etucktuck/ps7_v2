package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.*;

/** Represents available sounds for the participants in an Asteroids game to use when action are performed */
public class AsteroidSounds
{
    /** Collection map to keep track of available .wav files */
    private HashMap<String, Clip> soundMap;

    /** Constructs a new AsteroidSounds with audio clips open and ready to use */
    public AsteroidSounds ()
    {
        soundMap = new HashMap<>();
        addSoundMap();
    }

    /** Adds .wav files inside project directory to soundMap for future retrieval */
    private void addSoundMap ()
    {
        soundMap.put("fire", createClip("/sounds/fire.wav"));
        soundMap.put("smallAst", createClip("/sounds/bangSmall.wav"));
        soundMap.put("mediumAst", createClip("/sounds/bangMedium.wav"));
        soundMap.put("largeAst", createClip("/sounds/bangLarge.wav"));
        soundMap.put("shipDestroyed", createClip("/sounds/bangShip.wav"));
        soundMap.put("thrust", createClip("/sounds/thrust.wav"));
        soundMap.put("beat1", createClip("/sounds/beat1.wav"));
        soundMap.put("beat2", createClip("/sounds/beat2.wav"));
    }

    /** Gets "sound" form soundMap and plays .wav file */
    public void playSound (String sound)
    {
        // if sound is thrust use loop to play sound
        if (sound.equals("thrust"))
        {
            Clip s = soundMap.get(sound);

            // if the thruster is not running, begin thruster noise
            if (!s.isRunning())
            {
                s.setFramePosition(0);
                s.loop(10);
            }
        }
        // if sound other than "thrust"
        else
        {
            Clip s = soundMap.get(sound);

            if (s.isRunning())
            {
                s.stop();
            }
            s.setFramePosition(0);
            s.start();
        }
    }

    /** Turns the thruster loop off */
    public void turnOffThrust ()
    {
        Clip s = soundMap.get("thrust");
        s.stop();
    }

    /**
     * Creates an audio clip from a sound file.
     */
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }
}
