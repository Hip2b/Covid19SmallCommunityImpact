
import java.awt.Color;


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
