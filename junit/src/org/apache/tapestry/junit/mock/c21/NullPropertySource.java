package org.apache.tapestry.junit.mock.c21;

import org.apache.tapestry.engine.IPropertySource;

/**
 *  Empty implementation just to exercise some code paths in
 *  {@link org.apache.tapestry.engine.AbstractEngine}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class NullPropertySource implements IPropertySource
{

    public String getPropertyValue(String propertyName)
    {
        return null;
    }

}
