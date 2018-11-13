package cs1410;

import static org.junit.Assert.*;

import org.junit.Test;

public class CacheTests
{
    public static final Cache cache = new Cache("GCA\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1 ()
    {
        new Cache("GCA\tTitle\tOwner\t1.0\t5.0\tN40 38.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2 ()
    {
        new Cache("GCA\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000\tA+");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3a ()
    {
        new Cache("GC\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3b ()
    {
        new Cache("xyz\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3c ()
    {
        new Cache("GCaB7\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3d ()
    {
        new Cache("GCABCDEFGHIJKLmNOPQRSTUVWXYZ\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3e ()
    {
        new Cache("GC1234567890x1234567890\tTitle\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor4 ()
    {
        new Cache("GCA\t\tOwner\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor5 ()
    {
        new Cache("GCA\tTitle\t\t1.0\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor6 ()
    {
        new Cache("GCA\tTitle\tOwner\t0.5\t5.0\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor7 ()
    {
        new Cache("GCA\tTitle\tOwner\t1.0\t5.5\tN40 38.000\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor8 ()
    {
        new Cache("GCA\tTitle\tOwner\t1.0\t5.0\t\tW111 45.000");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor9 ()
    {
        new Cache("GCA\tTitle\tOwner\t1.0\t5.0\tN40 38.000\t");
    }
    
    @Test
    public void testGetOwner ()
    {
        assertEquals("Owner", cache.getOwner());
    }

    @Test
    public void testGetTitle ()
    {
        assertEquals("Title", cache.getTitle());
    }

    @Test
    public void testGetDifficulty ()
    {
        assertEquals(1.0, cache.getDifficulty(), 1e-6);
    }

    @Test
    public void testGetTerrain ()
    {
        assertEquals(5.0, cache.getTerrain(), 1e-6);
    }

    @Test
    public void testGetGcCode ()
    {
        assertEquals("GCA", cache.getGcCode());
    }

    @Test
    public void testGetLatitude ()
    {
        assertEquals("N40 38.000", cache.getLatitude());
    }

    @Test
    public void testGetLongitude ()
    {
        assertEquals("W111 45.000", cache.getLongitude());
    }
}
