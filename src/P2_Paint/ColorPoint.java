package P2_Paint;

import java.awt.*;

public class ColorPoint {
    private Color color;
    private int x, y;
    private int strokeSize;

    public ColorPoint(Color color, int x, int y, int strokeSize) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.strokeSize = strokeSize;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStrokeSize() {
        return strokeSize;
    }
}
