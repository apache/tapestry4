package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Byte}. 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class ByteAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "b";

    /**
     *  Registers using the prefix 'b'.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Byte.class, this);
    }

    /**
     *  Invoked <code>toString()</code> on data (which is type {@link Byte}),
     *  and prefixs the result.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     *  Constructs an {@link Byte} from the string, after stripping
     *  the prefix.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Byte(string.substring(1));
    }

}