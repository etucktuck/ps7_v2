package scan;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MyScanner
{
    private String[] tokens;
    private int index;

    public static void main (String[] args)
    {
    }

    /**
     * Constructs a new MyScanner that produces values scanned from the specified string
     */
    public MyScanner (String s)
    {
        this.tokens = s.trim().split("\\s+");
        this.index = 0;
    }

    /**
     * Returns true if there is a token remaining in MyScanner
     */
    public boolean hasNext ()
    {
        if (this.index < this.tokens.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the next token in MyScanner as a String
     * 
     * @Throws NoSuchElementException if there are no remaining tokens in MyScanner
     */
    public String next ()
    {
        if (this.hasNext())
        {
            this.index += 1;
            return this.tokens[index - 1];
        }
        else
        {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns true if the next token in MyScanner can be parsed as an integer
     */
    public boolean hasNextInt ()
    {
        if (this.hasNext())
        {
            String tempToken = this.tokens[index];
            for (int i = 0; i < tempToken.length(); i++)
            {
                if (!Character.isDigit(tempToken.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns an integer from the next token in MyScanner
     * 
     * @Throws InputMismatchException if the next token in MyScanner is not an integer
     * @Throws NoSuchElementException if there are no more tokens in MyScanner
     */
    public int nextInt ()
    {
        if (this.hasNextInt())
        {
            this.index += 1;
            return Integer.parseInt(this.tokens[index - 1]);
        }
        else if (this.hasNext())
        {
            throw new InputMismatchException();
        }
        else
        {
            throw new NoSuchElementException();
        }
    }
}