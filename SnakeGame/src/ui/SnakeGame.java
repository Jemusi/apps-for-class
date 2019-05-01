package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class SnakeGame extends JFrame{
    private boolean gameOver = false;
    private boolean gamePause = false;
    private int tailLength = 2;
    private int tickRate = 5;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private CardLayout layout = new CardLayout();
    private JPanel buttonPanel = new JPanel();
    private JPanel cards = new JPanel(layout);
    private JButton dragon = new JButton("Dragon");
    private JButton snake = new JButton("Snake");
    private JButton pause = new JButton("Pause");
    private JButton unpause = new JButton("Unpause");
    private JButton restart = new JButton("Restart");
    private JButton back = new JButton("Back");
    private JPanel imgPanel = new JPanel();
    private JPanel titlePanel = new JPanel();
    private BufferedImage image;
    public Board board = new Board(gameOver, gamePause, tailLength, tickRate);
    public Timer timer = new Timer(20,board);
    public JPanel Menu = new JPanel(new BorderLayout());

    public static void main(String[] args) {
            JFrame background = new SnakeGame();
    }

    public SnakeGame(){
        initMenu();
        initComponents();
        initUI();
        layout.show(cards, "Game Menu");
    }

    public final void initMenu(){
        try{
            image = ImageIO.read(this.getClass().getResource("snakeimg.png"));
        } catch (IOException e){
            System.out.println("Error has been detected!");
        }
        Image newimg = image.getScaledInstance(150,175, Image.SCALE_DEFAULT);
        JLabel snakeimg = new JLabel(new ImageIcon(newimg));
        JLabel title = new JLabel("A Simple Snake Game",JLabel.CENTER);
        buttonPanel.add(dragon);
        buttonPanel.add(snake);
        imgPanel.add(snakeimg);
        titlePanel.add(title);
        board.add(pause);
        board.add(unpause);
        board.add(restart);
        board.add(back);
        dragon.addActionListener(new ButtonListener());
        snake.addActionListener(new ButtonListener());
        pause.addActionListener(new ButtonListener());
        unpause.addActionListener(new ButtonListener());
        restart.addActionListener(new ButtonListener());
        back.addActionListener(new ButtonListener());
        Menu.add(buttonPanel,BorderLayout.SOUTH);
        Menu.add(imgPanel,BorderLayout.CENTER);
        Menu.add(titlePanel,BorderLayout.NORTH);
    }

    public final void initComponents(){
        cards.add(Menu, "Game Menu");
        cards.add(board, "Game Window");
    }

    public final void initUI(){
        add(cards);
        setSize(300,300);
        setResizable(false);
        setTitle("Game Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            if ("Dragon".equals(command)){
                board.setTickRate(2);
                board.setAnimalColor(Color.YELLOW);
                timer.start();
                setSize(600,602);
                setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
                layout.show(cards, "Game Window");
            } else if("Snake".equals(command)){
                board.setTickRate(4);
                board.setAnimalColor(Color.GREEN);
                timer.start();
                setSize(600,602);
                setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
                layout.show(cards,"Game Window");
            } else if("Pause".equals(command)){
                board.pauseGame();
            } else if("Unpause".equals(command)){
                board.unpauseGame();
            } else if("Restart".equals(command)){
                board.restart();
            } else if("Back".equals(command)){
                timer.stop();
                board.restart();
                setSize(300,300);
                setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
                layout.show(cards,"Game Menu");
            }
        }
    }
}
