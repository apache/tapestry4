package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Double}. 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class DoubleAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "d";

    /**
     *  Registers using the prefix 'd'.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Double.class, this);
    }

    /**
     *  Invoked <code>toString()</code> on data (which is type {@link Double}),
     *  and prefixs the result.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     *  Constructs an {@link Double} from the string, after stripping
     *  the prefix.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Double(string.substring(1));
    }

}