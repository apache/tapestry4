package com.primix.tapestry.spec;

import com.primix.tapestry.*;
import java.util.*;
import com.primix.tapestry.components.*;
import com.primix.foundation.exception.*;
import com.primix.foundation.*;
import com.primix.tapestry.inspector.ShowInspector;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Defines the configuration for a Tapestry application.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ApplicationSpecification extends BasePropertyHolder
{
	private String name;

	private final static int MAP_SIZE = 11;

	private Map pageMap = new HashMap(MAP_SIZE);

	private Map componentMap = new HashMap(MAP_SIZE);
	{
		componentMap.put("Insert", 
			"/com/primix/tapestry/components/Insert.jwc");
		componentMap.put("Action", 
			"/com/primix/tapestry/components/Action.jwc");
		componentMap.put("Checkbox",
			"/com/primix/tapestry/components/Checkbox.jwc");
		componentMap.put("InsertWrapped",
			"/com/primix/tapestry/components/InsertWrapped.jwc");
		componentMap.put("Conditional", 
			"/com/primix/tapestry/components/Conditional.jwc");
		componentMap.put("Foreach", 
			"/com/primix/tapestry/components/Foreach.jwc");
		componentMap.put("ExceptionDisplay",
			"/com/primix/tapestry/components/ExceptionDisplay.jwc");
		componentMap.put("Delegator",
			"/com/primix/tapestry/components/Delegator.jwc");
		componentMap.put("Form",
			"/com/primix/tapestry/components/Form.jwc");
		componentMap.put("TextField",
			"/com/primix/tapestry/components/TextField.jwc");
		componentMap.put("Text",
			"/com/primix/tapestry/components/Text.jwc");
		componentMap.put("Select",
			"/com/primix/tapestry/components/Select.jwc");
		componentMap.put("Option",
			"/com/primix/tapestry/components/Option.jwc");
		componentMap.put("Image",
			"/com/primix/tapestry/components/Image.jwc");
		componentMap.put("Any",
			"/com/primix/tapestry/components/Any.jwc");
		componentMap.put("RadioGroup",
			"/com/primix/tapestry/components/RadioGroup.jwc");
		componentMap.put("Radio",
			"/com/primix/tapestry/components/Radio.jwc");
		componentMap.put("DatabaseQuery",
			"/com/primix/tapestry/components/DatabaseQuery.jwc");
		componentMap.put("Rollover",
			"/com/primix/tapestry/components/Rollover.jwc");
		componentMap.put("Body",
			"/com/primix/tapestry/components/Body.jwc");
		componentMap.put("Direct",
			"/com/primix/tapestry/components/Direct.jwc");
		componentMap.put("Page",
			"/com/primix/tapestry/components/Page.jwc");
		componentMap.put("Service",
			"/com/primix/tapestry/components/Service.jwc");
		componentMap.put("InsertURL",
			"/com/primix/tapestry/components/InsertURL.jwc");
		componentMap.put("ImageButton",
			"/com/primix/tapestry/components/ImageButton.jwc");	
		componentMap.put("PropertySelection",
			"/com/primix/tapestry/components/PropertySelection.jwc");
		componentMap.put("Submit",
			"/com/primix/tapestry/components/Submit.jwc");	
		componentMap.put("Hidden",
			"/com/primix/tapestry/components/Hidden.jwc");	
		componentMap.put("ShowInspector",
			"/com/primix/tapestry/inspector/ShowInspector.jwc");		
        componentMap.put("Shell",
            "/com/primix/tapestry/components/Shell.jwc");
        componentMap.put("InsertText",
            "/com/primix/tapestry/components/InsertText.jwc");

		// Provide defaults for three of the four standard pages.
		// An application must provide a home page and may override
		// any of these.

		pageMap.put("StaleLink",
			new PageSpecification("/com/primix/tapestry/pages/StaleLink.jwc"));
		pageMap.put("StaleSession",
			new PageSpecification("/com/primix/tapestry/pages/StaleSession.jwc"));
		pageMap.put("Exception",
			new PageSpecification("/com/primix/tapestry/pages/Exception.jwc"));
		pageMap.put("Inspector",
			new PageSpecification("/com/primix/tapestry/inspector/Inspector.jwc", 0));		
	}

	/**
	*  Gets the resource path for a component given a potential alias.  If
	*  an resource is known for the alias, it is returned.
	*  Returns null if the alias is not known.
	*
	*  <p>
	*  The following components are automatically available.  They are
	*  registered with an alias that matches their class name.
	*
	*  <table border=1>
	* 	<tr> <th>Specification</th> <th>Class / Alias</th></tr>
	*  <tr>
	*	 <td>/com/primix/tapestry/components/Action.jwc</td>
	*	 <td>{@link Action}</td></tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Checkbox.jwc</td>
	*		<td>{@link Checkbox}</td>
	* </tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/components/Any.jwc</td>
	*		<td>{@link Any}</td> </tr>
	*  <tr>
	*		<td>/com/primix/tapestry/components/Body.jwc</td>
	*		<td>{@link Body}</td>
	* </tr>
	* <tr>
	*   <td>/com/primix/tapestry/component/Conditional.jwc</td>
	*		<td>{@link Conditional}</td> </tr>
	* </tr>
	*		<td>/com/primix/tapestry/components/DatabaseQuery.jwc</td>
	*		<td>{@link DatabaseQuery}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Delegator.jwc</td>
	*		<td>{@link Delegator}</td> </tr>
	*  <tr>
	*		<td>/com/primix/tapestry/components/Direct.jwc</td>
	*		<td>{@link Direct}</td>
	* </tr>
	* <tr>
	*     <td>/com/primix/tapestry/components/ExceptionDisplay.jwc</td>
	*	 	<td>{@link BaseComponent}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Foreach.jwc</td>
	*		<td>{@link Foreach}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Form.jwc</td>
	*		<td>{@link Form}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Hidden.jwc</td>
	*		<td>{@link Hidden}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Image.jwc</td>
	*		<td>{@link Image}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/ImageButton.jwc</td>
	*		<td>{@link ImageButton}</td>
	* </tr>
	*  <tr>
	* <td>/com/primix/tapestry/components/Insert.jwc </td> 
	*		<td>{@link Insert}</td></tr>
    *
    *   <tr>
    *       <td>/com/primix/tapestry/components/InsertText.jwc</td>
    *       <td>{@link InsertText}</td>
    *   </tr>
    *
	*  <tr>
	*		<td>/com/primix/tapestry/components/InsertURL.jwc</td>
	*		<td>{@link InsertURL}</td>
	*	</tr>
	*  <tr>
	* <td>/com/primix/tapestry/components/InsertWrapped.jwc</td> 
	*		<td>{@link InsertWrapped}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Option.jwc</td>
	*		<td>{@link Option}</td>
	* </tr>
	*  <tr>
	*	 <td>/com/primix/tapestry/components/Page.jwc</td>
	*	 <td>{@link Page}</td></tr>
	* <tr>
	* <tr>
	*  <td>/com/primix/tapestry/components/PropertySelection.jwc</td>
	*  <td>{@link PropertySelection}</td> </tr>
	*
	*		<td>/com/primix/tapestry/components/Radio.jwc</td>
	*		<td>{@link Radio}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/RadioGroup.jwc</td>
	*		<td>{@link RadioGroup}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Rollover.jwc</td>
	*		<td>{@link Rollover} </td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Select.jwc</td>
	*		<td>{@link Select}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Service.jwc</td>
	*		<td>{@link Service}</td>
	* </tr>
    * <tr>
    *       <td>/com/primix/tapestry/components/Shell.jwc</td>
    *       <td>{@link Shell}</td>
    * </tr>
	* <tr>
	*		<td>/com/primix/tapestry/inspector/ShowInspector.jwc</td>
	*		<td>{@link ShowInspector}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Submit.jwc</td>
	*		<td>{@link Submit}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/components/Text.jwc</td>
	*		<td>{@link Text}</td>	</tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/components/TextField.jwc</td>
	*		<td>{@link TextField}</td> </tr>
	*  </table>
	*
	*/

	public String getComponentAlias(String alias)
	{
		return (String)componentMap.get(alias);
	}

	public String getName()
	{
		return name;
	}

	/**
	*  Returns an unmodifiable <code>Collection</code>
    *  of the String names of the pages defined
	*  by the application.
	*
	*/

	public Collection getPageNames()
	{
		return Collections.unmodifiableSet(pageMap.keySet());
	}

	/**
	 *  Gets a page specification with the given name, or returns null.
	 *
	 *  <p>The following three default page specifications will always
	 *  be present, unless overriden:
	 *
	 *  <table border=1>
	 * 	<tr> <th>Specification</th> <th>Name / Class</th></tr>
	 *  <tr>
	 *	 <td>/com/primix/tapestry/pages/Exception.jwc</td>
	 *	 <td>{@link Exception}</td></tr>
	 *  <tr>
	 *	 <td>/com/primix/tapestry/pages/StaleLink.jwc</td>
	 *	 <td>StaleLink</td></tr>
	 *  <tr>
	 *	 <td>/com/primix/tapestry/pages/StaleSession.jwc</td>
	 *	 <td>StaleSession</td></tr>
	 *
	 *
	 */
	 
	public PageSpecification getPageSpecification(String name)
	{
		if (pageMap == null)
			return null;

		return (PageSpecification)pageMap.get(name);
	}

	public void setComponentAlias(String alias, String resourceName)
	{
		if (componentMap.containsKey(alias))
			throw new IllegalArgumentException("May not redefine component alias " +
				alias + ".");

		componentMap.put(alias, resourceName);
	}

	public void setName(String value)
	{
		name = value;
	}

	/**
	*  Adds a new page resource.  An existing page with the same name is replaced.
	*
	*  @param logicalName The name used for the page within the application.
	*  @param type The component resource path for the page.
	*
	*/

	public void setPageSpecification(String name, PageSpecification spec)
	{
		if (pageMap == null)
			pageMap = new HashMap(MAP_SIZE);

		pageMap.put(name, spec);
	}

	public String toString()
	{
		return "ApplicationSpecification[" + name + "]";
	}
}

