package net.sf.tapestry.junit.mock.app;

import org.apache.commons.lang.enum.Enum;


/**
 *  
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class AgeRange extends Enum
{
    
    public static final AgeRange CHILD = new AgeRange("CHILD");
    public static final AgeRange TEEN = new AgeRange("TEEN");
    public static final AgeRange ADULT = new AgeRange("ADULT");
    public static final AgeRange RETIREE = new AgeRange("RETIREE");
    public static final AgeRange ELDERLY = new AgeRange("ELDERLY");

    public static AgeRange[] getAllValues()
    {
        return new AgeRange[]
        {
            CHILD, TEEN, ADULT, RETIREE, ELDERLY
        };
    }

    private AgeRange(String name)
    {
        super(name);
    }

}
