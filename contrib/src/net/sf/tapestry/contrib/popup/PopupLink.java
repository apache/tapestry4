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
 * URL, windowName and windowFeatures for the javascript function:
 * <tt>window.open(URL, windowName, windowFeatures)</tt>.
 *
 *  [<a href="../../../../../../ComponentReference/Form.html">Component Reference</a>]
 * 
 * @version $Id$ 
 * @author Joe Panico
 */
public class PopupLink extends BaseComponent
{
	/** The default popup window name 'popuplink_window'. */
	public static final String DEFAULT_WINDOW_NAME = "popuplink_window";

	//	Instance variables
	private IBinding _urlBinding;
	private IBinding _windowNameBinding;
	private IBinding _featuresBinding;
	private IBinding _heightBinding;
	private IBinding _widthBinding;

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

	public IBinding getFeaturesBinding()
	{
		return _featuresBinding;
	}

	public void setFeaturesBinding(IBinding featuresBinding)
	{
		_featuresBinding = featuresBinding;
	}

	public IBinding getHeightBinding()
	{
		return _heightBinding;
	}

	public void setHeightBinding(IBinding heightBinding)
	{
		_heightBinding = heightBinding;
	}

	public IBinding getWidthBinding()
	{
		return _widthBinding;
	}

	public void setWidthBinding(IBinding widthBinding)
	{
		_widthBinding = widthBinding;
	}

	public String getUrl()
	{
		IBinding aUrlBinding = getUrlBinding();

		if (aUrlBinding != null)
		{
			return URLEncoder.encode(aUrlBinding.getString());
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

	public int getHeight()
	{
		IBinding aHeightBinding = getHeightBinding();
		if (aHeightBinding != null)
		{
			return aHeightBinding.getInt();
		}
		else
		{
			return 0;
		}
	}

	public int getWidth()
	{
		IBinding aWidthBinding = getWidthBinding();
		if (aWidthBinding != null)
		{
			return aWidthBinding.getInt();
		}
		else
		{
			return 0;
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
