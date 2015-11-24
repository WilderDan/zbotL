

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *Programmer Abdullah AlDossary
 */
//package hfdragger;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.InputEvent;
/**
 *
 * @author AbduL
 */
public class MouseRobot {

    Robot robot = null;
    /**
     * @param args the command line arguments
     */
    public MouseRobot(){
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(MouseRobot.class.getName()).log(Level.SEVERE, null, ex);
        }  
     }
    
    public void sleep(int mls){
        try {            
                Thread.sleep(mls);
            } catch (InterruptedException ex) {
                Logger.getLogger(OneNoteRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void click(int mask){
        robot.mousePress(mask);
        robot.mouseRelease(mask);
    }
    
    public void drag(int x, int y){
                                      //   x,   y,   n,   s
        new Thread( new MouseMoveThread(   x,   y,  50,   5 ) ).start();
    }

    private static class OneNoteRobot {

        public OneNoteRobot() {
        }
    }
    
    
    
    /*          MouseMoveThread will be created for only one mouse drag, but 
     *      without the mouse click release.
     * 
     */
    private class MouseMoveThread implements Runnable {

        private Robot robot;
        private int startX;
        private int startY;
        private int currentX;
        private int currentY;
        private int xAmount;
        private int yAmount;
        private int xAmountPerIteration;
        private int yAmountPerIteration;
        private int numberOfIterations;
        private long timeToSleep;           //in ms

        public MouseMoveThread( int xAmount, int yAmount,
                int numberOfIterations, long timeToSleep ) {

            this.xAmount = xAmount;
            this.yAmount = yAmount;
            this.numberOfIterations = numberOfIterations;
            this.timeToSleep = timeToSleep;

            try {

                robot = new Robot();

                Point startLocation = MouseInfo.getPointerInfo().getLocation();
                startX = startLocation.x;
                startY = startLocation.y;

            } catch ( AWTException exc ) {
                exc.printStackTrace();
            }

        }

        @Override
        public void run() {
            
            currentX = startX;
            currentY = startY;

            xAmountPerIteration = xAmount / numberOfIterations;
            yAmountPerIteration = yAmount / numberOfIterations;

            robot.mousePress(InputEvent.BUTTON1_MASK);      
            
            while ( currentX < startX + xAmount &&
                    currentY < startY + yAmount ) {         //drag start

                currentX += xAmountPerIteration;
                currentY += yAmountPerIteration;
                
                
                robot.mouseMove( currentX, currentY );

                try {
                    Thread.sleep( timeToSleep );
                } catch ( InterruptedException exc ) {
                    exc.printStackTrace();
                }
                
                
            }
            
            robot.mouseRelease(InputEvent.BUTTON1_MASK);    //after drag
        }   

    }

    
    
    
}
