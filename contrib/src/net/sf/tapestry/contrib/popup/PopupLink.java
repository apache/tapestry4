//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.popup;

import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 * TODO:
 * - javadoc
 * - port .script to DTD 1.3
 * - component reference page
 * - add demo usage on WorkBench page, possibly as help popup window
 * 
 * @version $Id$ 
 * @author Joe Panico
 */
public class PopupLink extends BaseComponent implements PageDetachListener
{
	// Constants
	private static final String DEFAULT_WINDOW_NAME = "_popup_link_window_";
	private static final Integer DEFAULT_WINDOW_HEIGHT = new Integer(480);
	private static final Integer DEFAULT_WINDOW_WIDTH = new Integer(640);

    private static final Log LOG = LogFactory.getLog(PopupLink.class);

	//	Instance variables
	private IBinding _urlBinding;
	private IBinding _windowNameBinding;
	private IBinding _windowHeightBinding;
	private IBinding _windowWidthBinding;
	private String _popoutFunctionName;
	private String _windowName;
	private Integer _windowHeight;
	private Integer _windowWidth;
	private boolean _needsToGenerateScript = true;

	public IBinding getUrlBinding()
	{
		return _urlBinding;
	}

	public void setUrlBinding(IBinding urlBinding)
	{
		_urlBinding = urlBinding;
	}

	public IBinding getWindowNameBinding()
	{
		return _windowNameBinding;
	}

	public void setWindowNameBinding(IBinding windowNameBinding)
	{
		_windowNameBinding = windowNameBinding;
	}

	public IBinding getWindowHeightBinding()
	{
		return _windowHeightBinding;
	}

	public void setWindowHeightBinding(IBinding windowHeightBinding)
	{
		_windowHeightBinding = windowHeightBinding;
	}

	public IBinding getWindowWidthBinding()
	{
		return _windowWidthBinding;
	}

	public void setWindowWidthBinding(IBinding windowWidthBinding)
	{
		_windowWidthBinding = windowWidthBinding;
	}

	public String getPopupFunctionName()
	{
		if (_popoutFunctionName == null)
		{
			_popoutFunctionName = "_" + getIdPath().replace('.', '_');
		}
		return _popoutFunctionName;
	}

	public String getUrl()
	{
		IBinding aUrlBinding = getUrlBinding();

		if (aUrlBinding != null)
		{
			return (String) aUrlBinding.getObject();
		}

		return null;
	}

	public String getLinkHref()
	{
		// Setting this to false prevents multiple javascript generation
		setNeedsToGenerateScript(false);

		String aUrl = getUrl();
        
        if (aUrl == null)
            return null;
        
		// TTT: find out why this is necessary
		String newUrl = URLEncoder.encode(aUrl);

		return "javascript:" + getPopupFunctionName() + "(\"" + newUrl + "\")";
	}

	public String getWindowName()
	{
		if (_windowName == null)
		{
			IBinding aWindowNameBinding = getWindowNameBinding();
			if (aWindowNameBinding != null)
			{
				_windowName = (String) aWindowNameBinding.getObject();
			} else
			{
				_windowName = DEFAULT_WINDOW_NAME;
			}
		}
		return _windowName;
	}

	public Integer getWindowHeight()
	{
		if (_windowHeight == null)
		{
			IBinding aWindowHeightBinding = getWindowHeightBinding();
			if (aWindowHeightBinding != null)
			{
				_windowHeight = (Integer) aWindowHeightBinding.getObject();
			} else
			{
				_windowHeight = DEFAULT_WINDOW_HEIGHT;
			}
		}
		return _windowHeight;
	}

	public Integer getWindowWidth()
	{
		if (_windowWidth == null)
		{
			IBinding aWindowWidthBinding = getWindowWidthBinding();
			if (aWindowWidthBinding != null)
			{
				_windowWidth = (Integer) aWindowWidthBinding.getObject();
			} else
			{
				_windowWidth = DEFAULT_WINDOW_WIDTH;
			}
		}
		return _windowWidth;
	}

	public boolean getNeedsToGenerateScript()
	{
		return _needsToGenerateScript;
	}

	public void setNeedsToGenerateScript(boolean aValue)
	{
		_needsToGenerateScript = aValue;
	}

    /**
     * @see BaseComponent#finishLoad(IRequestCycle, IPageLoader, ComponentSpecification) 
     */
    public void finishLoad(IRequestCycle cycle, IPageLoader loader,
            ComponentSpecification specification ) throws PageLoaderException
    {
        if (LOG.isInfoEnabled()) 
        {
            LOG.info("finishLoad loader: " + loader 
                     + " componentSpecification: " + specification);
        }

        getPage().addPageDetachListener(this);

        super.finishLoad(cycle, loader, specification);
    }

	/**
	 * @see PageDetachListener#pageDetached(PageEvent)
	 */
	public void pageDetached(PageEvent event)
	{
		_popoutFunctionName = null;
		_windowName = null;
		_windowHeight = null;
		_windowWidth = null;
		_needsToGenerateScript = true;
	}
}
