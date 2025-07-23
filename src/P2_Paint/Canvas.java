package P2_Paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private final static int STROKE_SIZE = 5;
    private int strokeSize = STROKE_SIZE;

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    // use to draw a line between pints
    private List<ColorPoint> currentPath;

    // color of the dots
    private Color color;

    // location of the dots
    private int x, y;

    // canvas
    private int canvasWidth, canvasHeight;

    public Canvas(int targetWidth, int targetHeight){
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
                Graphics g = getGraphics();
                g.setColor(color);
                g.fillRect(x, y, strokeSize, strokeSize);
                g.dispose();

                // strat current path
                currentPath = new ArrayList<>( 25);
                currentPath.add(new ColorPoint(color, x, y));
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
                Graphics2D g2d = (Graphics2D) getGraphics();
                g2d.setStroke(new BasicStroke(strokeSize));
                g2d.setColor(color);
                if(!currentPath.isEmpty()){
                    ColorPoint prevPoint = currentPath.get(currentPath.size() - 1);
                    g2d.setStroke (new BasicStroke (strokeSize));

                    // connect the current point to the previous point to draw a line
                    g2d.drawLine(prevPoint.getX(), prevPoint.getY(), x, y);
                }

                g2d.dispose();

                // add the new point to the path
                ColorPoint nextPoint = new ColorPoint(color, e.getX(), e.getY());
                currentPath.add(nextPoint);
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void resetCanvas(){
        Graphics g = getGraphics();
        g.clearRect(x, y, canvasWidth, canvasHeight);
        g.dispose();

        currentPath = null;

        repaint();
        revalidate();
    }
}
