import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import javax.swing.*;

public class Application extends JFrame {

    private Board board;

    public Application() {
        setResizable(false);
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        makeMenu();
        setBoard(new Board(35, 35));
        setLocationRelativeTo(null);
    }

    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem menuItem;

        JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem("Grid");
        checkBox.addItemListener(e -> {
            board.setGrid(e.getStateChange() == ItemEvent.SELECTED);
            board.repaint();
        });
        menu.add(checkBox);

        menuItem = new JMenuItem("Set size");
        menuItem.addActionListener(e -> {
            JTextField rowField = new JTextField(5);
            JTextField colField = new JTextField(5);

            JPanel panel = new JPanel();
            panel.add(new JLabel("rows:"));
            panel.add(rowField);
            panel.add(Box.createHorizontalStrut(15));
            panel.add(new JLabel("cols:"));
            panel.add(colField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Set size", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int rows = Integer.parseInt(rowField.getText());
                    int cols = Integer.parseInt(colField.getText());
                    Board b = new Board(rows, cols);
                    b.setGrid(checkBox.getState());
                    setBoard(b);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Input must be integers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Fill");
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

            JSlider slider = new JSlider();
            slider.setMajorTickSpacing(25);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setSnapToTicks(false);

            panel.add(new JLabel("Select fill percentage:"));
            panel.add(slider);

            int result = JOptionPane.showConfirmDialog(null, panel, "Fill", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                board.setPaused(true);
                board.fillBoard((double) slider.getValue() / 100);
                board.repaint();
            }
        });
        menu.add(menuItem);

        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    public void setBoard(Board board) {
        if (this.board != null)
            remove(this.board);
        add(board);
        this.board = board;
        pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Application().setVisible(true));
    }
}