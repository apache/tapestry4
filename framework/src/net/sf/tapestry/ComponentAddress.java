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

package net.sf.tapestry;

import java.io.Serializable;

/**
 * The ComponentAddress class contains the path to a component, allowing it to  
 * locate an instance of that component in a different RequestCycle.
 * 
 * <p>This class needs to be used mostly when working with components
 * accessed via the IRender interface. It allows those components to serialize and
 * pass as a Service Parameter information about what component they have to 
 * talk to if control returns back to them and they have not been initialized 
 * in another way. 
 * 
 * <p>This situation often occurs when the component used via IRender contains
 * Direct or Action links.
 * 
 * @version $Id$
 * @author mindbridge
 * @since 2.2
 */
public class ComponentAddress implements Serializable
{
	private String m_strPageName;
	private String m_strIdPath;

	/**
	 * Creates a new ComponentAddress object that carries the identification 
	 * information of the given component (the page name and the ID path).
	 * @param objComponent the component to get the address of
	 */
	public ComponentAddress(IComponent objComponent)
	{
		init(objComponent);
	}

	/**
	 * Creates a new ComponentAddress using the given Page Name and ID Path
	 * @param strPageName the Page Name of the component
	 * @param strIdPath the ID Path of the component
	 */
	public ComponentAddress(String strPageName, String strIdPath)
	{
		m_strPageName = strPageName;
		m_strIdPath = strIdPath;
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

	/**
	 * Finds a component with the current address using the given RequestCycle.
	 * @param objCycle the RequestCycle to use to locate the component
	 * @return IComponent a component that has been initialized for the given RequestCycle
	 */
	public IComponent findComponent(IRequestCycle objCycle)
	{
		IPage objPage = objCycle.getPage(m_strPageName);
		return objPage.getNestedComponent(m_strIdPath);
	}
	/**
	 * Returns the idPath of the component.
	 * @return String the ID path of the component
	 */
	public String getIdPath()
	{
		return m_strIdPath;
	}

	/**
	 * Returns the Page Name of the component.
	 * @return String the Page Name of the component
	 */
	public String getPageName()
	{
		return m_strPageName;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return m_strPageName.hashCode() * 31 + m_strIdPath.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ComponentAddress))
			return false;

		if (obj == this)
			return true;

		ComponentAddress objAddress = (ComponentAddress) obj;
		return getPageName().equals(objAddress.getPageName())
			&& getIdPath().equals(objAddress.getIdPath());
	}

}
