package cs1410;

import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * Represents a variety of information about a geocache. A geocache has a title, an owner, a difficulty rating, a
 * terrain rating, a GC code, a latitude, and a longitude.
 */
public class Cache
{
    // need to add comments
    private String GC;
    private String title;
    private String owner;
    private double difficultyRating;
    private double terrainRating;
    private String latitude;
    private String longitude;

    /**
     * Creates a Cache from a string that consists of these seven cache attributes: the GC code, the title, the owner,
     * the difficulty rating, the terrain rating, the latitude, and the longitude, in that order, separated by single
     * TAB ('\t') characters.
     * 
     * If any of the following problems are present, throws an IllegalArgumentException:
     * <ul>
     * <li>Fewer than seven attributes</li>
     * <li>More than seven attributes</li>
     * <li>A GC code that is anything other than "GC" followed by one or more upper-case letters and/or digits</li>
     * <li>A difficulty or terrain rating that parses to anything other than the doubles 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5,
     * or 5.</li>
     * <li>A title, owner, latitude, or longitude that consists only of white space</li>
     */
    public Cache (String attributes)
    {
        String[] cacheLine = attributes.split("\t");

        // test if line consists of required number of elements
        if (cacheLine.length != 7)
        {
            throw new IllegalArgumentException("Line did not consist of exactly 7 elements.");
        }

        parseGC(cacheLine[0]);
        parseTitle(cacheLine[1]);
        parseOwner(cacheLine[2]);
        parseRating(cacheLine[3], "difficulty");
        parseRating(cacheLine[4], "terrain");
        parseCoordinates(cacheLine[5], "long");
        parseCoordinates(cacheLine[6], "lat");
    }

    private void parseCoordinates (String coord, String key)
    {
        if (!testWhiteSpace(coord))
        {
            throw new IllegalArgumentException("Longitude and/or lattidue contained only whitespace.");
        }
        if (key.equals("long"))
        {
            this.longitude = coord;
        }
        else if (key.equals("lat"))
        {
            this.latitude = coord;
        }
    }

    private void parseRating (String rating, String key)
    {
        try
        {
            double value = Double.parseDouble(rating);

            if ((value * 2) % 1 != 0 || value > 5.0 || value < 1.0)
            {
                throw new IllegalArgumentException(
                        key + "rating value other than 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5 or 5.");
            }

            if (key.equals("terrain"))
            {
                this.terrainRating = value;
            }
            else if (key.equals("difficulty"))
            {
                this.difficultyRating = value;
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException(key + " rating does not contain a parsable double.");
        }
        catch (NullPointerException e)
        {
            throw new IllegalArgumentException(key + " rating is empty.");
        }
    }

    private void parseOwner (String owner)
    {
        if (owner.length() == 0)
        {
            throw new IllegalArgumentException("Owner name is empty.");
        }

        if (testWhiteSpace(owner))
        {
            this.owner = owner;
        }
        else
        {
            throw new IllegalArgumentException("Owner name contains only white space characters.");
        }
    }

    private void parseTitle (String title)
    {
        if (title.length() == 0)
        {
            throw new IllegalArgumentException("Title is empty.");
        }

        if (testWhiteSpace(title))
        {
            this.title = title;
        }
        else
        {
            throw new IllegalArgumentException("Title contains only white space characters.");
        }

    }

    /**
     * Returns true if "line" contains at least 1 non whitespace character. line > 0 length.
     */
    private boolean testWhiteSpace (String line)
    {
        for (int i = 0; i < line.length(); i++)
        {
            if (!Character.isWhitespace(line.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @Throws IllegalArugmentException for prefix not equal to "GC" in gcCode
     * @Throws IllegalArgumentException for character in gcCode not character,digit, or uppercase 
     * @Throws IllegalArgumentException for no characters following "GC" prefix
     */
    public void parseGC (String gcCode)
    {
        // if length of GC is not > 3 , throws exception
        if (gcCode.length() < 3)
        {
            throw new IllegalArgumentException();
        }

        // if first 2 characters != 'GC', throws exception
        if (!gcCode.substring(0, 2).equals("GC"))
        {
            throw new IllegalArgumentException("Prefix GC missing for GC code.");
        }

        for (int i = 2; i < gcCode.length(); i++)
        {
            char tempChar = gcCode.charAt(i);
            // if character is neither letter or digit, throws exception
            if (!Character.isLetterOrDigit(tempChar))
            {
                throw new IllegalArgumentException("Illegal character formatting in GC Code.");
            }
            // if letter is a character and lowercase, throws exception
            if (Character.isLetter(tempChar) && !Character.isUpperCase(tempChar))
            {
                throw new IllegalArgumentException("Illegal character formatting in GC Code.");
            }
        }
        this.GC = gcCode;
    }

    /**
     * Converts this cache to a string
     */
    public String toString ()
    {
        return getTitle() + " by " + getOwner();
    }

    /**
     * Returns the owner of this cache
     */
    public String getOwner ()
    {
        return this.owner;
    }

    /**
     * Returns the title of this cache
     */
    public String getTitle ()
    {
        return this.title;
    }

    /**
     * Returns the difficulty rating of this cache
     */
    public double getDifficulty ()
    {
        return this.difficultyRating;
    }

    /**
     * Returns the terrain rating of this cache
     */
    public double getTerrain ()
    {
        return this.terrainRating;
    }

    /**
     * Returns the GC code of this cache
     */
    public String getGcCode ()
    {
        return this.GC;
    }

    /**
     * Returns the latitude of this cache
     */
    public String getLatitude ()
    {

        return this.latitude;
    }

    /**
     * Returns the longitude of this cache
     */
    public String getLongitude ()
    {

        return this.longitude;
    }
}
