package net.sf.tapestry.util.io;

import java.io.IOException;

import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.Tapestry;

/**
 *  Squeezes a net.sf.tapestry.ComponentAddress.
 * 
 *  @author mindbridge
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class ComponentAddressAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "A";
    private static final char SEPARATOR = '/';
    
    public String squeeze(DataSqueezer squeezer, Object data) throws IOException
    {
        ComponentAddress address = (ComponentAddress) data;
        return PREFIX + address.getPageName() + SEPARATOR + address.getIdPath();
    }

    public Object unsqueeze(DataSqueezer squeezer, String string) throws IOException
    {
        int nSeparator = string.indexOf(SEPARATOR);
        if (nSeparator < 0) 
            throw new IOException(Tapestry.getString("ComponentAddressAdaptor.no-separator"));

        String strPageName = string.substring(1, nSeparator);
        String strIdPath = string.substring(nSeparator + 1);

        return new ComponentAddress(strPageName, strIdPath);
    }

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, ComponentAddress.class, this);
    }

}
