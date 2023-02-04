import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Racer {
    private static Boolean endgame;
    private static BufferedImage background;
    private static BufferedImage player;
    private static BufferedImage player2;
    private static BufferedImage gameCover;
    private static Boolean upPressed;
    private static Boolean downPressed;
    private static Boolean leftPressed;
    private static Boolean rightPressed;
    private static Boolean wPressed;
    private static Boolean sPressed;
    private static Boolean aPressed;
    private static Boolean dPressed;
    private static ImageObject p1;
    private static ImageObject p2;
    private static double pWidth;
    private static double pHeight;
    private static double p1OriginalX;
    private static double p1OriginalY;
    private static double p1Velocity;
    private static double p2Width;
    private static double p2Height;
    private static double p2OriginalX;
    private static double p2OriginalY;
    private static double p2Velocity;
    private static int maxSpeed;
    private static int xOffset;
    private static int yOffset;
    private static double pi;
    private static double twoPi;
    private static long start = System.currentTimeMillis();
    private static long startPlayer1 = System.currentTimeMillis();
    private static long startPlayer2 = System.currentTimeMillis();
    private static long bestTimePlayer1 = Integer.MAX_VALUE;
    private static long bestTimePlayer2 = Integer.MAX_VALUE;
    private static JFrame appFrame;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private static int maxLaps;
    private static int currentLap;
    private static JComboBox lapList;
    private static JComboBox vehicleList;
    private static JComboBox vehicleList2;
    private static int p1CurrentLap = 1;
    private static int p2CurrentLap = 1;
    private static BufferedImage Porshe;
    private static BufferedImage BMW;
    private static BufferedImage Supra;
    private static BufferedImage Toyota;
    private static BufferedImage[] barriers;
    private static AudioInputStream ais;
    private static Clip wav1;
    private static AudioInputStream ais2;
    private static Clip wav2;
    private static Image image;

    public Racer() {
        setup();
    }

    public static void setup() {
        appFrame = new JFrame("Vigorous Velocity - Racing Game");
        xOffset = 0;
        yOffset = 0;
        //winWidth = 500;
        //winHeight = 500;
        pi = Math.PI;
        twoPi = 2 * pi;
        endgame = false;

        maxSpeed = 5;
        maxLaps = 5;
        currentLap = 1;

        //vehicle dimensions
        pHeight = 50;
        pWidth = 50;

        //initial coords for the players
        p1OriginalX = 650; //(double) xOffset + ((double) winWidth / 2.0) - (pWidth / 2.0) + 400;
        p1OriginalY = 600; //(double) yOffset + ((double) winHeight / 2.0) - (pHeight / 2.0) + 50;

        p2OriginalX = 600; //(double) xOffset + ((double) winWidth / 2.0) - (pWidth / 2.0) + 400;
        p2OriginalY = 610; //(double) yOffset + ((double) winHeight / 2.0) - (pHeight / 2.0) + 100;

        System.out.println("P1 x: " + p1OriginalX + ", P1 y: " + p1OriginalY);
        System.out.println("P2 x: " + p2OriginalX + ", P2 y: " + p2OriginalY);

        try {

            //default images for the game
            gameCover = ImageIO.read(new File("src/images/coverlogo.png"));
            background = ImageIO.read(new File("src/images/track1.png"));
            player = ImageIO.read(new File("src/images/supratopview.png"));
            player2 = ImageIO.read(new File("src/images/poschetop.png"));

        } catch (IOException ioe) {

        }

    }

    /**
     * Main method for initializing the game, adding buttons, adding drop down menus,
     * binding keyboard keys, creating the main app frame
     */
    public static void main(String[] args) {
        setup();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(250, 250);

        JPanel myPanel = new JPanel();
        Cover();

        //start game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new StartGame());
        myPanel.add(newGameButton);

        //select laps drop down menu
        Integer[] laps = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JLabel lapText = new JLabel("Select Laps");
        lapList = new JComboBox(laps);
        myPanel.add(lapText);
        myPanel.add(lapList);
        lapList.addActionListener(new LapListener());

        //select vehicle player 1 drop down
        String[] vehicles = new String[]{"Supra", "Porshe", "BMW", "Toyota"};
        JLabel vehicleText1 = new JLabel("Player 1");
        vehicleList = new JComboBox(vehicles);
        myPanel.add(vehicleText1);
        myPanel.add(vehicleList);
        vehicleList.addActionListener(new vehicleListener1());

        //select vehicle player 2 drop down
        JLabel vehicleText2 = new JLabel("Player 2");
        vehicleList2 = new JComboBox(vehicles);
        myPanel.add(vehicleText2);
        myPanel.add(vehicleList2);
        vehicleList2.addActionListener(new vehicleListener2());

        //quit game button
        JButton quitButton = new JButton("Quit Game");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);

        //controls for player 1
        bindKey(myPanel, "UP");
        bindKey(myPanel, "DOWN");
        bindKey(myPanel, "LEFT");
        bindKey(myPanel, "RIGHT");

        //controls for player 2
        bindKey(myPanel, "W");
        bindKey(myPanel, "S");
        bindKey(myPanel, "A");
        bindKey(myPanel, "D");

        appFrame.getContentPane().add(myPanel, "South");
        appFrame.setVisible(true);
    }

    public static void Cover() {
        image = new ImageIcon("src/images/coverlogo.png").getImage();

        JPanel container = new MyBackground();
        appFrame.add(container);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(1920, 1080);
        appFrame.setVisible(true);
        try {
            //play music
            ais = AudioSystem.getAudioInputStream(new File("src/sounds/yt5s.io_-_NFS_CARBON_BELT_TUNER_THEMEPHONK_REMIX_320_kbps.mp3"));
            wav1 = AudioSystem.getClip();
            wav1.open(ais);
            wav1.start();
            wav1.loop(0);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing sound file: " + e.getMessage());
        }
    }


    public static class MyBackground extends JPanel {

        public MyBackground() {
            setBackground(new Color(0, true));
        }

        @Override
        public void paintComponent(Graphics g) {
            //Paint background first
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

            //Paint the rest of the component. Children and self etc.
            super.paintComponent(g);
        }
    }

    private static class Animate implements Runnable {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        public void run() {
            while (!endgame) {
                drawBackground();
                drawPlayer();
                drawClock();
                drawSpeed();

                if (p1CurrentLap >= maxLaps + 1) {
                    drawWinner("Player 1");
                    drawWinner("Player 1");
                    g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
                    g2d.setColor(Color.GREEN);
                    g2d.drawString("Player 1 Best Lap: " + bestTimePlayer1 / 1000f + " seconds", 450, 550);
                    g2d.drawString("Player 2 Best Lap: " + bestTimePlayer2 / 1000f + " seconds", 450, 650);
                    endgame = true;
                    wav1.close();
                    wav2.close();
                } else if (p2CurrentLap >= maxLaps + 1) {
                    drawWinner("Player 2");
                    g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
                    g2d.setColor(Color.red);
                    g2d.drawString("Player 1 Best Lap: " + bestTimePlayer1 / 1000f + " seconds", 450, 550);
                    g2d.drawString("Player 2 Best Lap: " + bestTimePlayer2 / 1000f + " seconds", 450, 650);
                    endgame = true;
                    wav1.close();
                    wav2.close();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Exception caught in Animate");
                }
            }
        }
    }

    private static void drawWinner(String winningPlayer) {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.red);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 120));
        g2d.drawString(winningPlayer + " won!", 400, 400);
    }

    private static class LapListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            maxLaps = lapList.getSelectedIndex() + 1;
        }
    }

    private static class vehicleListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int n = vehicleList.getSelectedIndex();
            player = vehiclePick(player, n);
        }
    }

    private static class vehicleListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int n = vehicleList2.getSelectedIndex();
            player2 = vehiclePick(player2, n);
        }
    }

    public static BufferedImage vehiclePick(BufferedImage playervehicle, int n) {
        if (n == 0)
            playervehicle = Porshe;
        else if (n == 1)
            playervehicle = BMW;
        else if (n == 2)
            playervehicle = Supra;
        else
            playervehicle = Toyota;
        return playervehicle;
    }


    private static class StartGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            // endgame = true;
            start = System.currentTimeMillis();
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;

            wPressed = false;
            sPressed = false;
            aPressed = false;
            dPressed = false;

            //instantiate the ImageObjects for the player vehicles
            p1 = new ImageObject(p1OriginalX, p1OriginalY, pWidth, pHeight, 0.0);
            p2 = new ImageObject(p2OriginalX, p2OriginalY, pWidth, pHeight, 0.0);

            p1Velocity = 0.0;
            p2Velocity = 0.0;

            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
                System.out.println("Caught the exception in start game");
            }

            try {
                //play music
                ais = AudioSystem.getAudioInputStream(new File("src/sounds/yt5s.io_-_NFS_CARBON_BELT_TUNER_THEMEPHONK_REMIX_320_kbps.mp3"));
                wav1 = AudioSystem.getClip();
                wav1.open(ais);
                wav1.loop(Clip.LOOP_CONTINUOUSLY);
                wav1.start();

            } catch (UnsupportedAudioFileException uafe) {
            } catch (IOException ioe) {
            } catch (LineUnavailableException lue) {
            }

            endgame = false;
            Thread t1 = new Thread(new Animate());
            Thread t2 = new Thread(new PlayerMover());
            Thread t3 = new Thread(new CollisionChecker());
            Thread t4 = new Thread(new WinChecker());
            t1.start();
            t2.start();
            //t3.start();
        }
    }
    private static class QuitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
            wav1.stop();
        }
    }

    private static class WinChecker implements Runnable {
        public void run() {
            if (p1CurrentLap >= maxLaps + 1) {
                drawWinner("Player 1");
            } else if (p2CurrentLap >= maxLaps + 1) {
                drawWinner("Player 2");
            }
            else if (bestTimePlayer1 == bestTimePlayer2) {
                drawWinner("Player 1");
            }
        }

        private static void drawWinner(String winningPlayer) {
            Graphics g = appFrame.getGraphics();
            Graphics2D g2d = (Graphics2D) g;

            g.setColor(Color.red);
            g2d.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g2d.drawString(winningPlayer + " won!", 450, 450);

            g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g2d.drawString("Player 1 Best Lap: " + bestTimePlayer1, 550, 550);
            g2d.drawString("Player 2 Best Lap: " + bestTimePlayer2, 550, 450);
        }
    }

    private static class PlayerMover implements Runnable {

        private double velocityStep;
        private double rotateStep;

        //level of acceleration and rotation speed
        public PlayerMover() {
            velocityStep = 0.01;
            rotateStep = 0.03;
        }

        private void changeVelocity(double playerVelocity) {
            if (playerVelocity > 0) {
                velocityStep -= 0.01 * 1;
            } else {
                velocityStep += 0.01 * 1;
            }
        }
        public static boolean inBounds(ImageObject playerCheck) {
            return playerCheck.getX() < 1123 && playerCheck.getX() > 207 && playerCheck.getY() > 216 && playerCheck.getY() < 837;
        }
        public static boolean inBoundsInner(ImageObject playerCheck) {
            return playerCheck.getX() < 970 && playerCheck.getX() > 375 && playerCheck.getY() > 375 && playerCheck.getY() < 670;
        }
        public static boolean hitBlueTent(ImageObject playerCheck) {
            return playerCheck.getX() > 445 && playerCheck.getX() < 815 && playerCheck.getY() > 28 && playerCheck.getY() < 175;
        }
        public static boolean hitBrownHouse(ImageObject playerCheck) {
            return playerCheck.getX() < 205 && playerCheck.getX() > -9 && playerCheck.getY() > 190 && playerCheck.getY() < 420;
        }
        public static boolean hitRedHouse(ImageObject playerCheck) {
            return playerCheck.getX() < 175 && playerCheck.getX() > -9 && playerCheck.getY() > 550 && playerCheck.getY() < 800;
        }
        public static boolean hitEndOfMap(ImageObject playerCheck) {
            return playerCheck.getX() >= 1000 || playerCheck.getX() <= 10 || playerCheck.getY() >= 920 || playerCheck.getY() <= 15;
        }

        public static boolean hitPlayer(ImageObject p1, ImageObject p2) {
            return (((p1.getX()) > p2.getX()) && ((p1.getX() - 25) < p2.getX()) &&
                    ((p1.getY()) < p2.getY()) && ((p2.getY() - 25) < p1.getY()));
        }

        public static boolean passedLap(ImageObject playerCheck) {
            return playerCheck.getX() >= 142 && playerCheck.getX() <= 142 && playerCheck.getY() >= 154 && playerCheck.getY() <= 166;
        }

        public static boolean passedMidLap(ImageObject playerCheck) {
            return playerCheck.getX() >= 46 && playerCheck.getX() <= 62 && playerCheck.getY() >= 121 && playerCheck.getY() <= 121;
        }

        public void run() {

            while (!endgame) {

                long p1Timer = System.currentTimeMillis() - startPlayer1;
                long p2Timer = System.currentTimeMillis() - startPlayer2;

                if (passedLap(p1) && p1Timer > 2000) {
                    p1CurrentLap += 1;
                    long prevLap = System.currentTimeMillis() - startPlayer1;
                    startPlayer1 = System.currentTimeMillis();
                    if (prevLap < bestTimePlayer1) bestTimePlayer1 = prevLap;
                }

                if (upPressed && !inBounds(p1)) {
                    p1Velocity = (double) maxSpeed * 0.8;
                } else if (wPressed && !inBounds(p2)) {
                    p2Velocity = (double) maxSpeed * 0.8;
                }

                if (upPressed && inBoundsInner(p1)) {
                    p1Velocity = (double) maxSpeed * 0.08;
                } else if (wPressed && inBoundsInner(p2)) {
                    p2Velocity = (double) maxSpeed * 0.08;
                }
                if (hitBlueTent(p1) || hitBrownHouse(p1) || hitRedHouse(p1) || hitEndOfMap(p1) || hitPlayer(p1, p2)) {
                    p1Velocity = 0;
                    p1Velocity -= velocityStep * 100;
                } else if (hitBlueTent(p2) || hitBrownHouse(p2) || hitRedHouse(p2) || hitEndOfMap(p2) || hitPlayer(p2, p1)) {
                    p2Velocity = 0;
                    p2Velocity -= velocityStep * 100;
                }

                if (passedLap(p2) && p2Timer > 2000) {
                    p2CurrentLap += 1;
                    long prevLap = System.currentTimeMillis() - startPlayer2;
                    startPlayer2 = System.currentTimeMillis();
                    if (prevLap < bestTimePlayer2) bestTimePlayer2 = prevLap;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Exception caught for PlayerMover");
                }

                if (upPressed && p1Velocity < maxSpeed && inBounds(p1)) {
                    p1Velocity += velocityStep * 4;
                } else if (wPressed && p2Velocity < maxSpeed && inBounds(p2)) {
                    p2Velocity += velocityStep * 4;
                }

                if (hitPlayer(p1, p2) || hitPlayer(p2, p1)) {
                    wav2.setFramePosition(0);
                    wav2.start();
                }

                if (!upPressed && !downPressed && p1Velocity > 0) {
                    p1Velocity -= velocityStep * 3;
                } else if (!wPressed && !sPressed && p2Velocity > 0) {
                    p2Velocity -= velocityStep * 3;
                }

                if (upPressed && downPressed && p1Velocity < 0) {
                    p1Velocity += velocityStep * 3;
                } else if (wPressed && sPressed && p2Velocity < 0) {
                    p2Velocity += velocityStep * 3;
                }

                if (downPressed && p1Velocity * -1 < maxSpeed / 2 && inBounds(p1))
                    p1Velocity -= velocityStep * 5;
                else if (sPressed && p2Velocity * -1 < maxSpeed / 2 && inBounds(p2)) {
                    p2Velocity -= velocityStep * 5;
                }

                if (leftPressed) {
                    if (p1Velocity < 0)
                        p1.rotate(-rotateStep);
                    else
                        p1.rotate(rotateStep);
                }

                if (dPressed) {
                    if (p2Velocity < 0)
                        p2.rotate(-rotateStep);
                    else
                        p2.rotate(rotateStep);
                }

                if (rightPressed) {
                    if (p1Velocity < 0)
                        p1.rotate(rotateStep);
                    else
                        p1.rotate(-rotateStep);
                }

                if (aPressed) {
                    if (p2Velocity < 0)
                        p2.rotate(rotateStep);
                    else
                        p2.rotate(-rotateStep);
                }

                p1.move(-p1Velocity * Math.cos(p1.getAngle() - pi / 2.0), p1Velocity * Math.sin(p1.getAngle() - pi / 2.0));
                p2.move(-p2Velocity * Math.cos(p2.getAngle() - pi / 2.0), p2Velocity * Math.sin(p2.getAngle() - pi / 2.0));
            }

        }
    }

    private static class KeyPressed extends AbstractAction {

        private String action;

        public KeyPressed() {
            action = "";
        }

        public KeyPressed(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) upPressed = true;
            if (action.equals("DOWN")) downPressed = true;
            if (action.equals("LEFT")) leftPressed = true;
            if (action.equals("RIGHT")) rightPressed = true;

            if (action.equals("W")) wPressed = true;
            if (action.equals("S")) sPressed = true;
            if (action.equals("A")) aPressed = true;
            if (action.equals("D")) dPressed = true;
        }
    }

    private static class KeyReleased extends AbstractAction {

        private String action;

        public KeyReleased() {
            action = "";
        }

        public KeyReleased(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
//            System.out.println("Key released");

            if (action.equals("UP")) upPressed = false;
            if (action.equals("DOWN")) downPressed = false;
            if (action.equals("LEFT")) leftPressed = false;
            if (action.equals("RIGHT")) rightPressed = false;

            if (action.equals("W")) wPressed = false;
            if (action.equals("S")) sPressed = false;
            if (action.equals("A")) aPressed = false;
            if (action.equals("D")) dPressed = false;
        }
    }

    private static void bindKey(JPanel myPanel, String input) {
        System.out.println("Key bound");

        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " + input), input + " pressed");
        myPanel.getActionMap().put(input + " pressed", new KeyPressed(input));

        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + input), input + " released");
        myPanel.getActionMap().put(input + " released", new KeyReleased(input));
    }
    private static void drawClock() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        DecimalFormat df = new DecimalFormat("##.##");
        long end = System.currentTimeMillis();
        float sec = (end - start) / 1000f;
        Stroke oldStroke = g2d.getStroke();

        g2d.setColor(Color.ORANGE);
        g2d.fillRect(30, 40, 275, 115);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(30, 40, 275, 115);
        g2d.setStroke(oldStroke);
        g2d.setFont(new Font("ARIAL", Font.BOLD, 26));
        g2d.drawString(df.format(sec) + " seconds", 35, 75);
        g2d.drawString("Player 1 Laps: " + p1CurrentLap + "/" + maxLaps, 35, 110);
        g2d.drawString("Player 2 Laps: " + p2CurrentLap + "/" + maxLaps, 35, 140);
    }

    private static void drawSpeed() {

        Graphics g = appFrame.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillRect(700, 40, 225, 100);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(700, 40, 225, 100);
        DecimalFormat df = new DecimalFormat("###");
        g2d.setFont(new Font("ARIAL", Font.BOLD, 22));
        g2d.drawString("P1 Speed: " + df.format(p1Velocity * 20) + " MPH", 505, 90);
        g2d.drawString("P2 Speed: " + df.format(p2Velocity * 20) + " MPH", 505, 130);
    }

    private static void drawPlayer() {

        Graphics g = appFrame.getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(rotateImageObject(p1).filter(player, null), (int) (p1.getX() + 0.5), (int) (p1.getY() + 0.5), null);
        g2d.drawImage(rotateImageObject(p2).filter(player2, null), (int) (p2.getX() + 0.5), (int) (p2.getY() + 0.5), null);
    }
    private static void drawBarriers() {
        //import graphics
        Graphics g = appFrame.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
    }

    private static AffineTransformOp rotateImageObject(ImageObject obj) {
        AffineTransform at = AffineTransform.getRotateInstance(-obj.getAngle(), obj.getWidth() / 2.0, obj.getyHeight() / 2.0);
        return new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    }

    private static void drawBackground() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background, xOffset, yOffset, null);
    }

    private static class CollisionChecker implements Runnable {
        public void run() {
            while (!endgame) {
                if (collisionOccurs(p1, p2)) {
                    System.out.println("crashed with other player");
                    wav2.setFramePosition(0);
                    wav2.start();
                    p1Velocity = changeVelocity(p1Velocity);
                    p2Velocity = changeVelocity(p2Velocity);
                }
            }
        }


        private static double changeVelocity(double playerVelocity) {
            playerVelocity -= playerVelocity * 2;
            return playerVelocity;
        }
    }

    private static Boolean isInside(double p1x, double p1y, double p2x1, double p2y1, double p2x2, double p2y2)
    {
        Boolean ret = false;
        if(p1x > p2x1 && p1x < p2x2)
        {
            if(p1y > p2y1 && p1y < p2y2)
            {
                ret = true;
            }
            if(p1y > p2y2 && p1y < p2y1)
            {
                ret = true;
            }
        }
        if(p1x > p2x2 && p1x < p2x1)
        {
            if(p1y > p2y1 && p1y < p2y2)
            {
                ret = true;
            }
            if(p1y > p2y2 && p1y < p2y1)
            {
                ret = true;
            }
        }
        return ret;

    }

    private static Boolean collisionOccursCoordinates(double p1x1, double p1y1, double p1x2, double p1y2, double p2x1, double p2y1, double  p2x2, double p2y2)
    {
        Boolean ret = false;
        if(isInside(p1x1, p1y1, p2x1, p2y1, p2x2, p2y2) == true)
        {
            ret = true;
        }
        if(isInside(p1x1, p1y2, p2x1, p2y1, p2x2, p2y2) == true)
        {
            ret = true;
        }
        if(isInside(p1x2, p1y1, p2x1, p2y1, p2x2, p2y2) == true)
        {
            ret = true;
        }
        if(isInside(p1x2, p1y2, p2x1, p2y1, p2x2, p2y2) == true)
        {
            ret = true;
        }
        if(isInside(p2x1, p2y1, p1x1, p1y1, p1x2, p1y2) == true)
        {
            ret = true;
        }
        if(isInside(p2x1, p2y2, p1x1, p1y1, p1x2, p1y2) == true)
        {
            ret = true;
        }
        if(isInside(p2x2, p2y1, p1x1, p1y1, p1x2, p1y2) == true)
        {
            ret = true;
        }
        if(isInside(p2x2, p2y2, p1x1, p1y1, p1x2, p1y2) == true)
        {
            ret = true;
        }
        return ret;

    }

    private static boolean collisionOccurs(ImageObject obj1, ImageObject obj2) {

        System.out.println("P1 x: " + obj1.x + ", P1 y: " + obj1.y);
        System.out.println("P2 x: " + obj2.x + ", P2 y: " + obj2.y);
        System.out.println();
        System.out.println();
        try {
            Thread.sleep(800);
        } catch (InterruptedException ie) {
            System.out.println("ie");
        }

        return collisionOccursCoordinates(obj1.getX(), obj1.getY(), obj1.getX() + obj1.getWidth(),
                obj1.getY() + obj1.getyHeight(), obj2.getX(), obj2.getY(), obj2.getX() + obj2.getWidth(),
                obj2.getY() + obj2.getyHeight());
    }

    private static class ImageObject {

        private double x;
        private double y;
        private double xWidth;
        private double yHeight;
        private double angle;
        private double internalAngle;
        private ArrayList<Double> coords;
        private ArrayList<Double> triangles;
        private double comX;
        private double comY;

        public ImageObject() {
        }

        public ImageObject(double xInput, double yInput, double xWidthInput, double yHeightInput, double angleInput) {
            x = xInput;
            y = yInput;
            xWidth = xWidthInput;
            yHeight = yHeightInput;
            angle = angleInput;
            internalAngle = 0.0;
            coords = new ArrayList<Double>();
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return xWidth;
        }

        public double getyHeight() {
            return yHeight;
        }

        public double getAngle() {
            return angle;
        }

        public double getInternalAngle() {
            return internalAngle;
        }

        public void setAngle(double angleInput) {
            angle = angleInput;
        }

        public void setInternalAngle(double interalAngleInput) {
            internalAngle = interalAngleInput;
        }

        public ArrayList<Double> getCoords() {
            return coords;
        }

        public void setCoords(ArrayList<Double> coordsInput) {
            coords = coordsInput;
            generateTriangles();
            //printTriangles();
        }

        public void generateTriangles() {
            triangles = new ArrayList<Double>();
            comX = getComX();
            comY = getComY();

            for (int i = 0; i < coords.size(); i = i + 2) {
                triangles.add(coords.get(i));
                triangles.add(coords.get(i + 1));

                triangles.add(coords.get((i + 2) % coords.size()));
                triangles.add(coords.get((i + 3) % coords.size()));

                triangles.add(comX);
                triangles.add(comY);
            }
        }

        public void printTriangles() {
            for (int i = 0; i < triangles.size(); i = i + 6) {
                System.out.print("p0x: " + triangles.get(i) + ", p0y: " + triangles.get(i + 1));
                System.out.print("p1x: " + triangles.get(i + 2) + ", p1y: " + triangles.get(i + 3));
                System.out.print("p2x: " + triangles.get(i + 4) + ", p2y: " + triangles.get(i + 5));

            }
        }

        public double getComX() {
            double ret = 0;

            if (coords.size() > 0) {
                for (int i = 0; i < coords.size(); i = i + 2) {
                    ret += coords.get(i);
                }
                ret /= (coords.size() / 2.0);
            }
            return ret;
        }

        public double getComY() {
            double ret = 0;

            if (coords.size() > 0) {
                for (int i = 1; i < coords.size(); i = i + 2) {
                    ret += coords.get(i);
                }
                ret /= (coords.size() / 2.0);
            }
            return ret;
        }

        public void move(double xinput, double yinput) {
            x += xinput;
            y += yinput;
        }

        public void moveTo(double xinput, double yinput) {
            x = xinput;
            y = yinput;
        }
        public void screenWrap(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
            if (x > rightEdge) {
                moveTo(leftEdge, getY());
            }
            if (x < leftEdge) {
                moveTo(rightEdge, getY());
            }
            if (y > bottomEdge) {
                moveTo(getX(), topEdge);
            }
            if (y < topEdge) {
                moveTo(getX(), bottomEdge);
            }
        }

        public void rotate(double angleInput) {
            angle += angleInput;
            while (angle > twoPi) {
                angle -= twoPi;
            }
            while (angle < 0) {
                angle += twoPi;
            }
        }

        public void spin(double internalAngleInput) {
            internalAngle += internalAngleInput;
            while (internalAngle > twoPi) {
                internalAngle -= twoPi;
            }
            while (internalAngle < 0) {
                internalAngle += twoPi;
            }
        }
    }
}