package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Float}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class FloatAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "f";

    /**
     *  Registers using the prefix 'f'.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Float.class, this);
    }

    /**
     *  Invoked <code>toString()</code> on data (which is type {@link Float}),
     *  and prefixs the result.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     *  Constructs a {@link Float} from the string, after stripping
     *  the prefix.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Float(string.substring(1));
    }

}