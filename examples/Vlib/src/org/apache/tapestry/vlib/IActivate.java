package org.apache.tapestry.vlib;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 *  Interface used for pages that may be activated by the Border component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public interface IActivate extends IPage
{
	public void activate(IRequestCycle cycle);
}
