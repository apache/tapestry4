package org.apache.tapestry.contrib.services;

import org.apache.tapestry.contrib.services.impl.RoundedCornerGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 */
public class TestRoundedUtil extends JComponent {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    public TestRoundedUtil()
    {
    }

    public Dimension getPreferredSize() {

        return new Dimension(200, 200);
    }

    public Dimension getMinimumSize() {

        return new Dimension(200,200);
    }
/*
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        RoundedCornerGenerator generator = new RoundedCornerGenerator();
        BufferedImage image = null;

        try {

            image = generator.buildShadow("white", 100, 100, 20f, 20f, 6, 0.5f);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        //g2.setColor(Color.white);
        //g2.fillRect(0, 0, 100, 100);

        g2.drawImage(image, 0, 0, null);
    }
*/
    /*
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        RoundedCornerGenerator generator = new RoundedCornerGenerator();
        BufferedImage image = null;

        try {
            
            image = generator.buildSideShadow("top", 8, 0.5f);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        g2.setColor(Color.white);
        g2.fillRect(0, 0, 100, 100);
        
        g2.drawImage(image, 0, 0, null);
    }
    */

    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        RoundedCornerGenerator generator = new RoundedCornerGenerator();
        BufferedImage image = null;

        try {

            image = generator.buildCorner("6188C7", "white", 6, 6, "tr", -1, -1);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        g2.drawImage(image, 0, 0, null);

        
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Rounded Corner Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TestRoundedUtil id = new TestRoundedUtil();
        frame.getContentPane().add(id, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
