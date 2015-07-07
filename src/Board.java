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

    public static final int WIDTH = Initializer.getInstance().getBoardWidth();
    public static final int HEIGHT = Initializer.getInstance().getBoardHeight();
    private final int DELAY = Initializer.getInstance().getTimerDelay();
    private Timer timer;
    private ShapeContainer shapeContainer;
    private GameLogicsManager gameLogicsManager;
    private Player player;

    public Board() {
        super();
        gameLogicsManager = GameLogicsManager.getInstance();
        shapeContainer = ShapeContainer.getInstance();
        player = Initializer.getInstance().getPlayer();


        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        timer.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        shapeContainer.doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        shapeContainer.update();
        gameLogicsManager.update();
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }

}
