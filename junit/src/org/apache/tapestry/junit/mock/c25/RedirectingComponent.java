/*
 * Created on Apr 20, 2003
 *
 */
package org.apache.tapestry.junit.mock.c25;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

/**
 * @author teo
 */
public class RedirectingComponent extends BaseComponent implements PageValidateListener
{

    /**
	 * @see org.apache.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();
        getPage().addPageValidateListener(this);
	}

	/**
	 * @see org.apache.tapestry.event.PageValidateListener#pageValidate(org.apache.tapestry.event.PageEvent)
	 */
	public void pageValidate(PageEvent event)
	{
        String pageName = getBinding("page").getString();
        throw new PageRedirectException(pageName);
	}

}
