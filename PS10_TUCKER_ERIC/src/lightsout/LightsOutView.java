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

@SuppressWarnings("serial")
public class LightsOutView extends JFrame implements ActionListener
{
    private final static int WIDTH = 500;
    private final static int HEIGHT = 500;
    private final static int IMAGE_WIDTH = 100;
    private final static int IMAGE_HEIGHT = 100;
    public final static int ROWS = 5;
    public final static int COLS = 5;

    private LightsOutModel model;

    private Board board;

    private JButton manualMode;

    public LightsOutView ()
    {
        this.setTitle("Lights Out Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(600, 150);

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        this.setContentPane(root);

        model = new LightsOutModel(ROWS, COLS);
        board = new Board(model, this);
        root.add(board, "Center");

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

        board.refreshBoard();
        this.setVisible(true);
    }

    class Board extends JPanel implements MouseListener
    {
        LightsOutModel model;

        LightsOutView view;

        public Board (LightsOutModel m, LightsOutView v)
        {
            this.model = m;
            this.view = v;

            this.setLayout(new GridLayout(ROWS, COLS));

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

        private void refreshBoard ()
        {
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

        @Override
        public void mouseClicked (MouseEvent e)
        {
            System.out.println("test");
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
        public void mousePressed (MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased (MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered (MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited (MouseEvent e)
        {
            // TODO Auto-generated method stub

        }
    }

    class Square extends JPanel
    {
        private int row;

        private int col;

        private Color color;

        public Square (int row, int col, Color color)
        {
            this.row = row;
            this.col = col;
            this.color = color;
        }

        public void setColor (Color color)
        {
            this.color = color;
        }

        public int getRow ()
        {
            return this.row;
        }

        public int getCol ()
        {
            return this.col;
        }

        @Override
        public void paintComponent (Graphics g)
        {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(this.color);
            g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        }
    }

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
