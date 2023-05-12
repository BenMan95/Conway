import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    static public final int CELL_SIZE = 20;

    public final int rows;
    public final int cols;
    private boolean[][] cells;

    private final Timer timer;
    private int delay;
    private boolean paused;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        cells = new boolean[rows][cols];

        fillBoard(0);

        delay = 128;
        timer = new Timer(delay, this);
        timer.setInitialDelay(0);
        paused = true;

        addMouseListener(new Mouse());
        addKeyListener(new Keyboard());
        setFocusable(true);

        Dimension size = new Dimension(cols * CELL_SIZE, rows * CELL_SIZE);
        setPreferredSize(size);
        setBackground(Color.BLACK);
    }

    public void fillBoard(double density) {
        Random rand = new Random();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                cells[r][c] = rand.nextDouble() < density;
    }

    private void drawBoard(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        for (int r = 0; r < rows; r++) {
            int y = r * CELL_SIZE;
            for (int c = 0; c < cols; c++) {
                int x = c * CELL_SIZE;
                if (cells[r][c])
                    g2d.fillRect(x+1, y+1, CELL_SIZE-2, CELL_SIZE-2);
            }
        }
    }

    private void updateBoard() {
        boolean[][] temp = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int count = count3x3(r,c);
                temp[r][c] = count == 3 || (count == 4 && cells[r][c]);
            }
        }
        cells = temp;
    }

    private int count3x3(int r, int c) {
        int count = 0;
        for (int rn = r-1; rn <= r+1; rn++) {
            if (rn < 0 || rn >= rows)
                continue;
            for (int cn = c-1; cn <= c+1; cn++)
                if (cn >= 0 && cn < rows)
                    if (cells[rn][cn])
                        count++;
        }
        return count;
    }

    public void setPaused(boolean p) {
        paused = p;
        if (p) timer.stop();
        else timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBoard(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateBoard();
        repaint();
    }

    private class Mouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (paused) {
                int c = e.getX() / CELL_SIZE;
                int r = e.getY() / CELL_SIZE;
                int button = e.getButton();
                if (button == MouseEvent.BUTTON1) cells[r][c] = true;
                if (button == MouseEvent.BUTTON3) cells[r][c] = false;
                repaint();
            }
        }
    }

    private class Keyboard extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            if (key == ' ') setPaused(!paused);
            else if (key == ',') {
                if (delay == 0) delay = 1;
                else delay <<= 1;
                timer.setDelay(delay);
            } else if (key == '.') {
                delay >>= 1;
                timer.setDelay(delay);
            } else if (paused && key == '/') {
                updateBoard();
                repaint();
            }
        }
    }

}
