/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.contrib.popup;

import java.net.URLEncoder;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;

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
