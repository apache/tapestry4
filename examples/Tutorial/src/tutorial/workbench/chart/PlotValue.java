package tutorial.workbench.chart;

import java.io.Serializable;

/**
 *  An single point of data in the plot.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 *
 **/

public class PlotValue implements Serializable
{
    private String name;
    private int value;

    public PlotValue()
    {
    }

    public PlotValue(String name, int value)
    {
        this.name = name;
        this.value = value;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("PlotValue@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append(name);
        buffer.append(' ');
        buffer.append(value);
        buffer.append(']');

        return buffer.toString();
    }
}