package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Integer}.  This adaptor claims all the digits as prefix
 *  characters, so its the very simplest conversion of all!
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class IntegerAdaptor implements ISqueezeAdaptor
{
    /**
     *  Registers this adaptor using all nine digits and the minus sign.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register("-0123456789", Integer.class, this);
    }

    /**
     *  Simply invokes <code>toString()</code> on the data,
     *  which is actually type {@link Integer}.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return data.toString();
    }

    /**
     *  Constructs an {@link Integer} from the string.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Integer(string);
    }

}