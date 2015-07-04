import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener{

    public static final int WIDTH = 200;
    public static final int HEIGHT = 200;
    private final int DELAY = 20;
    private Timer timer;
    private ShapeContainer shapeContainer;
    private GameLogicsManager gameLogicsManager;

    public Board() {
        super();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        timer.start();
        gameLogicsManager = GameLogicsManager.getInstance();
        shapeContainer = ShapeContainer.getInstance();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        shapeContainer.doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        GameLogicsManager.getInstance().updateGame();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            gameLogicsManager.getPlayer().keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            gameLogicsManager.getPlayer().keyPressed(e);
        }
    }

}
