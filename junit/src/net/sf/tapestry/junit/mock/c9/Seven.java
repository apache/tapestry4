package net.sf.tapestry.junit.mock.c9;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  One giant test page to test all kinds of persistent properties.  Eight
 *  scalar types and an object type.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public abstract class Seven extends BasePage
{
    abstract public boolean getBooleanValue();
    abstract public byte getByteValue();
    abstract public char getCharValue();
    abstract public double getDoubleValue();
    abstract public float getFloatValue();
    abstract public int getIntValue();
    abstract public long getLongValue();
    abstract public short getShortValue();
    abstract public String getStringValue();
    abstract public void setBooleanValue(boolean booleanValue);
    abstract public void setByteValue(byte byteValue);
    abstract public void setCharValue(char charValue);
    abstract public void setDoubleValue(double doubleValue);
    abstract public void setFloatValue(float floatValue);
    abstract public void setIntValue(int intValue);
    abstract public void setLongValue(long longValue);
    abstract public void setShortValue(short shortValue);
    abstract public void setStringValue(String stringValue);

    public void finishLoad()
    {
        setBooleanValue(true);
        setByteValue((byte) 'A');
        setShortValue((short) 97);
        setCharValue('Z');
        setDoubleValue(3.2);
        setFloatValue(-22.7f);
        setIntValue(100);
        setLongValue(32000000);
        setStringValue("Magic");
    }

    public void update(IRequestCycle cycle)
    {
        setBooleanValue(false);
        setByteValue((byte) 'Q');
        setShortValue((short) 21);
        setCharValue('f');
        setDoubleValue(9.87);
        setFloatValue(-202.2f);
        setIntValue(3097);
        setLongValue(132000001);
        setStringValue("Marker");
    }

}
