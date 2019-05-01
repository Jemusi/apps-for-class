package ui;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel implements ActionListener {

    private boolean gameOver;

    private int overPlayed = 0;

    private boolean gamePause;

    private int tailLength;

    private int time = 0;

    private int score = 0;

    private int tickRate;

    private int ticks = 0;

    private Random random = new Random();

    private Point head = new Point(0, 0);

    private Point fruit = new Point(50, 50);

    private final int SCALE = 10;

    private Color animalColor;

    private ArrayList<Point> animalBody = new ArrayList();

    private Direction direction = Direction.DOWN;

    InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = getActionMap();

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    //EFFECTS: Creates new board for game
    public Board(boolean gameOver, boolean gamePause,
                 int tailLength, int tickRate) {
        this.gameOver = gameOver;
        this.gamePause = gamePause;
        this.tailLength = tailLength;
        this.tickRate = tickRate;
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0 ,false), "left.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0 ,false), "up.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0 ,false), "down.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0 ,false), "right.pressed");
        am.put("left.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction != Direction.RIGHT)) {
                    setDirection(Direction.LEFT);
                }
            }
        });
        am.put("right.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction != Direction.LEFT)) {
                    setDirection(Direction.RIGHT);
                }
            }
        });
        am.put("up.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction != Direction.DOWN)) {
                    setDirection(Direction.UP);
                }
            }
        });
        am.put("down.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction != Direction.UP)) {
                    setDirection(Direction.DOWN);
                }
            }
        });
        try {
            InputStream is = getClass().getResourceAsStream("ruins.wav");
            AudioStream BGM = new AudioStream(is);
            AudioPlayer.player.start(BGM);
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }

    public void setDirection(Direction d) {
        this.direction = d;
    }

    public void setHead(int i, int j) {
        head = new Point(i, j);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);
        g.setColor(animalColor);
        g.fillRect(head.x * SCALE, head.y * SCALE,
                SCALE, SCALE);
        for (Point p : animalBody) {
            g.fillRect(p.x * SCALE, p.y * SCALE, SCALE, SCALE);
        }
        g.setColor(Color.RED);
        g.fillRect(fruit.x * SCALE, fruit.y * SCALE, SCALE, SCALE);
        g.setColor(Color.YELLOW);
        String string = "Score: " +score+ " Length: " +(tailLength+1)+" Time: "+time;
        g.drawString(string,240 - string.length(),580);
        string = "Game Over!";
        if (gameOver){
            g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g.drawString(string,200,250);
            if(overPlayed == 0){
                playOver();
                }
                overPlayed = 1;
            }
        string = "Game is paused.";
        if (gamePause && !gameOver){
            g.drawString(string, 250,250);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
        ticks++;
        if ((ticks % tickRate == 0) && (!gameOver) && (!gamePause)) {
            time++;
            animalBody.add(new Point(head.x, head.y));
            move();
            if (animalBody.size() > tailLength){
                animalBody.remove(0);
            }
            eat();
        }
    }

    private void move() {
        if (this.direction == Direction.UP) {
            if ((head.y - 1 >= 0) && (notIntoTail(head.x,head.y - 1))){
                setHead(this.head.x, this.head.y - 1);
            }
            else{
                this.gameOver = true;
            }
        }

        if (this.direction == Direction.DOWN) {
            if ((head.y + 1 < 58) && (notIntoTail(head.x,head.y + 1))){
                setHead(this.head.x, this.head.y + 1);
            }
            else{
                this.gameOver = true;
            }
        }

        if (this.direction == Direction.LEFT) {
            if ((head.x - 1 >= 0) && (notIntoTail(head.x - 1,head.y))){
                setHead(this.head.x - 1, this.head.y);
            }
            else{
                this.gameOver = true;
            }
        }

        if (this.direction == Direction.RIGHT) {
            if ((head.x + 1 < 60) && (notIntoTail(head.x + 1,head.y))){
                setHead(this.head.x + 1, this.head.y);
            }
            else{
                this.gameOver = true;
            }
        }
    }

    private boolean notIntoTail(int x, int y){
        for (Point p : animalBody){
            if (p.equals(new Point(x,y))){
                return false;
            }
        }
        return true;
    }

    private void eat(){
        if (head.equals(fruit)){
            try {
                InputStream is = getClass().getResourceAsStream("chips.wav");
                AudioStream BGM = new AudioStream(is);
                AudioPlayer.player.start(BGM);
            }catch(IOException e){
                System.out.println(e.toString());
            }
            tailLength++;
            score+=5;
            fruit.setLocation(new Point(random.nextInt(49)+10,
                    random.nextInt(47)+10));
        }
    }

    public void setTickRate(int i){
        this.tickRate = i;
    }

    public void pauseGame(){
        this.gamePause = true;
    }

    public void unpauseGame(){
        this.gamePause = false;
    }

    public void setAnimalColor(Color c){
        this.animalColor = c;
    }

    public void playOver(){
        try {
            InputStream is = getClass().getResourceAsStream("bird.wav");
            AudioStream BGM = new AudioStream(is);
            AudioPlayer.player.start(BGM);
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }

    public void restart(){
        head = new Point(0,0);
        fruit = new Point(50,50);
        animalBody.clear();
        score = 0;
        ticks = 0;
        time = 0;
        tailLength = 2;
        direction = Direction.DOWN;
        gameOver = false;
        gamePause = false;
    }
}

