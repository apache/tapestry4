package net.sf.tapestry.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.util.BasePropertyHolder;

/**
 * Defines a contained component.  This includes the information needed to
 * get the contained component's specification, as well as any bindings
 * for the component.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class ContainedComponent extends BasePropertyHolder
{
	private String type;

	private String copyOf;

	protected Map bindings;

	private static final int MAP_SIZE = 3;

	/**
	 *  Returns the named binding, or null if the binding does not
	 *  exist.
	 *
	 **/

	public BindingSpecification getBinding(String name)
	{
		if (bindings == null)
			return null;

		return (BindingSpecification) bindings.get(name);
	}

	/**
	 *  Returns an umodifiable <code>Collection</code>
	 *  of Strings, each the name of one binding
	 *  for the component.
	 *
	 **/

	public Collection getBindingNames()
	{
		if (bindings == null)
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableCollection(bindings.keySet());
	}

	public String getType()
	{
		return type;
	}

	public void setBinding(String name, BindingSpecification spec)
	{
		if (bindings == null)
			bindings = new HashMap(MAP_SIZE);

		bindings.put(name, spec);
	}

	public void setType(String value)
	{
		type = value;
	}

	/**
	 * 	Sets the String Id of the component being copied from.
	 *  For use by IDE tools like Spindle.
	 * 
	 *  @since 1.0.9
	 **/

	public void setCopyOf(String id)
	{
		copyOf = id;
	}

	/**
	 * 	Returns the id of the component being copied from.
	 *  For use by IDE tools like Spindle.
	 * 
	 *  @since 1.0.9
	 **/

	public String getCopyOf()
	{
		return copyOf;
	}
}