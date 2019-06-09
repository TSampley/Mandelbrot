
package mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Taush Sampley
 */

public abstract class DrawSet extends JPanel {
    protected final int BLACK_AND_WHITE = 0, RANDOM = 1, GRADIENT = 2, GRAYSCALE = 3, SHEER = 4;
    protected final int[][] raster;
    protected int countEach[] = new int[4];
    protected int design;
    protected Color[] randomColors;
    protected Color[] colors;
    protected Color[] BWcolors;

    BufferedImage image;
    Graphics ref;
    Dimension dim;

    protected final double initialWidth;
    protected Point2D.Double start, end;
    protected int wide, tall;

    protected Point2D.Double zoom = new Point2D.Double(0, 0);
    protected double zoomFactor = .1;
    protected double iterations = 1000;

    protected JFrame colorController;
    protected ColorArrayCreator colorGuts;

    public DrawSet(int width, int height) {
    	System.out.println("++DrawSet");
        if (width > height) {
            double f = (double)width/height;
            start = new Point2D.Double(-f, -1);
            end = new Point2D.Double(f, 1);
        } else {
            double f = (double)height/width;
            start = new Point2D.Double(-1, -f);
            end = new Point2D.Double(1, f);
        }
    	initialWidth = end.x - start.x;

    	wide = width;
    	tall = height;
        raster = new int[width][height];

    	design = GRADIENT;
    	setupColors();
        image = new BufferedImage(wide, tall, BufferedImage.TYPE_INT_RGB);
        ref = image.getGraphics();

        dim = new Dimension(wide, tall);

        colorController = new JFrame();
        colorController.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        colorController.getContentPane().add(colorGuts = new ColorArrayCreator(this));
        colorController.pack();

    	addMouseListener(getMouseListener());
        requestFocus();
        setFocusable(true);
	addKeyListener(getKeyListener());

    	setPreferredSize(dim);
    	System.out.println("--DrawSet");
    }
    protected void setupColors() {
        randomColors = new Color[1000];
    	for (int i = 0; i < 1000; i++)
    		randomColors[i] = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));

        colors = makeColorArray(80, Color.black, Color.red, Color.yellow, Color.white);
        //colors = makeColorArray(80, Color.black, Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta);
        BWcolors = makeColorArray(255, Color.black, Color.white);
    }
    public abstract MouseListener getMouseListener();
    public abstract KeyListener getKeyListener();
    public abstract int testPoint(double i, double j);
    protected void shiftUp(int dir) {
    	double disty = -dir * (end.y - start.y) / 4;

    	start.y -= disty;
    	end.y -= disty;

    	redraw();
    }

    protected void shiftRight(int dir) {
    	double distx = dir * (end.x - start.x) / 4;

    	start.x += distx;
    	end.x += distx;

    	redraw();
    }

    protected void zoomIn() {
    	double distx = (end.x - start.x) / 8;
    	double disty = (end.y - start.y) / 8;

    	start.x += distx;
    	end.x -= distx;
    	start.y += disty;
    	end.y -= disty;

    	redraw();
    }
    
    protected void zoomOut() {
    	double distx = (end.x - start.x) / 4;
    	double disty = (end.y - start.y) / 4;

    	start.x -= distx;
    	end.x += distx;
    	start.y -= disty;
    	end.y += disty;

    	redraw();
    }
    protected final void save(String prefix) {
        System.out.println("Save Initialized");
        try {
            File picturePath;
            switch (design) {
                case (BLACK_AND_WHITE):
                    picturePath = getAvailableFileInstance("Black and White", prefix);
                    break;
                case (RANDOM):
                    picturePath = getAvailableFileInstance("Random", prefix);
                    break;
                case (GRADIENT):
                    picturePath = getAvailableFileInstance("Gradient", prefix);
                    break;
                default:  //grayscale
                    picturePath = getAvailableFileInstance("Grayscale", prefix);
                    break;
            }
            String str = JOptionPane.showInputDialog("Enter the scaling factor for the image.");
            double d = Double.parseDouble(str);
            BufferedImage im = new BufferedImage((int)(wide*d), (int)(tall*d), BufferedImage.TYPE_INT_RGB);
            System.out.println(d);
            render(getGraphics(), im.getGraphics(), im.getWidth(), im.getHeight());
            System.out.println("Writing Path\\File: " + picturePath);
            ImageIO.write(im, "PNG", picturePath);
        } catch (IOException excep) {}
        getGraphics().drawImage(image, 0, 0, null);
        System.out.println("done");
    }
    public File getAvailableFileInstance(String folderName, String prefix) {
        File folder = new File(folderName);
        if (!folder.exists())
            folder.mkdir();
        File picturePath = new File(folderName + "/" + countEach[design] + ".png");
        countEach[design]++;
        while (picturePath.exists()) {
            picturePath = new File(folderName + "/" + countEach[design] + ".png");
            countEach[design]++;
        }
        return picturePath;
    }
    @Override
    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        page.drawImage(image, 0, 0, null);
    }
    public final void redraw() {
        Graphics page;
        while((page = getGraphics()) == null) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }
        
        renderToRaster(page, ref);
        
        page.drawImage(image, 0, 0, null);
    }
    public final void recolor() {
        Graphics page = getGraphics();
        for (int i = 0; i < wide; i++)
            for (int j = 0; j < tall; j++) {
                switch (design) {
                    case (BLACK_AND_WHITE):
                        ref.setColor(new Color((int)(raster[i][j] * 255 / iterations), (int)(raster[i][j] * 255 / iterations), (int)(raster[i][j] * 255 / iterations)));
                        break;
                    case (RANDOM):
                        ref.setColor(randomColors[raster[i][j]%1000]);
                        break;
                    case (GRADIENT):
                        ref.setColor(colors[raster[i][j] % colors.length]);
                        break;
                    case (GRAYSCALE):
                        ref.setColor(BWcolors[raster[i][j] % BWcolors.length]);
                        break;
                    case (SHEER):
                        ref.setColor(Color.gray);
                        break;
                }
                if (raster[i][j] == iterations)
                    ref.setColor(Color.black);
                ref.drawLine(i, tall - j - 1, i, tall - j - 1);
            }
        page.drawImage(image, 0, 0, null);
    }
    public final void renderToRaster(Graphics page, Graphics ref) {
    	int pixelY = 0;
    	// algorithm here
    	double incrementX = (end.x - start.x) / wide;
    	double incrementY = (end.y - start.y) / tall;

    	for (double i = start.y; i < end.y; i += incrementY)
    	{
            int pixelX = 0;
            for (double j = start.x; j < end.x; j += incrementX)
            {
                int iteration = raster[pixelX == wide ? wide-1 : pixelX][pixelY == tall ? tall-1 : pixelY] = testPoint(j, i);

                if (iteration == iterations)
                    ref.setColor(Color.black);
                else
                {
                    switch (design)
                    {
                        case (BLACK_AND_WHITE):
                            ref.setColor(new Color((int)(iteration * 255 / iterations), (int)(iteration * 255 / iterations), (int)(iteration * 255 / iterations)));
                            break;
                        case (RANDOM):
                            ref.setColor(randomColors[iteration%1000]);
                            break;
                        case (GRADIENT):
                            ref.setColor(colors[iteration % colors.length]);
                            break;
                        case (GRAYSCALE):
                            ref.setColor(BWcolors[iteration % BWcolors.length]);
                            break;
                        case (SHEER):
                            ref.setColor(Color.gray);
                            break;
                    }
                }

                ref.drawLine(pixelX, tall - pixelY-1, pixelX, tall - pixelY-1);

                pixelX++;
            }
            page.setColor(Color.green);
            page.drawLine(0, tall-pixelY, 5, tall-pixelY);
            pixelY++;
    	}
        page.drawImage(image, 0, 0, null);
    }

    public final void render(Graphics page, Graphics ref, int w, int h) {
    	int pixelY = 0;
    	// algorithm here
    	double incrementX = (end.x - start.x) / w;
    	double incrementY = (end.y - start.y) / h;

    	for (double i = start.y; i < end.y; i += incrementY) {
            int pixelX = 0;
            for (double j = start.x; j < end.x; j += incrementX) {
                int iteration = testPoint(j, i);

                if (iteration == iterations)
                    ref.setColor(Color.black);
                else {
                    switch (design) {
                        case (BLACK_AND_WHITE):
                            ref.setColor(new Color((int)(iteration * 255 / iterations), (int)(iteration * 255 / iterations), (int)(iteration * 255 / iterations)));
                            break;
                        case (RANDOM):
                            ref.setColor(randomColors[iteration%1000]);
                            break;
                        case (GRADIENT):
                            ref.setColor(colors[iteration % colors.length]);
                            break;
                        case (GRAYSCALE):
                            ref.setColor(BWcolors[iteration % BWcolors.length]);
                            break;
                        case (SHEER):
                            ref.setColor(Color.gray);
                            break;
                    }
                }

                ref.drawLine(pixelX, h - pixelY-1, pixelX, h - pixelY-1);

                pixelX++;
            }
            page.setColor(Color.green);
            page.drawLine(0, tall-tall*pixelY/h, 5, tall-tall*pixelY/h);
            pixelY++;
    	}
    }

    public static Color[] makeColorArray(int definition, Color start, Color ... colors) {
        System.out.println("MAKEARRAY");
        System.out.println(start);
        Color[] colorArr = new Color[(colors.length+1)*definition];
        double r = start.getRed(), g = start.getGreen(), b = start.getBlue(),
               rStep = 0, bStep = 0, gStep = 0;
        int offset = 0;
        for (Color c : colors) {
            System.out.println(c);
            rStep = (c.getRed() - r) / definition;
            gStep = (c.getGreen() - g) / definition;
            bStep = (c.getBlue() - b) / definition;
            for (int i = 0; i < definition; i++) {
                colorArr[i+offset] = new Color((int)r, (int)g, (int)b);
                r+=rStep;
                g+=gStep;
                b+=bStep;
            }
            r = c.getRed();
            g = c.getGreen();
            b = c.getBlue();
            offset+=definition;
        }
        rStep = (start.getRed() - r) / definition;
        gStep = (start.getGreen() - g) / definition;
        bStep = (start.getBlue() - b) / definition;
        for (int i = 0; i < definition; i++) {
            colorArr[i+offset] = new Color((int)r, (int)g, (int)b);
            r+=rStep;
            g+=gStep;
            b+=bStep;
        }

        return colorArr;
    }
    
    public final void resetGradientArray(int def, Color clr, Color[] clrs) {
        colors = makeColorArray(def, clr, clrs);
        recolor();
    }
}
