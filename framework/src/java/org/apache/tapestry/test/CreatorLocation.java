package org.apache.tapestry.test;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;

/**
 * Fake {@link org.apache.hivemind.Location} used by {@link org.apache.tapestry.test.Creator} when
 * adding properties to a class.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class CreatorLocation implements Location
{
    public String toString()
    {
        return "[Creator Location]";
    }

    public Resource getResource()
    {
        return null;
    }

    public int getLineNumber()
    {
        return 0;
    }

    public int getColumnNumber()
    {
        return 0;
    }

}
