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
 * This component provides a popup link to launch a new window using a given
 * href, windowName and windowFeatures for the javascript function:
 * <tt>window.open(URL, windowName, windowFeatures)</tt>.
 *
 *  [<a href="../../../../../../ComponentReference/contrib.PopupLink.html">Component Reference</a>]
 * 
 * @version $Id$ 
 * @author Joe Panico
 */
public class PopupLink extends BaseComponent
{
	/** The default popup window name 'popuplink_window'. */
	public static final String DEFAULT_WINDOW_NAME = "popuplink_window";

	//	Instance variables
	private IBinding _hrefBinding;
	private IBinding _windowNameBinding;
	private IBinding _featuresBinding;

	public IBinding getHrefBinding()
	{
		return _hrefBinding;
	}

	public void setHrefBinding(IBinding hrefBinding)
	{
		_hrefBinding = hrefBinding;
	}

	public IBinding getWindowNameBinding()
	{
		return _windowNameBinding;
	}

	public void setWindowNameBinding(IBinding windowNameBinding)
	{
		_windowNameBinding = windowNameBinding;
	}

	public IBinding getFeaturesBinding()
	{
		return _featuresBinding;
	}

	public void setFeaturesBinding(IBinding featuresBinding)
	{
		_featuresBinding = featuresBinding;
	}

	public String getHref()
	{
		IBinding aHrefBinding = getHrefBinding();

		if (aHrefBinding != null)
		{
			return URLEncoder.encode(aHrefBinding.getString());
		}

		return null;
	}

	public String getWindowName()
	{
		IBinding aWindowNameBinding = getWindowNameBinding();
		if (aWindowNameBinding != null)
		{
			return aWindowNameBinding.getString();
		}
		else
		{
			return DEFAULT_WINDOW_NAME;
		}
	}

	public String getFeatures()
	{
		IBinding aFeaturesBinding = getFeaturesBinding();
		if (aFeaturesBinding != null)
		{
			return aFeaturesBinding.getString();
		}
		else
		{
			return "";
		}
	}
    
    public String getPopupFunctionName()
    {
        return getIdPath().replace('.', '_') + "_popup";
    }
}
