package net.sf.tapestry.junit.mock.c6;

import java.io.Serializable;

public class StringHolder implements Serializable
{
    private String _string;

    public StringHolder()
    {
    }

    public StringHolder(String string)
    {
        setString(string);
    }

    public String getString()
    {
        return _string;
    }

    public void setString(String string)
    {
        _string = string;
    }

    public String toString()
    {
        return "StringHolder[" + _string + "]";
    }
}
