/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mandelbrot;

import javax.swing.JFrame;

/**
 *
 * @author Owner
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	JFrame frame = new JFrame("Mendelbrot Sets are fun");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        DrawSet set = new DrawMandelbrotSet(dim.width, dim.height);
//        DrawSet set = new DrawMandelbrotSet(300, 300);
    	frame.getContentPane().add(set);

        frame.setUndecorated(true);
    	frame.pack();
    	frame.setVisible(true);
        set.redraw();
    }
}