package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.*;

public class SoundDemo
{
    private HashMap<String, Clip> soundMap;

    public SoundDemo ()
    {
        soundMap = new HashMap<>();
        addSoundMap();
    }

    private void addSoundMap ()
    {
        soundMap.put("fire", createClip("/sounds/fire.wav"));
        soundMap.put("smallAst", createClip("/sounds/bangSmall.wav"));
        soundMap.put("mediumAst", createClip("/sounds/bangMedium.wav"));
        soundMap.put("largeAst", createClip("/sounds/bangLarge.wav"));
    }
    
    public void playSound(String sound)
    {
        Clip s = soundMap.get(sound);
        if (s.isRunning())
        {
            s.close();
        }
        s.setFramePosition(0);
        s.start();        
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
