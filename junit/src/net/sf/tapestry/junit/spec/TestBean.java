package net.sf.tapestry.junit.spec;

/**
 *  Bean used to test extensions.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TestBean
{
    private boolean _booleanProperty;
    private int _intProperty;
    private long _longProperty;
    private String _stringProperty;
    private double _doubleProperty;
    
    public boolean getBooleanProperty()
    {
        return _booleanProperty;
    }

    public double getDoubleProperty()
    {
        return _doubleProperty;
    }

    public int getIntProperty()
    {
        return _intProperty;
    }

    public long getLongProperty()
    {
        return _longProperty;
    }

    public String getStringProperty()
    {
        return _stringProperty;
    }

    public void setBooleanProperty(boolean booleanProperty)
    {
        _booleanProperty = booleanProperty;
    }

    public void setDoubleProperty(double doubleProperty)
    {
        _doubleProperty = doubleProperty;
    }

    public void setIntProperty(int intProperty)
    {
        _intProperty = intProperty;
    }

    public void setLongProperty(long longProperty)
    {
        _longProperty = longProperty;
    }

    public void setStringProperty(String stringProperty)
    {
        _stringProperty = stringProperty;
    }

}
