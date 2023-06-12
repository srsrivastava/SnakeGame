import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int RAND_POS = 29;
    int B_HEIGHT = 400;
    int B_WIDTH = 400;

    int MAX_DOTS = 1600;
    int DOT_SIZE = 10;

    int DOTS;
    int x[] = new int[MAX_DOTS];
    int y[] = new int[MAX_DOTS];

    int apple_x;
    int apple_y;

    Image head, body, apple;

    Timer timer;
    int DELAY = 150;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;
    Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        initGame();
        loadImages();
    }

    //Initialize game
    public void initGame() {
        DOTS = 3;

        //Initialize Snake's Position
        x[0] = 150;
        y[0] = 150;
        for(int i = 0; i < DOTS; i++) {
            x[i] = x[0] + DOT_SIZE*i;
            y[i] = y[0];
        }

        //Initialize Apple's position
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();

    }

    //Load Images from resources folder to image objects
    public void loadImages() {
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();

        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();

        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }

    //draw images at snake and apple's position
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

            if(inGame) {
                g.drawImage(apple, apple_x, apple_y, this);

                for (int i = 0; i < DOTS; i++) {
                    if (i == 0) {
                        g.drawImage(head, x[i], y[i], this);
                    } else {
                        g.drawImage(body, x[i], y[i], this);
                    }
                }
            } else {
                gameOver(g);
                timer.stop();
            }
    }

    //Randomize Apple position every time
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    //Checks collision of head with any obstacle
    private void checkCollision() {

        for (int z = DOTS; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        int score = (DOTS-3)*100;
        String scoremsg = "\nScore: "+ Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, (B_HEIGHT / 2)-10);
        g.drawString(scoremsg, (B_WIDTH - metr.stringWidth(scoremsg)) / 2,(B_HEIGHT / 2)+10 );
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private void move() {

        for (int z = DOTS; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }



    // Add eat functionality
    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            DOTS++;
            locateApple();
        }
    }
    //Implement Controls
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
