package net.sf.tapestry.util.io;

/**
 *  Squeezes a String (which is pretty simple, most of the time).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class StringAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "S";
    private static final char PREFIX_CH = 'S';

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, String.class, this);
    }

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        String string = (String) data;
        char ch;

        // An empty String is encoded as an 'S', that is, a String with
        // a length of zero.

        if (string.length() == 0)
            return PREFIX;

        ch = string.charAt(0);

        // If the first character of the string is claimed
        // this or some other adaptor, then prefix it
        // with this adaptor's prefix.

        if (ch == PREFIX_CH || squeezer.isPrefixRegistered(ch))
            return PREFIX + string;
        else
            // Otherwise, the string is OK as is.
            return string;
    }

    /**
     *  Strips the prefix from the string.  This method is only
     *  invoked by the {@link DataSqueezer} if the string leads
     *  with its normal prefix (an 'S').
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        if (string.length() == 1)
            return "";

        return string.substring(1);
    }
}