package net.sf.tapestry.util.io;

import java.io.IOException;

/**
 *  Squeezes a Character.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class CharacterAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "c";
    private static final char PREFIX_CH = 'c';
    
    public String squeeze(DataSqueezer squeezer, Object data) throws IOException
    {
        Character charData = (Character)data;
        char value = charData.charValue();
        
        char[] buffer = new char[]
        {
            PREFIX_CH, value
        };
        
        return new String(buffer);
    }

    public Object unsqueeze(DataSqueezer squeezer, String string) throws IOException
    {
        return new Character(string.charAt(1));
    }

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Character.class, this);
    }

}
