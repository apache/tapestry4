package tutorial.portal;

import java.io.Serializable;

/**
 *  Stores information about a single Stock.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Stock implements Serializable
{
    private String _tickerId;
    private double _price;
    private double _change;

    public Stock()
    {
    }

    public Stock(String tickerId, double price, double change)
    {
        _tickerId = tickerId;
        _price = price;
        _change = change;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Stock[");
        buffer.append(_tickerId);
        buffer.append(' ');
        buffer.append(_price);
        buffer.append(' ');
        buffer.append(_change);
        buffer.append(']');

        return buffer.toString();
    }
    
    public double getChange()
    {
        return _change;
    }

    public double getPrice()
    {
        return _price;
    }

    public String getTickerId()
    {
        return _tickerId;
    }

}