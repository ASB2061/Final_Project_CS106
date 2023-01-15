import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputListener;

/**
 * This scatterplot class uses a sorted arrayheap of sorted houses and then highlights the top 10 of houses to represent
 * theme in a scatterplot graph, where each dot represents a house's distance from a feature and their price per square
 * foot.
 * This code was modified and borrowed by StackOverFlow's user: user3623221
 * Link: https://stackoverflow.com/questions/23709827/drawing-a-scatterplot-in-java
 */
public class Scatterplot extends javax.swing.JFrame {

    private List points = new ArrayList<>();
    private ArrayList<Double> specialPoints = new ArrayList();
    private ArrayList<Ellipse2D> GraphicsPoints = new ArrayList<>();
    private double beta0;
    private double beta1;
    public Scatterplot(String fullList, String topTen) {
        super("Scatterplot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//The following code reads in the two input files given and then uses them
        File source = new File(fullList);
        File source2 = new File(topTen);
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> specialX = new ArrayList<>();

        try {
            Scanner sc = new Scanner(source);
            Scanner sc2 = new Scanner(source2);
            int lineTotal = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] elements = line.split(",");
                x.add(Float.parseFloat(elements[0]));
                y.add(Float.parseFloat(elements[1]));
                lineTotal = lineTotal + 1;
            }
            sc.close();

            while(sc2.hasNextLine()){
                String line = sc2.nextLine();
                specialX.add(Float.parseFloat(line));
                lineTotal= lineTotal + 1;
            }

            for(int i = 0; i < x.size(); i++){
                points.add(new Point2D.Float(x.get(i), y.get(i)));
            }
            for(int i = 0; i < specialX.size(); i++){
                specialPoints.add(Double.parseDouble(String.valueOf(specialX.get(i))));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        class MyPanel extends JPanel implements MouseInputListener {
            public Ellipse2D selected;
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                // for (Ellipse2D ep : GraphicsPoints) {
                //     if (ep.contains(e.getX(), e.getY())){
                //         System.out.println("Mouse press x:" + e.getX() + " y:" + e.getY());
                //     }
                // }
            }

            /**
             * This code detects when a dot is clicked by the mouse
             * @param e is the mouse being clicked becauase there are mouse listeners on the graph
             */
            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                // Point ept = e.getLocationOnScreen();
                for (Ellipse2D ep : GraphicsPoints) {
                    if (ep.contains(e.getX(), e.getY())){
                        this.selected = ep;
                    }

                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                // Point ept = e.getPoint();
                // System.out.println("Mouse entered " + ept.x + " y:" + ept.y);
                // for (int p = 0; p < points.size(); p++) {
                //     Point2D.Float pt = (java.awt.geom.Point2D.Float) points.get(p) ;


            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Mouse entered");
                // Component c = e.getComponent();

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub

            }

        }
        MyPanel panel = new MyPanel() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                //g.translate(0, 0);
                /**
                 * Creates teh rectangle for the graph in black and the labels for the x Axis, Y axis, and title in blue
                 */
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
//                g.setColor(Color.RED);

                g.drawRect(0, 0, getWidth() - 20, getHeight() - 20);
                g.setColor(Color.BLUE);
                g.drawString("Estimating Price per Square Foot Using Distance From Feature" , ((getWidth())/2 )
                        - 150 , 15);
                g.drawString("Distance from Feature", (((getWidth() - 20)/2 ) - 75), (getHeight() - 5));
                g.drawString("$/", (getWidth() - 16), ((getHeight() - 20) /2)- 10);
                g.drawString("sft", (getWidth() - 18), ((getHeight() - 20) /2));
                g.setColor(Color.BLACK);

                //Checks to see if one of the points is a part of the top ten list and then colors it pink, if so
                for (int p = 0; p < points.size(); p++) {
                    g2d.setColor(Color.BLACK);
                        if (((Point2D.Float)points.get(p)).getX() <=
                                Float.parseFloat(String.valueOf(specialPoints.get(specialPoints.size()-1)))) {
                            g2d.setColor(Color.PINK);
                        }
                        Point2D.Float pt = (Point2D.Float) points.get(p) ;
                        Ellipse2D dot = new Ellipse2D.Float((float)(((pt.x +20) * 0.450) -50), (getHeight() -
                                ((float)(pt.y +20 * 0.450) -110)), 10, 10);
                        GraphicsPoints.add(dot);
                        g2d.fill(dot);
                        addMouseListener(this);
                        }
//Rounds the price per square foot so it is in the typical money format. It then prints the string at the bottom of the
                // screen when a dot is pressed, so users can see the x and y values.
                DecimalFormat f = new DecimalFormat("##.00");
                if (this.selected != null){
                    g.drawString("x:" + (((this.selected.getX()/0.450) -20) +50) + " meters" + " y: $" +
                                   f.format(((this.selected.getY()/ 0.450) -20) +110) + " per square foot",
                            ((getWidth() - 20)/2 ) - 175, (getHeight() - 25));
                }
                g2d.dispose();
            }

        };
/**
 * Creates a window for the scatter plot and sets all of the objects on the graph to be visible. Biggest challenge with
 * this code is figuring out how to use the setBounds function so the scale fits the dots and looks correct. Never got
 * this code perfected, but we tried our best for the resources and time we had.
 */
        setContentPane(panel);
        setBounds(200,200,1000,3000);
        setVisible(true);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    }

