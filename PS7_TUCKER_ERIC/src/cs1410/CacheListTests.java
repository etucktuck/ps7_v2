package cs1410;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.Test;

public class CacheListTests
{
    /**
     * An example test for CacheList objects
     */
    @Test
    public void test () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setTitleConstraint("Turns");
        ArrayList<Cache> selected = clist.select();
        assertEquals(1, selected.size());
        assertEquals("GCABCD", selected.get(0).getGcCode());
    }
    
    @Test
    public void test2 () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setTitleConstraint("hello");
        ArrayList<Cache> selected = clist.select();
        assertEquals(0, selected.size());
    }
    
    @Test
    public void testTitleNotCaseSensitive () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setTitleConstraint("turns");
        ArrayList<Cache> selected = clist.select();
        assertEquals(1, selected.size());
    }
    
    @Test
    public void testOwnersNotRepeated () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tbunny\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        ArrayList<String> selected = clist.getOwners();
        assertEquals(1, selected.size());
    }
    
    @Test
    public void testOwnerConstraint () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setOwnerConstraint("bunny");
        ArrayList<Cache> selected = clist.select();
        assertEquals(1, selected.size());
    }
    
    @Test
    public void testOwnerConstraint2 () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tbunny\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setOwnerConstraint("bunny");
        ArrayList<Cache> selected = clist.select();
        assertEquals(2, selected.size());
    }
    
    @Test
    public void testOwnerConstraintNotCaseSensitive () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t3.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setOwnerConstraint("Bunny");
        ArrayList<Cache> selected = clist.select();
        assertEquals(1, selected.size());
    }
    
    @Test
    public void testMinDiff () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t1\t3\tN40 45.850\tW111 48.045\n"));
        clist.setDifficultyConstraints(1.5, 5.0);
        ArrayList<Cache> selected = clist.select();
        assertEquals(0, selected.size());
    }
    
    @Test
    public void testMinDiff2 () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t2\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t1\t3\tN40 45.850\tW111 48.045\n"));
        clist.setDifficultyConstraints(2.0, 5.0);
        ArrayList<Cache> selected = clist.select();
        assertEquals(1, selected.size());
    }
    
    @Test
    public void testMinDiff3 () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t2\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t2.5\t3\tN40 45.850\tW111 48.045\n"));
        clist.setDifficultyConstraints(2.0, 5.0);
        ArrayList<Cache> selected = clist.select();
        assertEquals(2, selected.size());
    }
    
    @Test
    public void testMinTerrainDiff () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t1\t1\tN40 45.850\tW111 48.045\n"));
        clist.setTerrainConstraints(1.5, 5.0);
        ArrayList<Cache> selected = clist.select();
        assertEquals(0, selected.size());
    }
    
    @Test
    public void testMinTerrainDiff2 () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1.5\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t1\t1\tN40 45.850\tW111 48.045\n"));
        clist.setTerrainConstraints(1.5, 5.0);
        ArrayList<Cache> selected = clist.select();
        assertEquals(1, selected.size());
    }
    
    @Test
    public void testMinTerrainDiff3 () throws IOException
    {
        CacheList clist = new CacheList(new Scanner(
                "GCABCD\tAs The World Turns\tbunny\t1\t1.5\tN40 45.875\tW111 48.986\nGCRQWK\tOld Three Tooth\tfred\t1\t2.5\tN40 45.850\tW111 48.045\n"));
        clist.setTerrainConstraints(1.5, 5.0);
        ArrayList<Cache> selected = clist.select();
        assertEquals(2, selected.size());
    }
    
    
}
