/*
 * Created on 2004-1-7
 *
 */
package org.apache.tapestry.junit.mock.c9;

import org.apache.tapestry.html.BasePage;

/**
 *  Testing for correct initialization of properties
 *
 *  @author mindbridge
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class Twelve extends BasePage
{
    private int _initialValue = 0;
    
    public int getInitialValue()
    {
        return _initialValue++;
    }

}
