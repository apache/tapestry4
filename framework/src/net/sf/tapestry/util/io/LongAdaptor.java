package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Long}. 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class LongAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "l";

    /**
     *  Registers using the prefix 'l'.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Long.class, this);
    }

    /**
     *  Invoked <code>toString()</code> on data (which is type {@link Long}),
     *  and prefixs the result.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     *  Constructs a {@link Long} from the string, after stripping
     *  the prefix.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Long(string.substring(1));
    }

}