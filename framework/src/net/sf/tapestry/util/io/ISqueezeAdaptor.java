package net.sf.tapestry.util.io;

import java.io.IOException;

/**
 *  Interface which defines a class used to convert data for a specific
 *  Java type into a String format (squeeze it),
 *  or convert from a String back into a Java type (unsqueeze).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface ISqueezeAdaptor
{
    /**
     *  Converts the data object into a String.
     *
     *  @throws IOException if the object can't be converted.
     **/

    public String squeeze(DataSqueezer squeezer, Object data) throws IOException;

    /**
     *  Converts a String back into an appropriate object.
     *
     *  @throws IOException if the String can't be converted.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
        throws IOException;

    /**
     *  Invoked to ask an adaptor to register itself to the squeezer.
     *
     **/

    public void register(DataSqueezer squeezer);
}