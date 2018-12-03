package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import java.util.Iterator;
import javax.swing.*;
import asteroids.participants.Ship;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;

    /** Game controller */
    private Controller controller;

    /** Current score of game */
    private String score;

    /** Current level of game */
    private String level;

    /** Amount of lives remaining */
    private int lives;

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        score = "";
        level = "";
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        setFocusable(true);
    }

    /** Sets the score */
    public void setScore (String score)
    {
        this.score = score;
    }

    /** Sets the level */
    public void setLevel (String level)
    {
        this.level = level;
    }

    /** Sets the amount of lives remaining */
    public void setLives (int lives)
    {
        this.lives = lives;
    }

    /**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        // Use better resolution
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Do the default painting
        super.paintComponent(g);

        // Draw each participant in its proper place
        Iterator<Participant> iter = controller.getParticipants();
        while (iter.hasNext())
        {
            iter.next().draw(g);
        }

        // Draw the legend across the middle of the panel
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);

        // draws score
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, LABEL_VERTICAL_OFFSET));
        g.drawString(this.score, LABEL_HORIZONTAL_OFFSET, LABEL_VERTICAL_OFFSET * 2);

        // draw amount of lives remaining
        for (int i = 0; i < this.lives; i++)
        {
            Ship life = new Ship(LABEL_HORIZONTAL_OFFSET + (i * LABEL_HORIZONTAL_OFFSET), LABEL_VERTICAL_OFFSET * 3,
                    -Math.PI / 2, null);
            life.move();
            life.draw(g);
        }

        // draws level
        g.drawString(this.level, SIZE - LABEL_HORIZONTAL_OFFSET, LABEL_VERTICAL_OFFSET * 2);
    }
}
