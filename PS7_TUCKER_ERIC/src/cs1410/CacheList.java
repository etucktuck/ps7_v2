package cs1410;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import cs1410lib.Dialogs;

/**
 * A CacheList is a collection of Cache objects together with these six constraints:
 * 
 * <ol>
 * <li>A title constraint</li>
 * <li>An owner constraint</li>
 * <li>A minimum difficulty constraint</li>
 * <li>A maximum difficulty constraint</li>
 * <li>A minimum terrain constraint</li>
 * <li>A maximum terrain constraint</li>
 * </ol>
 */
public class CacheList
{
    // The caches being managed by this CacheList. They are arranged in
    // ascending order according to cache title.
    private ArrayList<Cache> allCaches;

    /**
     * Instance for variables for 5 Geocache browser constraints user can search with. If user does not enter a
     * constraint then the default values are assigned.
     */

    /** Title of Geocache constraint. Default value is "" */
    private String title;
    /** Owner name constraint. Default value is "" */
    private String owner;
    /** Minimum difficulty constraint. Default value 1.0. */
    private double minDifficulty;
    /** Maximum difficulty constraint. Default value 5.0 */
    private double maxDifficulty;
    /** Minimum terrain difficulty constraint. Default value 1.0. */
    private double minTerrain;
    /** Maximum terrain difficulty constraint. Default value 5.0. */
    private double maxTerrain;

    /**
     * Creates a CacheList from the specified Scanner. Each line of the Scanner should contain the description of a
     * cache in a format suitable for consumption by the Cache constructor. The resulting CacheList should contain one
     * Cache object corresponding to each line of the Scanner.
     * 
     * Sets the initial value of the title and owner constraints to the empty string, sets the minimum difficulty and
     * terrain constraints to 1.0, and sets the maximum difficulty and terrain constraints to 5.0.
     * 
     * Throws an IOException if the Scanner throws an IOException, or an IllegalArgumentException if any of the
     * individual lines are not appropriate for the Cache constructor.
     * 
     * When an IllegalArgumentException e is thrown, e.getMessage() is the number of the line that was being read when
     * the error that triggered the exception was encountered. Lines are numbered beginning with 1.
     */
    public CacheList (Scanner caches) throws IOException
    {
        this.allCaches = new ArrayList<Cache>();
        int lineCount = 1;
        try
        {
            while (caches.hasNextLine())
            {
                String line = caches.nextLine();
                Cache c = new Cache(line);
                this.allCaches.add(c);
                lineCount++;
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("" + lineCount);
        }

        // Sort the list of caches
        Collections.sort(allCaches, (c1, c2) -> c1.getTitle().compareToIgnoreCase(c2.getTitle()));

        // upon construction, sets default constraints to title/owner/min-max difficulty & terrain
        this.title = "";
        this.owner = "";
        this.minDifficulty = 1.0;
        this.minTerrain = 1.0;
        this.maxDifficulty = 5.0;
        this.maxTerrain = 5.0;
    }

    /**
     * Sets the title constraint to the specified value.
     */
    public void setTitleConstraint (String title)
    {
        this.title = title;
    }

    /**
     * Sets the owner constraint to the specified value.
     */
    public void setOwnerConstraint (String owner)
    {
        this.owner = owner;
    }

    /**
     * Sets the minimum and maximum difficulty constraints to the specified values.
     */
    public void setDifficultyConstraints (double min, double max)
    {
        this.minDifficulty = min;
        this.maxDifficulty = max;
    }

    /**
     * Sets the minimum and maximum terrain constraints to the specified values.
     */
    public void setTerrainConstraints (double min, double max)
    {
        this.minTerrain = min;
        this.maxTerrain = max;
    }

    /**
     * Returns a list that contains each cache c from the CacheList so long as c's title contains the title constraint
     * as a substring, c's owner equals the owner constraint (unless the owner constraint is empty), c's difficulty
     * rating is between the minimum and maximum difficulties (inclusive), and c's terrain rating is between the minimum
     * and maximum terrains (inclusive). Both the title constraint and the owner constraint are case insensitive.
     * 
     * The returned list is arranged in ascending order by cache title.
     */
    public ArrayList<Cache> select ()
    {
        ArrayList<Cache> caches = new ArrayList<Cache>();

        for (int i = 0; i < this.allCaches.size(); i++)
        {
            Cache c = this.allCaches.get(i);

            // descending if statements comparing CacheList constraints to values in c
            if (c.getTitle().toLowerCase().indexOf(this.title.toLowerCase()) != -1 || this.title == null)
            {
                if (c.getOwner().toLowerCase().indexOf(this.owner.toLowerCase()) != -1 || this.owner == null)
                {
                    if (c.getDifficulty() <= this.maxDifficulty && c.getDifficulty() >= this.minDifficulty)
                    {
                        if (c.getTerrain() <= this.maxTerrain && c.getTerrain() >= this.minTerrain)
                        {
                            caches.add(c);
                        }
                    }
                }
            }
        }
        return caches;
    }

    /**
     * Returns a list containing all the owners of all of the Cache objects in this CacheList. There are no duplicates.
     * The list is arranged in ascending order.
     */
    public ArrayList<String> getOwners ()
    {
        HashSet<String> ownerSet = new HashSet<String>();

        for (int i = 0; i < this.allCaches.size(); i++)
        {
            ownerSet.add(this.allCaches.get(i).getOwner());
        }

        // copies HashSet of owners into ArrayList of owners
        ArrayList<String> owners = new ArrayList<String>(ownerSet);

        Collections.sort(owners, (s1, s2) -> s1.compareToIgnoreCase(s2));
        return owners;
    }
}
