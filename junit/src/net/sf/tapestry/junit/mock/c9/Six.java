package net.sf.tapestry.junit.mock.c9;

import net.sf.tapestry.html.BasePage;

/**
 *  This is used in a test where the types do not match up.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public abstract class Six extends BasePage
{
	abstract public int getValue();
	
	abstract public void setValue(int value);
}
