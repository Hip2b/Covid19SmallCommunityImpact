
import java.awt.Color;

/**
 * Provide a counter for a participant in the simulation.
 * This includes an identifying string and a count of how
 * many participants of this type currently exist within 
 * the simulation.
 * 
 * @author David J. Barnes and Michael Kolling. Modified by David Dobervich 2007-2013
 * @version 2006.03.30
 */
import java.awt.Color;

public class Counter
{
    private String name;
    private Class organismClass;

    private int count;

    public Counter(Class organismClass)
    {
        this.organismClass = organismClass;
        this.name = organismClass.getName();
        count = 0;
    }

    public String getName()
    {
        return name;
    }

    public int getCount()
    {
        return count;
    }

    public void increment()
    {
        count++;
    }

    public void reset()
    {
        count = 0;
    }

    public Class getClassName() {
        return organismClass;
    }
}
