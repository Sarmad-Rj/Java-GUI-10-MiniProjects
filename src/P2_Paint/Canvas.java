package P2_Paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Canvas extends JPanel {
    private List<List<ColorPoint>> paths = new ArrayList<>();

    private final static int STROKE_SIZE = 5;
    private int strokeSize = STROKE_SIZE;

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    // use to draw a line between points
    private List<ColorPoint> currentPath;

    // color of the dots
    private Color color;

    // location of the dots
    private int x, y;

    // canvas
    private int canvasWidth, canvasHeight;

    public Canvas(int targetWidth, int targetHeight) {
        super();
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(targetWidth, targetHeight));

        canvasWidth = targetWidth;
        canvasHeight = targetHeight;

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // get current mouse location
                x = e.getX();
                y = e.getY();

                // draw in current mouse location
                currentPath = new ArrayList<>(25);
                currentPath.add(new ColorPoint(color, x, y, strokeSize));
                paths.add(currentPath);
                repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                super.mouseReleased(e);
                currentPath = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // get current mouse location
                x = e.getX();
                y = e.getY();

                // used to be able to draw a new line
                currentPath.add(new ColorPoint(color, x, y, strokeSize));
                repaint();

            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void resetCanvas() {
        paths.clear();
        currentPath = null;
        repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (List<ColorPoint> path : paths) {
            for (int i = 1; i < path.size(); i++) {
                ColorPoint p1 = path.get(i - 1);
                ColorPoint p2 = path.get(i);
                g2d.setColor(p1.getColor());
                g2d.setStroke(new BasicStroke(p1.getStrokeSize()));
                g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }
    }

    public void saveCanvasAsImage(File file) {
        // Create a BufferedImage the size of the canvas
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        // Paint the current canvas onto the BufferedImage
        Graphics2D g2d = image.createGraphics();
        this.paint(g2d); // or paintAll(g2d);
        g2d.dispose();

        try {
            // Write the image to the chosen file as PNG
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage());
        }
    }

}
