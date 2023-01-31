import java.util.Vector;
import java.util.Random;

import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Racer {
    public Racer() {setup();}

    public static void setup() {
        appFrame = new JFrame("Racer");
        XOFFSET = 0;
        YOFFSET = 40;
        // Changed the window height and width for png
        WINWIDTH = 480;
        WINHEIGHT = 480;
        pi = 3.14159265358979;
        twoPi = 2.0 * 3.14159265358979;
        endgame = false;
        // TODO: Make sure that entities' width and height match the png width and height of our track


        //TODO Define all other variables needed at setup

        //TODO Read in images
    }

    public static class Animate implements Runnable {
        public void run() {
            while (endgame == false) {
                //TODO Replace draw functions below with ours for racer game
//                backgroundDraw();
//                asteroidsDraw();
//                explosionsDraw();
//                enemyBulletsDraw();
//                enemyDraw();
//                playerBulletsDraw();
//                playerDraw();
//                flameDraw();
                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class PlayerMover implements Runnable {
        public PlayerMover() {
            velocitystep = 0.01;
            rotatestep = 0.01;
        }

        public void run() {
            //TODO Add Racer controls. Look at PlayerMover in Asteroids
            //Note Because we have two players we have to pass in a player object or create multiple instances of PlayerMover
        }

        private double velocitystep;
        private double rotatestep;
    }

    //TODO If the racetrack takes up the entire window this isn't needed but if we have the track zoomed in we need a TrackMover class similar to PlayerMover


    //TODO CollisionChecker class look at the one in Asteroids


    private static class WinChecker implements Runnable {
        public void run() {
            while (endgame == false) {
                //TODO Add win condition
            }
        }
    }

    //TODO Add Draw functions

    private static class KeyPressed extends AbstractAction{
        public KeyPressed(){
            action = "";
        }
        public KeyPressed(String input){
            action = input;
        }
        public void actionPerformed(ActionEvent e){
            if(action.equals("UP")){
                upPressed = true;
            }
            if(action.equals("DOWN")){
                downPressed = true;
            }
            if(action.equals("LEFT")){
                leftPressed = true;
            }
            if(action.equals("RIGHT")){
                rightPressed = true;
            }
            if(action.equals("F")){
                firePressed = true;
            }
        }
        private String action;
    }

    public static class KeyReleased extends AbstractAction{
        public KeyReleased(){
            action = "";
        }
        public KeyReleased(String input){
            action = input;
        }

        public void actionPerformed(ActionEvent e ){
            if(action.equals("UP")){
                upPressed = false;
            }
            if(action.equals("DOWN")){
                downPressed = false;
            }
            if(action.equals("LEFT")){
                leftPressed = false;
            }
            if(action.equals("RIGHT")){
                rightPressed = false;
            }
            if(action.equals("F")){
                firePressed = false;
            }
        }
        private String action;
    }

    private static class QuitGame implements ActionListener{
        public void actionPerformed(ActionEvent e){
            endgame = true;
        }
    }

    private static class StartGame implements ActionListener{
        public void actionPerformed (ActionEvent e){
            endgame = true;
            enemyAlive = true;
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            firePressed = false;
            p1 = new ImageObject(p1originalX, p1originalY, p1width, p1height, 0.0);
            p1velocity = 0.0;
            //TODO Replace code below with Racer functions
//            generateEnemy();
//            flames = new ImageObject(p1originalX + p1width/2.0, p1originalY+p1height, flamewidth, flamewidth, 0.0);
//            flamecount = 1;
//            expcount = 1;
//            try{
//                Thread.sleep(50);
//            }catch (InterruptedException ie){
//                ie.printStackTrace();
//            }
//            playerBullets = new Vector<ImageObject>();
//            playerBulletsTimes = new Vector<Long>();
//            enemyBullets = new Vector<ImageObject>();
//            enemyBulletsTimes = new Vector<Long>();
//            explosions = new Vector<ImageObject>();
//            explosionsTimes = new Vector<Long>();
//            generateAsteroids();
//            endgame = false;
//
//            Thread t1 = new Thread(new Animate());
//            Thread t2 = new Thread(new PlayerMover());
//            Thread t3 = new Thread(new FlameMover());
//            Thread t4 = new Thread(new AsteroidsMover());
//            Thread t5 = new Thread(new PlayerBulletsMover());
//            Thread t6 = new Thread(new EnemyShipMover());
//            Thread t7 = new Thread(new EnemyBulletsMover());
//            Thread t8 = new Thread(new CollisionChecker());
//            Thread t9 = new Thread(new WinChecker());

//            t1.start();
//            t2.start();
//            t3.start();
//            t4.start();
//            t5.start();
//            t6.start();
//            t7.start();
//            t8.start();
//            t9.start();
        }
    }

    private static class ImageObject{
        public ImageObject (){}

        public ImageObject (double xinput , double yinput , double xwidthinput , double yheightinput , double angleinput){
            x = xinput;
            y = yinput;
            xwidth = xwidthinput;
            yheight = yheightinput;
            angle = angleinput;
            internalangle = 0.0;
            coords = new Vector<Double>();
        }

        public double getX (){
            return x;
        }

        public double getY(){
            return y;
        }


        public double getWidth() {
            return xwidth;
        }
        public double getHeight() {
            return yheight;
        }

        public double getAngle() {
            return angle;
        }

        public double getInternalAngle() {
            return internalangle;
        }

        public void setAngle(double angleinput) {
            angle = angleinput;
        }

        public void setInternalAngle(double internalangleinput) {
            internalangle = internalangleinput;
        }

        public Vector<Double> getCoords() {
            return coords;
        }

        public void setCoords(Vector<Double> coordsinput) {
            coords = coordsinput;
            generateTriangles();
            //printTriangles();
        }

        public void generateTriangles() {
            triangles = new Vector<Double>( ) ;
            // format : (0 , 1) , (2 , 3) , (4 , 5) i s the ( x , y ) coords of a triangle.
            // get center point of all coordinates .
            comX = getComX ( ) ;
            comY = getComY ( ) ;
            for ( int i = 0 ; i < coords.size() ; i = i + 2 )
            {
                triangles.addElement ( coords . elementAt ( i ) ) ;
                triangles.addElement ( coords . elementAt ( i +1) ) ;
                triangles.addElement ( coords . elementAt ( ( i +2) % coords.size( ) )) ;
                triangles.addElement ( coords . elementAt ( ( i +3) % coords.size() )) ;
                triangles.addElement (comX ) ;
                triangles.addElement (comY ) ;
            }
        }

        public void printTriangles() {
            for (int i = 0; i < triangles.size(); i = i + 6) {
                System.out.println("p0x: " + triangles.elementAt(i) + ", p0y: "  +
                        triangles.elementAt(i + 1));
                System.out.println(" p1x: " + triangles.elementAt(i + 2) + ", p1y: "+
                        triangles.elementAt(i + 3));
                System.out.println(" p2x: " + triangles.elementAt(i + 4) + ", p2y: " +
                        triangles.elementAt(i + 5));
            }
        }

        public double getComX() {
            double ret = 0;
            if ( coords.size() > 0) {
                for ( int i = 0; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public double getComY() {
            double ret = 0;
            if ( coords.size() > 0) {
                for ( int i = 1; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public void move(double xinput, double yinput) {
            x = x + xinput;
            y = y + yinput;
        }

        public void moveto(double xinput, double yinput) {
            x = xinput;
            y = yinput;
        }

        public void screenWrap(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
            if (x > rightEdge) {
                moveto( leftEdge, getY() );
            }
            if (x < leftEdge) {
                moveto( rightEdge, getY() );
            }
            if (y > bottomEdge) {
                moveto( getX(), topEdge );
            }
            if (y < topEdge) {
                moveto( getX(), bottomEdge );
            }
        }

        public void rotate(double angleinput) {
            angle = angle + angleinput;
            while ( angle > twoPi) {
                angle = angle - twoPi;
            }

            while ( angle < 0 ) {
                angle = angle + twoPi;
            }
        }

        public void spin(double internalangleinput) {
            internalangle = internalangle + internalangleinput;
            while (internalangle > twoPi) {
                internalangle = internalangle - twoPi;
            }

            while (internalangle < 0) {
                internalangle = internalangle + twoPi;
            }
        }

        private double x;
        private double y;
        private double xwidth;
        private double yheight;
        private double angle; // in Radians
        private double internalangle; // in Radians
        private Vector<Double> coords;
        private Vector<Double> triangles;
        private double comX;
        private double comY;
    }


    private static Boolean endgame;
    private static Boolean enemyAlive;
    private static BufferedImage background;
    private static BufferedImage player;

    private static Boolean upPressed;
    private static Boolean downPressed;
    private static Boolean leftPressed;
    private static Boolean rightPressed;
    private static Boolean firePressed;

    //TODO Declare image object vectors for all player models and track pieces
    private static ImageObject p1;
    private static double p1width;
    private static double p1height;
    private static double p1originalX;
    private static double p1originalY;
    private static double p1velocity;

    private static int XOFFSET;
    private static int YOFFSET;
    private static int WINWIDTH;
    private static int WINHEIGHT;

    private static double pi;
    private static double twoPi;

    private static JFrame appFrame;

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
}
