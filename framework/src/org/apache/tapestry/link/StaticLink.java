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

package org.apache.tapestry.link;

import org.apache.tapestry.engine.ILink;

/**
 *  Used by {@link org.apache.tapestry.link.GenericLink} to represent
 *  an external, static URL.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/
public class StaticLink implements ILink
{
	private String _url;

	public StaticLink(String url)
	{
		_url = url;
	}

    public String getURL()
    {
        return _url;
    }

    public String getURL(String anchor, boolean includeParameters)
    {
        if (anchor == null)
        	return _url;
        	
        	return _url + "#" + anchor;
    }

    public String getAbsoluteURL()
    {
        return _url;
    }

    public String getAbsoluteURL(
        String scheme,
        String server,
        int port,
        String anchor,
        boolean includeParameters)
    {
        return getURL(anchor, false);
    }

    public String[] getParameterNames()
    {
        return null;
    }

    public String[] getParameterValues(String name)
    {
        throw new IllegalArgumentException();
    }

}
