import java.util.LinkedHashMap;
import java.util.Map;
import processing.core.*;

public class FieldDisplay {
    private static final int EMPTY_COLOR = 0xFFFFFFFF;

    private static final int UNKNOWN_COLOR = 0x66666666;

    private PApplet p;
    private Field f;
    private int x, y, w, h;
    private float dx, dy;
    private Map<Class, Integer> colors;

    public FieldDisplay(PApplet p, Simulator s) {
        this(p, s.getField());
    }

    public FieldDisplay(PApplet p, Field f) {
        this(p, f, 1, 1, 100, 100);
    }

    public FieldDisplay(PApplet p, Field f, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.p = p;
        this.f = f;

        this.dx = (w / f.getWidth())/3;
        this.dy = h / f.getHeight()+1;

        colors = new LinkedHashMap<Class, Integer>();
    }

    public void drawField(Field f) {
        Object organism;
        Integer organismColor;
        for (int i = 0; i < f.getWidth(); i++) {
            for (int j = 0; j < f.getHeight(); j++) {
                organism = f.getObjectAt(i, j);
                if (organism != null) {
                    organismColor = getColor(organism.getClass());
                    p.fill(organismColor);

                } else {
                    p.fill(this.EMPTY_COLOR);
                }
                p.rect(x + i * dx, y + j * dy, dx, dy);
            }
        }
    }

    public void setColor(Class organismClass, Integer color) {
        colors.put(organismClass, color);
    }


    private Integer getColor(Class organismClass) {
        Integer col = colors.get(organismClass);
        if (col == null) {  // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    public Location gridLocationAt(float mx, float my) {
        if (mx > x && mx < x + w && my > y && my < y+h) {
            return new Location((int)Math.floor((mx-x)/dx),
                    (int)Math.floor((my-y)/dy));
        } else return null;
    }
}
