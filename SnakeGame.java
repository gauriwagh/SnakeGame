package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 800;
    private final int BOARD_HEIGHT = 600;
    private final int UNIT_SIZE = 25;
    private final int TOTAL_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;

    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;

    private boolean gameOver = false;
    private int score = 0;

    private char direction = 'R';

    public SnakeGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new SnakeKeyListener());
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(UNIT_SIZE, UNIT_SIZE));
        generateFood();
        gameOver = false;
        score = 0;
        direction = 'R';

        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt((int) (BOARD_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = rand.nextInt((int) (BOARD_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            checkCollisions();
            repaint();
        }
    }

    public void move() {

        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, snake.get(i - 1));
        }


        int headX = snake.get(0).x;
        int headY = snake.get(0).y;

        switch (direction) {
            case 'U':
                headY -= UNIT_SIZE;
                break;
            case 'D':
                headY += UNIT_SIZE;
                break;
            case 'L':
                headX -= UNIT_SIZE;
                break;
            case 'R':
                headX += UNIT_SIZE;
                break;
        }

        snake.set(0, new Point(headX, headY));
    }

    public void checkCollisions() {

        if (snake.get(0).x < 0 || snake.get(0).x >= BOARD_WIDTH || snake.get(0).y < 0 || snake.get(0).y >= BOARD_HEIGHT) {
            gameOver = true;
        }


        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                gameOver = true;
            }
        }


        if (snake.get(0).equals(food)) {
            score++;
            snake.add(new Point(-1, -1));
            generateFood();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {

            for (Point point : snake) {
                g.setColor(Color.green);
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }


            g.setColor(Color.red);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);


            g.setColor(Color.white);
            g.drawString("Score: " + score, 10, 20);
        } else {

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over")) / 2, BOARD_HEIGHT / 2);
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Score: " + score, (BOARD_WIDTH - metrics.stringWidth("Score: " + score)) / 2, BOARD_HEIGHT / 2 + 40);
        }
    }

    private class SnakeKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            } else if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}


