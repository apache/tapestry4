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

        // a 'null' id path is encoded as an empty string
        String idPath = address.getIdPath();
        if (idPath == null)
        	idPath = "";

        return PREFIX + address.getPageName() + SEPARATOR + idPath;
    }

    public Object unsqueeze(DataSqueezer squeezer, String string) throws IOException
    {
        int separator = string.indexOf(SEPARATOR);
        if (separator < 0) 
            throw new IOException(Tapestry.getString("ComponentAddressAdaptor.no-separator"));

        String pageName = string.substring(1, separator);
        String idPath = string.substring(separator + 1);
        if (idPath.equals(""))
        	idPath = null;

        return new ComponentAddress(pageName, idPath);
    }

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, ComponentAddress.class, this);
    }

}
