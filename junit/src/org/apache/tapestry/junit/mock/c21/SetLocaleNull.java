package org.apache.tapestry.junit.mock.c21;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.html.BasePage;

/**
 *  Sets the locale of the engine to null, provoking an exception.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class SetLocaleNull extends BasePage implements IExternalPage
{

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
        throws RequestCycleException
    {
    	getEngine().setLocale(null);
    }

}
