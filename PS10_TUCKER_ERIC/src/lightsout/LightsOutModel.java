package lightsout;

import java.util.Random;

public class LightsOutModel
{
    private boolean board[][];

    private boolean manualSetupMode;

    private boolean recentlyExitedManual;

    public LightsOutModel (int row, int col)
    {
        this.board = new boolean[row][col];
        this.newGame();
    }

    public boolean recentlyExitedManual ()
    {
        return this.recentlyExitedManual;
    }

    /**
     * Reports if all elements in LightsOut are false. Always returns false during manual setup mode.
     */
    public boolean checkLightsOut ()
    {
        if (this.manualSetupMode)
        {
            return false;
        }

        for (int i = 0; i < this.board.length; i++)
        {
            for (int j = 0; j < this.board[i].length; j++)
            {
                if (this.board[i][j] == true)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates a new random and solvabe game state. New games begin with Manual mode off.
     */
    public void newGame ()
    {
        // sets all squares from board[][] to false
        this.clearBoard();

        // resets game state variables
        this.manualSetupMode = false;
        this.recentlyExitedManual = false;

        Random r = new Random();

        // generates 4 random moveTo moves
        for (int i = 0; i < 4; i++)
        {
            int row = r.nextInt(this.board.length);
            int col = r.nextInt(this.board[0].length);
            this.moveTo(row, col);

            // used for solving. COMMENT OUT ON TURN IN
            System.out.println(row + " " + col);
        }
    }

    /**
     * Sets all elements of board to false.
     */
    private void clearBoard ()
    {
        for (int i = 0; i < this.board.length; i++)
        {
            for (int j = 0; j < this.board[i].length; j++)
            {
                board[i][j] = false;
            }
        }
    }

    /**
     * Switches the boolean state of the designated element and if not in manual setup mode switches boolean state of 4
     * adjacent squares: north, west, south and east of designated square.
     */
    public void moveTo (int row, int col)
    {
        // does nothing if move requested is out of bounds
        if (row < 0 || col < 0 || row > this.board.length || col > this.board[0].length)
        {
            return;
        }

        // flips designated square
        this.flipSquare(row, col);

        // flips adjacent squares if NOT in manual setup mode
        if (!this.manualSetupMode)
        {
            this.flipSquare(row + 1, col);
            this.flipSquare(row - 1, col);
            this.flipSquare(row, col - 1);
            this.flipSquare(row, col + 1);

            this.recentlyExitedManual = false;
        }
        return;
    }

    /**
     * Switches the the boolean state of element at specified row and column
     */
    private void flipSquare (int row, int col)
    {
        try
        {
            // switches the square on the board.
            this.board[row][col] = !this.board[row][col];
        }
        catch (Exception e)
        {

        }
    }

    /**
     * Switches the boolean state of Manual Setup Mode
     */
    public void switchMode ()
    {
        if (this.manualSetupMode)
        {
            this.recentlyExitedManual = true;
        }
        this.manualSetupMode = !this.manualSetupMode;
    }

    /**
     * Returns if currently in Manual Setup Mode.
     */
    public boolean getMode ()
    {
        return this.manualSetupMode;
    }

    /**
     * Prints out the current state of the board in a row x col console grid. Used for testing before GUI.
     */
    public void print ()
    {
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                if (board[i][j] == false)
                {
                    System.out.print("f - ");
                }
                else
                {
                    System.out.print("t - ");
                }
                if (j == board[i].length - 1)
                {
                    System.out.println("");
                }
            }
        }
    }

    /**
     * Returns if the element in specified row or column is true or false.
     */
    public boolean getLight (int row, int col)
    {
        if (row < 0 || col < 0 || row > this.board.length || col > this.board[0].length)
        {
            return false;
        }
        return this.board[row][col];
    }

}
