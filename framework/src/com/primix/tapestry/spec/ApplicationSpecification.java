/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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

package com.primix.tapestry.spec;

import com.primix.tapestry.*;
import java.util.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.html.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.link.*;
import com.primix.tapestry.valid.*;
import com.primix.tapestry.util.exception.*;
import com.primix.tapestry.util.*;
import com.primix.tapestry.inspector.*;
import com.primix.tapestry.script.*;

/**
 *  Defines the configuration for a Tapestry application.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class ApplicationSpecification 
extends BasePropertyHolder
{
	private String name;
	private String engineClassName;

	private final static int MAP_SIZE = 11;

	// Map of PageSpecification, keyed on String (name of page), specific
	// to this application.  Will not be null in a running application.

	private Map pageMap;

	// Map of String (full path to component specification), 
	// keyed on String (component alias), may often be null.

	private Map componentMap;

	// The Default component map is shared by all specifications

	private static Map defaultComponentMap = new HashMap(MAP_SIZE);

	static
	{
		defaultComponentMap.put("Insert", 
			"/com/primix/tapestry/components/Insert.jwc");
		defaultComponentMap.put("Action", 
			"/com/primix/tapestry/link/Action.jwc");
		defaultComponentMap.put("Checkbox",
			"/com/primix/tapestry/form/Checkbox.jwc");
		defaultComponentMap.put("InsertWrapped",
			"/com/primix/tapestry/components/InsertWrapped.jwc");
		defaultComponentMap.put("Conditional", 
			"/com/primix/tapestry/components/Conditional.jwc");
		defaultComponentMap.put("Foreach", 
			"/com/primix/tapestry/components/Foreach.jwc");
		defaultComponentMap.put("ExceptionDisplay",
			"/com/primix/tapestry/html/ExceptionDisplay.jwc");
		defaultComponentMap.put("Delegator",
			"/com/primix/tapestry/components/Delegator.jwc");
		defaultComponentMap.put("Form",
			"/com/primix/tapestry/form/Form.jwc");
		defaultComponentMap.put("TextField",
			"/com/primix/tapestry/form/TextField.jwc");
		defaultComponentMap.put("Text",
			"/com/primix/tapestry/form/Text.jwc");
		defaultComponentMap.put("Select",
			"/com/primix/tapestry/form/Select.jwc");
		defaultComponentMap.put("Option",
			"/com/primix/tapestry/form/Option.jwc");
		defaultComponentMap.put("Image",
			"/com/primix/tapestry/html/Image.jwc");
		defaultComponentMap.put("Any",
			"/com/primix/tapestry/components/Any.jwc");
		defaultComponentMap.put("RadioGroup",
			"/com/primix/tapestry/form/RadioGroup.jwc");
		defaultComponentMap.put("Radio",
			"/com/primix/tapestry/form/Radio.jwc");
		defaultComponentMap.put("Rollover",
			"/com/primix/tapestry/html/Rollover.jwc");
		defaultComponentMap.put("Body",
			"/com/primix/tapestry/html/Body.jwc");
		defaultComponentMap.put("Direct",
			"/com/primix/tapestry/link/Direct.jwc");
		defaultComponentMap.put("Page",
			"/com/primix/tapestry/link/Page.jwc");
		defaultComponentMap.put("Service",
			"/com/primix/tapestry/link/Service.jwc");
		defaultComponentMap.put("ImageSubmit",
			"/com/primix/tapestry/form/ImageSubmit.jwc");	
		defaultComponentMap.put("PropertySelection",
			"/com/primix/tapestry/form/PropertySelection.jwc");
		defaultComponentMap.put("Submit",
			"/com/primix/tapestry/form/Submit.jwc");	
		defaultComponentMap.put("Hidden",
			"/com/primix/tapestry/form/Hidden.jwc");	
		defaultComponentMap.put("ShowInspector",
			"/com/primix/tapestry/inspector/ShowInspector.jwc");		
		defaultComponentMap.put("Shell",
			"/com/primix/tapestry/html/Shell.jwc");
		defaultComponentMap.put("InsertText",
			"/com/primix/tapestry/html/InsertText.jwc");
		defaultComponentMap.put("ValidatingTextField",
			"/com/primix/tapestry/valid/ValidatingTextField.jwc");
		defaultComponentMap.put("DateField",
			"/com/primix/tapestry/valid/DateField.jwc");
		defaultComponentMap.put("IntegerField",
			"/com/primix/tapestry/valid/IntegerField.jwc");
		defaultComponentMap.put("FieldLabel",
			"/com/primix/tapestry/valid/FieldLabel.jwc");
		defaultComponentMap.put("Script",
			"/com/primix/tapestry/script/Script.jwc");
		defaultComponentMap.put("Block",
			"/com/primix/tapestry/components/Block.jwc");
		defaultComponentMap.put("InsertBlock",
			"/com/primix/tapestry/components/InsertBlock.jwc");
			
	}

	// Default page map shared by all applications.

	private static Map defaultPageMap = new HashMap(MAP_SIZE);
	{
		// Provide defaults for three of the four standard pages.
		// An application must provide a home page and may override
		// any of these.

		defaultPageMap.put("StaleLink",
			new PageSpecification("/com/primix/tapestry/pages/StaleLink.jwc"));
		defaultPageMap.put("StaleSession",
			new PageSpecification("/com/primix/tapestry/pages/StaleSession.jwc"));
		defaultPageMap.put("Exception",
			new PageSpecification("/com/primix/tapestry/pages/Exception.jwc"));

		// Provide the Inspector, which is quietly available and never
		// overriden.

		defaultPageMap.put("Inspector",
			new PageSpecification("/com/primix/tapestry/inspector/Inspector.jwc"));		
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
	*	 <td>/com/primix/tapestry/link/Action.jwc</td>
	*	 <td>{@link Action}</td></tr>
	* <tr>
	* <td>/com/primix/tapestry/components/Block.jwc </td> 
	*		<td>{@link Block}</td></tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/Checkbox.jwc</td>
	*		<td>{@link Checkbox}</td>
	* </tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/components/Any.jwc</td>
	*		<td>{@link Any}</td> </tr>
	*  <tr>
	*		<td>/com/primix/tapestry/html/Body.jwc</td>
	*		<td>{@link Body}</td>
	* </tr>
	* <tr>
	*   <td>/com/primix/tapestry/component/Conditional.jwc</td>
	*		<td>{@link Conditional}</td> </tr>
	* </tr>
	*  <tr>
	*       <td>/com/primix/tapestry/valid/DateField.jwc</td>
	*       <td>{@link DateField}</td>
	* </tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/components/Delegator.jwc</td>
	*		<td>{@link Delegator}</td> </tr>
	*  <tr>
	*		<td>/com/primix/tapestry/link/Direct.jwc</td>
	*		<td>{@link Direct}</td>
	* </tr>
	* <tr>
	*     <td>/com/primix/tapestry/html/ExceptionDisplay.jwc</td>
	*	 	<td>{@link BaseComponent}</td> </tr>
	*
	*  <tr>
	*       <td>/com/primix/tapestry/valid/FieldLabel.jwc</td>
	*       <td>{@link FieldLabel}</td>
	* </tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/components/Foreach.jwc</td>
	*		<td>{@link Foreach}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/Form.jwc</td>
	*		<td>{@link Form}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/Hidden.jwc</td>
	*		<td>{@link Hidden}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/html/Image.jwc</td>
	*		<td>{@link Image}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/ImageSubmit.jwc</td>
	*		<td>{@link ImageSubmit}</td>
	* </tr>
	*  <tr>
	* <td>/com/primix/tapestry/components/Insert.jwc </td> 
	*		<td>{@link Insert}</td></tr>
	*
	* <tr>
	* <td>/com/primix/tapestry/components/InsertBlock.jwc </td> 
	*		<td>{@link InsertBlock}</td></tr>
	*
	*   <tr>
	*       <td>/com/primix/tapestry/html/InsertText.jwc</td>
	*       <td>{@link InsertText}</td>
	*   </tr>
	*
	*  <tr>
	* <td>/com/primix/tapestry/components/InsertWrapped.jwc</td> 
	*		<td>{@link InsertWrapped}</td> </tr>
	*
	*  <tr>
	*       <td>/com/primix/tapestry/valid/IntegerField.jwc</td>
	*       <td>{@link IntegerField}</td>
	* </tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/form/Option.jwc</td>
	*		<td>{@link Option}</td>
	* </tr>
	*  <tr>
	*	 <td>/com/primix/tapestry/link/Page.jwc</td>
	*	 <td>{@link Page}</td></tr>
	* <tr>
	* <tr>
	*  <td>/com/primix/tapestry/form/PropertySelection.jwc</td>
	*  <td>{@link PropertySelection}</td> </tr>
	*
	*		<td>/com/primix/tapestry/form/Radio.jwc</td>
	*		<td>{@link Radio}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/RadioGroup.jwc</td>
	*		<td>{@link RadioGroup}</td> </tr>
	* <tr>
	*		<td>/com/primix/tapestry/html/Rollover.jwc</td>
	*		<td>{@link Rollover} </td> </tr>
	*
	* <tr>
	*       <td>/com/primix/tapestry/script/Script.jwc</td>
	*       <td>{@link Script}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/Select.jwc</td>
	*		<td>{@link Select}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/link/Service.jwc</td>
	*		<td>{@link Service}</td>
	* </tr>
	* <tr>
	*       <td>/com/primix/tapestry/html/Shell.jwc</td>
	*       <td>{@link Shell}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/inspector/ShowInspector.jwc</td>
	*		<td>{@link ShowInspector}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/Submit.jwc</td>
	*		<td>{@link Submit}</td>
	* </tr>
	* <tr>
	*		<td>/com/primix/tapestry/form/Text.jwc</td>
	*		<td>{@link Text}</td>	</tr>
	*
	* <tr>
	*		<td>/com/primix/tapestry/form/TextField.jwc</td>
	*		<td>{@link TextField}</td> </tr>
	*
	*  <tr>
	*       <td>/com/primix/tapestry/valid/ValidatingTextField.jwc</td>
	*       <td>{@link ValidatingTextField}</td>
	* </tr>
	*
	*  </table>
	*
	*/

	public String getComponentAlias(String alias)
	{
		String result = null;

		if (componentMap != null)
			result = (String)componentMap.get(alias);

		if (result == null)
			result = (String)defaultComponentMap.get(alias);

		return result;
	}

	public String getName()
	{
		return name;
	}

	public void setEngineClassName(String value)
	{
		engineClassName = value;
	}

	public String getEngineClassName()
	{
		return engineClassName;
	}

	/**
	*  Returns a {@link Collection}
	*  of the String names of the pages defined
	*  by the application.
	*
	*/

	public Collection getPageNames()
	{
		Collection result;

		result = new HashSet();

		// Add any pages specific to this application (a running application
		// will always have at least one page, Home, but we check for null
		// anyway).

		if (pageMap != null)
			result.addAll(pageMap.keySet());

		// Now add any additional pages (such as Inspector) that are provided
		// by the system.

		result.addAll(defaultPageMap.keySet());

		return result;
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
	*  <tr>
	*  <td>/com/primix/tapestry/inspector/Inspector.jwc</td>
	*  <td>{@link Inspector}</td>
	*  </tr>
	* </table>
	 *
	 *
	 */

	public PageSpecification getPageSpecification(String name)
	{
		PageSpecification result = null;

		if (pageMap != null)
			result = (PageSpecification)pageMap.get(name);

		if (result == null)
			result = (PageSpecification)defaultPageMap.get(name);

		return result;
	}

	public void setComponentAlias(String alias, String resourceName)
	{
		if (defaultComponentMap.containsKey(alias))
			throw new IllegalArgumentException("May not redefine component alias " +
				alias + ".");

		if (componentMap == null)
			componentMap = new HashMap(MAP_SIZE);

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
		return "ApplicationSpecification[" + name + " " + engineClassName + "]";
	}
}

