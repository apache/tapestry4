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

package net.sf.tapestry.contrib.table.components;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;

/**
 *
 * @version $Id$
 * @author mindbridge
 */
public class ComponentAddress implements Serializable
{
	private String m_strPageName;
	private String m_strIdPath;

	public ComponentAddress(IComponent objComponent)
	{
		init(objComponent);
	}

	private void init(IComponent objComponent)
	{
		IPage objPage = objComponent.getPage();
		INamespace objNamespace = objPage.getNamespace();
		String strNamespace = objNamespace.getExtendedId();
		if (strNamespace == null)
			strNamespace = "";
		else
			strNamespace = strNamespace + ":";
		m_strPageName = strNamespace + objPage.getName();

        m_strIdPath = objComponent.getIdPath();
	}

	public IComponent findComponent(IRequestCycle objCycle)
	{
		IPage objPage = objCycle.getPage(m_strPageName);
		return objPage.getNestedComponent(m_strIdPath);
	}
}
