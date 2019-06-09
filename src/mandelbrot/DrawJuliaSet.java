/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mandelbrot;

/**
 *
 * @author Taush Sampley
 */
//yea yea bad programming style....  -.-
import javax.swing.JFrame;
import java.awt.geom.Point2D;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class DrawJuliaSet extends DrawSet {
    private Point2D.Double c;
    private JFrame frame;
    public DrawJuliaSet(int width, int height, double a, double b, JFrame parent) {
        super(width, height);
    	c = new Point2D.Double(a, b);
        frame = parent;
    }
    public MouseListener getMouseListener() {
        return null;
    }
    public KeyListener getKeyListener() {
        return new PressListener();
    }
    private class PressListener implements KeyListener {
    	public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case (KeyEvent.VK_SPACE):
                    System.out.println((initialWidth / (end.x - start.x)) + "\n" + start.x + " " + start.y + "i   ~   " + end.x + " " + end.y + "i");
                    System.out.println(c.x + " + " + c.y);
                    break;
                case (KeyEvent.VK_UP):
                    shiftUp(-1);
                    break;
                case (KeyEvent.VK_DOWN):
                    shiftUp(1);
                    break;
                case (KeyEvent.VK_RIGHT):
                    shiftRight(1);
                    break;
                case (KeyEvent.VK_LEFT):
                    shiftRight(-1);
                    break;
                case (KeyEvent.VK_Z):
                    zoomIn();
                    break;
                case (KeyEvent.VK_X):
                    zoomOut();
                    break;
                case (KeyEvent.VK_S):
                    save("Julia");
                    break;
                case (KeyEvent.VK_G):
                    design = GRADIENT;
                    recolor();
                    break;
                case (KeyEvent.VK_B):
                    design = BLACK_AND_WHITE;
                    recolor();
                    break;
                case (KeyEvent.VK_R):
                    design = RANDOM;
                    recolor();
                    break;
                case (KeyEvent.VK_F):
                    design = GRAYSCALE;
                    recolor();
                    break;
                case (KeyEvent.VK_V):
                    design = SHEER;
                    recolor();
                    break;
                case (KeyEvent.VK_P):
                    colorController.setVisible(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    frame.setVisible(false);
                    break;
            }
    	}
    	public void keyReleased(KeyEvent e) {}
    	public void keyTyped(KeyEvent e) {}
    }
    public void updateSet(double a, double b) {
        c.x = a;
        c.y = b;
        System.out.println(c);
        redraw();
    }
    @Override
    public int testPoint(double i, double j) {
        int num = 0;

        double x = i;
        double y = j;

        while (x*x + y*y <= (4) && num < iterations)
        {
            double xtemp = x*x - y*y + c.x;
            y = 2*x*y + c.y;

            x = xtemp;

            num++;
        }
        
        return num;
    }
    public static void main(String[] args) {
    	JFrame frame = new JFrame("Mendelbrot Sets are fun");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	frame.getContentPane().add(new DrawJuliaSet(800, 800, -0.7499962646584729, 0.005703313299747305, frame));

    	frame.pack();
    	frame.setVisible(true);
    }
}
