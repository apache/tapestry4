package net.sf.tapestry.util.io;

/**
 *  Squeezes a {@link Boolean}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class BooleanAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "TF";

    /**
     *  Registers using the prefixes 'T' and 'F' (for TRUE and FALSE).
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Boolean.class, this);
    }

    /**
     *  Squeezes the {@link Boolean} data to either 'T' or 'F'.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        Boolean bool = (Boolean) data;

        if (bool.booleanValue())
            return "T";
        else
            return "F";
    }

    /**
     *  Unsqueezes the string to either {@link Boolean#TRUE} or {@link Boolean#FALSE},
     *  depending on the prefix character.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        char ch = string.charAt(0);

        if (ch == 'T')
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

}