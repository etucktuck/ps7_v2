package scan;

import static org.junit.Assert.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import org.junit.Test;

public class MyScannerTests
{
    @Test(expected = NoSuchElementException.class)
    public void NextError1 ()
    {
        MyScanner s2 = new MyScanner("");
        assertFalse(s2.hasNext());
        assertFalse(s2.hasNextInt());
        // expects the NoSuchElementException
        s2.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void NextError2 ()
    {
        MyScanner s1 = new MyScanner("hello ");
        assertEquals("hello", s1.next());
        assertFalse(s1.hasNext());
        // expects the NoSuchElementException
        s1.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void NextTests ()
    {
        MyScanner s1 = new MyScanner("hello      world how are you");
        assertEquals("hello", s1.next());
        assertTrue(s1.hasNext());
        assertEquals("world", s1.next());
        assertEquals("how", s1.next());
        assertEquals("are", s1.next());
        assertEquals("you", s1.next());
        assertFalse(s1.hasNext());
        // expects the NoSuchElementException
        s1.next();
    }

    @Test(expected = InputMismatchException.class)
    public void NextIntErrorDouble ()
    {
        MyScanner s1 = new MyScanner("hello 1 5.2 are 6.4");
        assertEquals("hello", s1.next());
        assertTrue(s1.hasNextInt());
        assertEquals(1, s1.nextInt());
        assertFalse(s1.hasNextInt());
        assertEquals("5.2", s1.next());
        assertEquals("are", s1.next());
        assertFalse(s1.hasNextInt());
        assertTrue(s1.hasNext());
        // expects the InputMismatchException
        s1.nextInt();
    }

    @Test(expected = InputMismatchException.class)
    public void NextIntErrorString ()
    {
        MyScanner s1 = new MyScanner("hello");
        // expects the InputMismatchException
        s1.nextInt();
    }
}
