//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.popup;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.net.URLCodec;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.Tapestry;

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
    private static final URLCodec _urlCodec = new URLCodec();

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
            String encoding = getPage().getEngine().getOutputEncoding();
            try
            {
                return _urlCodec.encode(aHrefBinding.getString(), encoding);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.format("illegal-encoding", encoding),
                    e);
            }
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
