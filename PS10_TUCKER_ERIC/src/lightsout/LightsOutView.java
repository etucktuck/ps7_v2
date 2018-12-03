package lightsout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static lightsout.LightsOutView.*;

/**
 * Implements Lights Out Model with a GUI interface.
 * 
 * @author eric tucker
 */
@SuppressWarnings("serial")
public class LightsOutView extends JFrame implements ActionListener
{
    // formatting constraints
    private final static int WIDTH = 500;
    private final static int HEIGHT = 500;
    public final static int SQUARE_WIDTH = 100;
    public final static int SQUARE_HEIGHT = 100;
    public final static int ROWS = 5;
    public final static int COLS = 5;

    /** The intelligence of the game */
    private LightsOutModel model;

    /** The portion of the GUI that contains the playing surface **/
    private Board board;

    /** Button representing ability to toggle manual mode on and off */
    private JButton manualMode;

    /** Create and initiates the game */
    public LightsOutView ()
    {
        // Set the title that appears at the top of the window
        this.setTitle("Lights Out Game");
        
        // Cause the application to end when the windows is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Does not allow user to resize window
        this.setResizable(false);
        
        // Sets initial size of window
        this.setSize(WIDTH, HEIGHT);

        // Root panel will contain all other GUI elements
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        this.setContentPane(root);

        // Center portion of root contains the playing board
        model = new LightsOutModel(ROWS, COLS);
        board = new Board(model, this);
        root.add(board, "Center");

        // Top portion of root contains buttons for New Game and Manual Mode
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout());
        top.setBackground(Color.GRAY);
        JButton newGame = new JButton();
        newGame.setText("New game");
        newGame.addActionListener(this);
        top.add(newGame);
        this.manualMode = new JButton();
        this.manualMode.setText("Enter Manual Setup");
        manualMode.addActionListener(this);
        top.add(manualMode);
        root.add(top, "North");

        // Refreshes board and sets visible
        board.refreshBoard();
        this.setVisible(true);
    }

    /** Implements action listeners for 'New Game' and 'Manual Mode' buttons.
     * 'New Game' - resets board and refreshes display
     * 'Manual Mode' - toggles current state of manual mode. If exiting manual, also checks for win
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            JButton j = (JButton) e.getSource();
            if (j.getText().equals("New game"))
            {
                this.model.newGame();
                this.manualMode.setText("Enter Manual Setup");
                this.board.refreshBoard();
            }
            else if (j.getText().equals("Enter Manual Setup") || j.getText().equals("Exit Manual Setup"))
            {
                if (this.model.checkLightsOut())
                {
                    return;
                }
                if (this.model.getMode())
                {
                    this.manualMode.setText("Enter Manual Setup");
                }
                else
                {
                    this.manualMode.setText("Exit Manual Setup");
                }
                this.model.switchMode();
                this.board.checkWin();
            }
        }
    }
}

/**
 * The playing surface of the game.
 */
@SuppressWarnings("serial")
class Board extends JPanel implements MouseListener
{
    /** The intelligence of the game */
    LightsOutModel model;

    /** The GUI interface portion */
    LightsOutView view;

    /**
     * Creates the board portion of the GUI.
     */
    public Board (LightsOutModel m, LightsOutView v)
    {
        // Record the model and the top-level display
        this.model = m;
        this.view = v;

        // Set the background color and the layout
        this.setLayout(new GridLayout(ROWS, COLS));

        // Create and lay out the grid of squares that make up the game.
        for (int i = 0; i < ROWS; i++)
        {
            for (int j = 0; j < COLS; j++)
            {
                boolean light = model.getLight(i, j);
                if (light)
                {
                    Square s = new Square(i, j, Color.WHITE);
                    s.addMouseListener(this);
                    this.add(s);
                }
                else
                {
                    Square s = new Square(i, j, Color.BLACK);
                    s.addMouseListener(this);
                    this.add(s);
                }
            }
        }
    }

    /**
     * Refreshes the display. This should be called whenever something changes in the model.
     */
    public void refreshBoard ()
    {
        // Iterate through all of the squares that make up the grid
        Component[] squares = getComponents();
        for (Component c : squares)
        {
            Square s = (Square) c;

            // Set the color of the squares appropriately
            boolean light = this.model.getLight(s.getRow(), s.getCol());

            if (light)
            {
                s.setColor(Color.WHITE);
            }
            else
            {
                s.setColor(Color.BLACK);
            }
        }
        repaint();
    }

    /**
     * Checks if player has won the game with all lights turned off. If win, alerts user with message. If player wins
     * from manual mode, alerts them accordingly.
     */
    public void checkWin ()
    {
        if (this.model.checkLightsOut() && this.model.recentlyExitedManual())
        {
            JOptionPane.showMessageDialog(null, "You won the game in manual mode.");
        }
        else if (this.model.checkLightsOut())
        {
            JOptionPane.showMessageDialog(null, "You won the game.");
        }
    }

    @Override
    public void mouseClicked (MouseEvent e)
    {
    }

    /**
     * Called whenever a Square is clicked. Notifies the model that a move has been attempted.
     */
    @Override
    public void mousePressed (MouseEvent e)
    {
        if (!this.model.checkLightsOut())
        {
            Square s = (Square) e.getSource();
            int row = s.getRow();
            int col = s.getCol();

            this.model.moveTo(row, col);
            refreshBoard();
            checkWin();
        }
    }

    @Override
    public void mouseReleased (MouseEvent e)
    {
    }

    @Override
    public void mouseEntered (MouseEvent e)
    {
    }

    @Override
    public void mouseExited (MouseEvent e)
    {
    }
}

/**
 * A single square on the board where a move can be made
 */
@SuppressWarnings("serial")
class Square extends JPanel
{
    /**
     * The row within the board of this Square. Rows are numbered from zero; row zero is at the top of the board.
     */
    private int row;

    /** The column within the board of this Square. Columns are numbered from zero; column zero is at the left */
    private int col;

    /** The current Color of this Square */
    private Color color;

    /**
     * Creates a square and records its row and column
     */
    public Square (int row, int col, Color color)
    {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    /** Sets the color of this square */
    public void setColor (Color color)
    {
        this.color = color;
    }

    /** Returns the row of this square */
    public int getRow ()
    {
        return this.row;
    }

    /** Returns the column of this square */
    public int getCol ()
    {
        return this.col;
    }

    /**
     * Paints this Square onto g
     */
    @Override
    public void paintComponent (Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(this.color);
        g.fillRect(0, 0, SQUARE_WIDTH, SQUARE_HEIGHT);
    }
}
