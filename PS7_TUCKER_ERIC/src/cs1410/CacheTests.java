package cs1410;

import static org.junit.Assert.*;
import org.junit.Test;

public class CacheTests
{
    /**
     * An example test for Cache objects
     */
    @Test
    public void test ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("GCRQWK", c.getGcCode());
    }
    
    /**
     * Tests for missing "GC" prefix
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGCErrorNoPrefix ()
    {
        Cache c = new Cache("RQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        c.getGcCode();
    }
    
    /**
     * Error GC code consists of lower-case letter
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGCErrorLowerCase ()
    {
        Cache c = new Cache("GCRqWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        c.getGcCode();
    }
    
    /**
     * Error GC code consists of non digit/alpha character
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGCErrorIllegalCharacter ()
    {
        Cache c = new Cache("GCRQ!K\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        c.getGcCode();
    }
    
    /**
     * Error GC code has no suffix following GC prefix
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGCErrorNoSuffix ()
    {
        Cache c = new Cache("GC\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        c.getGcCode();
    }
    

    @Test
    public void testTitle ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("Old Three Tooth", c.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleWhiteSpaceError ()
    {
        Cache c = new Cache("GCRQWK\t  \tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        c.getTitle();
    }

    @Test
    public void testOwner ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("geocadet", c.getOwner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOwnerWhiteSpaceError ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\t t\t3.5\t3\tN40 45.850\tW111 48.045");
        c.getOwner();
    }

    @Test
    public void testDifficulty ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals(3.5, c.getDifficulty(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDifficultyErrorIncorrectRange ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.4\t3\tN40 45.850\tW111 48.045");
        c.getDifficulty();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDifficultyErrorIncorrectRange2 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t5.1\t3\tN40 45.850\tW111 48.045");
        c.getDifficulty();
    }

    @Test
    public void testTerrainRating ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals(3, c.getTerrain(), 0.0001);
    }

    @Test
    public void testLong ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("N40 45.850", c.getLongitude());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongWhiteSpaceError ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\t \tW111 48.045");
        c.getLongitude();
    }

    /**
     * tests latitude properly parses and getLatitude returns proper string
     */
    @Test
    public void testLat ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("W111 48.045", c.getLatitude());
    }

    /**
     * tests for latitude containing only whitespace, throws exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLatWhiteSpaceError ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\t ");
        c.getLatitude();
    }
    
    /**
     * tests for number of attributes > 7, throws exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNumAttributes ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\thello");
        c.getLatitude();
    }
}
