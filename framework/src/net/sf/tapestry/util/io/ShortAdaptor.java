package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Short}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class ShortAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "s";

    /**
     *  Registers using the prefix 's'.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Short.class, this);
    }

    /**
     *  Invoked <code>toString()</code> on data (which is type {@link Short}),
     *  and prefixs the result.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     *  Constructs a {@link Short} from the string, after stripping
     *  the prefix.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Short(string.substring(1));
    }

}