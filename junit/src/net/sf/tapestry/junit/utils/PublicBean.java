package net.sf.tapestry.junit.utils;

import java.util.Random;

import net.sf.tapestry.util.prop.IPublicBean;

/**
 *  Bean used by {@link net.sf.tapestry.junit.utils.TestPublicBean}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class PublicBean implements IPublicBean
{
    private static Random r = new Random();
    
    private static long random()
    {
        return r.nextLong();
    }
    
    public String stringProperty = Long.toHexString(random());
    public Object objectProperty = new Long(random());
    public long longProperty = random();
    
    private long privateLongProperty = random();
    
    public double getSyntheticProperty()
    {
        return 3.14;
    }
}
