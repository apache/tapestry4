/*
 * Created on Apr 20, 2003
 *
 */
package org.apache.tapestry.junit.mock.c25;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 * @author mindbridge
 */
public class Home extends BasePage
{

    public void directListener(IRequestCycle cycle)
    {
        cycle.activate("Two");
    }


}
