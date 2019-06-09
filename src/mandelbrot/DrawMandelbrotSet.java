
package mandelbrot;

import javax.swing.JFrame;
import java.awt.event.*;

/**
 *
 * @author Taush Sampley
 */
public class DrawMandelbrotSet extends DrawSet {

    protected JFrame child;
    protected DrawJuliaSet childGuts;

    public DrawMandelbrotSet(int w, int h) {
        super(w, h);
        
        child = new JFrame();
        childGuts = new DrawJuliaSet(dim.width, dim.height, -0.7499962646584729, 0.005703313299747305, child);
        child.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        child.getContentPane().add(childGuts);
        child.setUndecorated(true);
        child.pack();
    }

    public MouseListener getMouseListener() {
        return new ClickListener();
    }
    public KeyListener getKeyListener() {
        return new PressListener();
    }
    protected class ClickListener implements MouseListener {
        public void mouseClicked(MouseEvent e){
            double x, y;

            x = start.x + e.getPoint().x * (end.x - start.x) / wide;
            y = end.y - e.getPoint().y * (end.y - start.y) / tall;

            child.setVisible(true);
            childGuts.updateSet(x, y);
        }
    	public void mousePressed(MouseEvent e){}
    	public void mouseReleased(MouseEvent e){}
    	public void mouseEntered(MouseEvent e){}
    	public void mouseExited(MouseEvent e){}
    }
    protected class PressListener implements KeyListener {
    	public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case (KeyEvent.VK_SPACE):
                    System.out.println((initialWidth / (end.x - start.x)) + "\n" + start.x + " " + start.y + "i   ~   " + end.x + " " + end.y + "i");
                    break;
                case (KeyEvent.VK_UP):
                    shiftUp(1);
                    break;
                case (KeyEvent.VK_DOWN):
                    shiftUp(-1);
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
                case (KeyEvent.VK_W):
                    zoom.y+=zoomFactor;
                    System.out.println(zoom);
                    redraw();
                    break;
                case (KeyEvent.VK_S):
                    zoom.y-=zoomFactor;
                    System.out.println(zoom);
                    redraw();
                    break;
                case (KeyEvent.VK_A):
                    zoom.x-=zoomFactor;
                    System.out.println(zoom);
                    redraw();
                    break;
                case (KeyEvent.VK_D):
                    zoom.x+=zoomFactor;
                    System.out.println(zoom);
                    redraw();
                    break;
                case (KeyEvent.VK_T):
                    iterations*=10;
                    System.out.println("Iterations: " + iterations);
                    redraw();
                    break;
                case (KeyEvent.VK_Y):
                    iterations/=10;
                    System.out.println("Iterations: " + iterations);
                    redraw();
                    break;
                case (KeyEvent.VK_L):
                    zoomFactor/=10;
                    System.out.println("Zoom factor: " + zoomFactor);
                    break;
                case (KeyEvent.VK_M):
                    zoomFactor*=10;
                    System.out.println("Zoom factor: " + zoomFactor);
                    break;
                case (KeyEvent.VK_ESCAPE):
                    System.exit(0);
                    break;
                case (KeyEvent.VK_Q):
                    save("Mandelbrot");
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
            }
        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }
    public int testPoint(double i, double j) {
        int num = 0;

        double x = zoom.x;
        double y = zoom.y;

        while (x*x + y*y <= (4) && num < iterations) {
            double xtemp = x*x - y*y + i;
            y = 2*x*y + j;

            x = xtemp;

            num++;
        }

        return num;
    }
}
