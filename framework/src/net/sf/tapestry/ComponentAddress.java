package net.sf.tapestry;

import java.io.Serializable;

/**
 * The ComponentAddress class contains the path to a component, allowing it to  
 * locate an instance of that component in a different 
 * {@link net.sf.tapestry.IRequestCycle}.
 * 
 * <p>This class needs to be used mostly when working with components
 * accessed via the {@link net.sf.tapestry.IRender} interface. 
 * It allows those components to serialize and
 * pass as a service parameter information about what component they have to 
 * talk to if control returns back to them. 
 * 
 * <p>This situation often occurs when the component used via IRender contains
 * Direct or Action links.
 * 
 * @version $Id$
 * @author mindbridge
 * @since 2.2
 * 
 */
public class ComponentAddress implements Serializable
{
	private String _pageName;
	private String _idPath;

	/**
	 * Creates a new ComponentAddress object that carries the identification 
	 * information of the given component (the page name and the ID path).
	 * @param component the component to get the address of
	 */
	public ComponentAddress(IComponent component)
	{
		init(component);
	}

	/**
	 * Creates a new ComponentAddress using the given Page Name and ID Path
	 * @param pageName the name of the page that contains the component
	 * @param idPath the ID Path of the component
	 */
	public ComponentAddress(String pageName, String idPath)
	{
		_pageName = pageName;
		_idPath = idPath;
	}

	/**
	 * Creates a new ComponentAddress using the given Page Name and ID Path
	 * relative on the provided Namespace
	 * @param namespace the namespace of the page that contains the component
	 * @param pageName the name of the page that contains the component
	 * @param idPath the ID Path of the component
	 */
	public ComponentAddress(
		INamespace namespace,
		String pageName,
		String idPath)
	{
        _pageName = namespace.constructQualifiedName(pageName);
		_idPath = idPath;
	}

	private void init(IComponent component)
	{
		IPage objPage = component.getPage();

		_pageName = objPage.getName();
		_idPath = component.getIdPath();
	}

	/**
	 * Finds a component with the current address using the given RequestCycle.
	 * @param objCycle the RequestCycle to use to locate the component
	 * @return IComponent a component that has been initialized for the given RequestCycle
	 */
	public IComponent findComponent(IRequestCycle cycle)
	{
		IPage objPage = cycle.getPage(_pageName);
		return objPage.getNestedComponent(_idPath);
	}
	/**
	 * Returns the idPath of the component.
	 * @return String the ID path of the component
	 */
	public String getIdPath()
	{
		return _idPath;
	}

	/**
	 * Returns the Page Name of the component.
	 * @return String the Page Name of the component
	 */
	public String getPageName()
	{
		return _pageName;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return _pageName.hashCode() * 31 + _idPath.hashCode();
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
