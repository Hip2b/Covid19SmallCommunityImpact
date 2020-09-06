
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;

public class FieldStats {


    private HashMap<Class, Counter> counts;

    private boolean countsValid = false;

    public FieldStats(){

        counts = new HashMap<Class, Counter>();
    }

    public String getPopulationDetails(Field field)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counts.keySet()) {
            Counter info = counts.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    public void reset()
    {
        countsValid = false;
        for(Class key : counts.keySet()) {
            Counter count = counts.get(key);
            count.reset();
        }
    }

    public void incrementCount(Class organismClass)
    {
        Counter count = counts.get(organismClass);
        if(count == null) {

            count = new Counter(organismClass);
            counts.put(organismClass, count);
        }

        count.increment();
    }

    public void countFinished()
    {
        countsValid = true;
    }

    public boolean isViable(Field field)
    {
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }

        for(Class key : counts.keySet()) {
            Counter info = counts.get(key);
            if(info.getCount() > 0) {
                nonZero++;
            }
        }

        return nonZero > 1;
    }

    public void generateCounts(Field field)
    {
        reset();
        for(int row = 0; row < field.getHeight(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object organism = field.getObjectAt(col, row);
                if(organism != null) {
                    incrementCount(organism.getClass());
                }
            }
        }
        countsValid = true;
    }

    public Collection<Counter> getCounts() {
        return this.counts.values();
    }
}
