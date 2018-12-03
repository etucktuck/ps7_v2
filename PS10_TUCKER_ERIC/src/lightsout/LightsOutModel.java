package lightsout;

import java.util.Random;

/**
 * Represents the state of the Lights Out game board. Construction of a new LightsOutModel initiates a randomized 2D
 * array of booleans. User can select squares on the board to switch the boolean state of given square and consequently
 * adjacent squares. Goal of the game is to have all squares as false or lights off at the same time.
 * 
 * 
 * Features included: New Game - wipes current game and creates a new random, solvable board state. Manual Setup Mode -
 * allows user to select 1 square at a time.
 * 
 * @author eric tucker
 */
public class LightsOutModel
{
    /**
     * Boolean 2D array representing state of the playing board. True for lights on and false for lights off.
     */
    private boolean board[][];

    /**
     * True when user enters manual set-up mode.
     */
    private boolean manualSetupMode;

    /**
     * True when manualSetupMode switches from true -> false && moveTo hasn't been actioned.
     */
    private boolean recentlyExitedManual;

    /**
     * Creates a new Lights Out model beginning with a 2D playing board row x col dimensions. Game begins with a
     * randomized solvable puzzle.
     */
    public LightsOutModel (int row, int col)
    {
        this.board = new boolean[row][col];
        this.newGame();
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
            //System.out.println(row + " " + col);
        }
        // if randomized moves created an already completed state, recalls method
        if (this.checkLightsOut())
        {
            this.newGame();
        }
    }

    /**
     * Reports true if all elements in LightsOut are turned off (false). Always returns false during manual setup mode.
     */
    public boolean checkLightsOut ()
    {
        // while in manualSetupMode, always reports false
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
     * Returns boolean state of recentlyExitedManual mode.
     */
    public boolean recentlyExitedManual ()
    {
        return this.recentlyExitedManual;
    }

    /**
     * Returns if currently in Manual Setup Mode.
     */
    public boolean getMode ()
    {
        return this.manualSetupMode;
    }

    /**
     * Prints to the console the current state of the board in a row x col console grid. Used for testing before GUI.
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
     * Returns the boolean state of the element at the specified row & column.
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
