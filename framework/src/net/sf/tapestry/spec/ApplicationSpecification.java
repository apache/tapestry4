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

package net.sf.tapestry.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import net.sf.tapestry.IEngineService;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  Defines the configuration for a Tapestry application.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public class ApplicationSpecification extends BasePropertyHolder
{
    private String name;
    protected String engineClassName;

    /** @since 1.0.9 **/
    private String description;

    private final static int MAP_SIZE = 11;

    // Map of PageSpecification, keyed on String (name of page), specific
    // to this application.  Will not be null in a running application.

    protected Map pageMap;

    // Map of String (full path to component specification), 
    // keyed on String (component alias), may often be null.

    protected Map componentMap;

    // Map of String (name of service) to String (Java class name), often null

    protected Map serviceMap;

    // The Default component map is shared by all specifications

    private static Map defaultComponentMap = new HashMap(MAP_SIZE);

    static {
        defaultComponentMap.put("Insert", "/net/sf/tapestry/components/Insert.jwc");
        defaultComponentMap.put("Action", "/net/sf/tapestry/link/Action.jwc");
        defaultComponentMap.put("Checkbox", "/net/sf/tapestry/form/Checkbox.jwc");
        defaultComponentMap.put("InsertWrapped", "/net/sf/tapestry/components/InsertWrapped.jwc");
        defaultComponentMap.put("Conditional", "/net/sf/tapestry/components/Conditional.jwc");
        defaultComponentMap.put("Foreach", "/net/sf/tapestry/components/Foreach.jwc");
        defaultComponentMap.put("Frame", "/net/sf/tapestry/html/Frame.jwc");
        defaultComponentMap.put("ExceptionDisplay", "/net/sf/tapestry/html/ExceptionDisplay.jwc");
        defaultComponentMap.put("Delegator", "/net/sf/tapestry/components/Delegator.jwc");
        defaultComponentMap.put("Form", "/net/sf/tapestry/form/Form.jwc");
        defaultComponentMap.put("TextField", "/net/sf/tapestry/form/TextField.jwc");
        defaultComponentMap.put("Text", "/net/sf/tapestry/form/Text.jwc");
        defaultComponentMap.put("Select", "/net/sf/tapestry/form/Select.jwc");
        defaultComponentMap.put("Option", "/net/sf/tapestry/form/Option.jwc");
        defaultComponentMap.put("Image", "/net/sf/tapestry/html/Image.jwc");
        defaultComponentMap.put("Any", "/net/sf/tapestry/components/Any.jwc");
        defaultComponentMap.put("RadioGroup", "/net/sf/tapestry/form/RadioGroup.jwc");
        defaultComponentMap.put("Radio", "/net/sf/tapestry/form/Radio.jwc");
        defaultComponentMap.put("Rollover", "/net/sf/tapestry/html/Rollover.jwc");
        defaultComponentMap.put("Body", "/net/sf/tapestry/html/Body.jwc");
        defaultComponentMap.put("Direct", "/net/sf/tapestry/link/Direct.jwc");
        defaultComponentMap.put("Page", "/net/sf/tapestry/link/Page.jwc");
        defaultComponentMap.put("Service", "/net/sf/tapestry/link/Service.jwc");
        defaultComponentMap.put("ImageSubmit", "/net/sf/tapestry/form/ImageSubmit.jwc");
        defaultComponentMap.put("PropertySelection", "/net/sf/tapestry/form/PropertySelection.jwc");
        defaultComponentMap.put("Submit", "/net/sf/tapestry/form/Submit.jwc");
        defaultComponentMap.put("Hidden", "/net/sf/tapestry/form/Hidden.jwc");
        defaultComponentMap.put("ShowInspector", "/net/sf/tapestry/inspector/ShowInspector.jwc");
        defaultComponentMap.put("Shell", "/net/sf/tapestry/html/Shell.jwc");
        defaultComponentMap.put("InsertText", "/net/sf/tapestry/html/InsertText.jwc");
        defaultComponentMap.put("ValidField", "/net/sf/tapestry/valid/ValidField.jwc");
        defaultComponentMap.put("FieldLabel", "/net/sf/tapestry/valid/FieldLabel.jwc");
        defaultComponentMap.put("Script", "/net/sf/tapestry/html/Script.jwc");
        defaultComponentMap.put("Block", "/net/sf/tapestry/components/Block.jwc");
        defaultComponentMap.put("InsertBlock", "/net/sf/tapestry/components/InsertBlock.jwc");
        defaultComponentMap.put("ListEdit", "/net/sf/tapestry/form/ListEdit.jwc");
        defaultComponentMap.put("Upload", "/net/sf/tapestry/form/Upload.jwc");
        defaultComponentMap.put("GenericLink", "/net/sf/tapestry/link/GenericLink.jwc");
    }

    // Default page map shared by all applications.

    private static Map defaultPageMap = new HashMap(MAP_SIZE);

    static {
        // Provide defaults for three of the four standard pages.
        // An application must provide a home page and may override
        // any of these.

        defaultPageMap.put("StaleLink", new PageSpecification("/net/sf/tapestry/pages/StaleLink.jwc"));
        defaultPageMap.put(
            "StaleSession",
            new PageSpecification("/net/sf/tapestry/pages/StaleSession.jwc"));
        defaultPageMap.put("Exception", new PageSpecification("/net/sf/tapestry/pages/Exception.jwc"));

        // Provide the Inspector, which is quietly available and never
        // overriden.

        defaultPageMap.put("Inspector", new PageSpecification("/net/sf/tapestry/inspector/Inspector.jwc"));
    }

    private static Map defaultServiceMap = new HashMap(MAP_SIZE);

    static {
        defaultServiceMap.put(IEngineService.HOME_SERVICE, "net.sf.tapestry.engine.HomeService");
        defaultServiceMap.put(IEngineService.ACTION_SERVICE, "net.sf.tapestry.engine.ActionService");
        defaultServiceMap.put(IEngineService.DIRECT_SERVICE, "net.sf.tapestry.engine.DirectService");
        defaultServiceMap.put(IEngineService.PAGE_SERVICE, "net.sf.tapestry.engine.PageService");
        defaultServiceMap.put(IEngineService.RESET_SERVICE, "net.sf.tapestry.engine.ResetService");
        defaultServiceMap.put(IEngineService.RESTART_SERVICE, "net.sf.tapestry.engine.RestartService");
        defaultServiceMap.put(IEngineService.ASSET_SERVICE, "net.sf.tapestry.asset.AssetService");

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
     *	 <td>/net/sf/tapestry/link/Action.jwc</td>
     *	 <td>{@link net.sf.tapestry.link.Action}</td></tr>
     *
     * <tr>
     *		<td>/net/sf/tapestry/components/Any.jwc</td>
     *		<td>{@link net.sf.tapestry.components.Any}</td> </tr>
     * <tr>
     * <td>/net/sf/tapestry/components/Block.jwc </td> 
     *      <td>{@link net.sf.tapestry.components.Block}</td></tr>
     *  <tr>
     *      <td>/net/sf/tapestry/html/Body.jwc</td>
     *      <td>{@link net.sf.tapestry.html.Body}</td>
     * </tr>
     * <tr>
     *      <td>/net/sf/tapestry/form/Checkbox.jwc</td>
     *      <td>{@link net.sf.tapestry.form.Checkbox}</td>
     * </tr>
     * <tr>
     *   <td>/net/sf/tapestry/component/Conditional.jwc</td>
     *		<td>{@link net.sf.tapestry.components.Conditional}</td> </tr>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/components/Delegator.jwc</td>
     *		<td>{@link net.sf.tapestry.components.Delegator}</td> </tr>
     *  <tr>
     *		<td>/net/sf/tapestry/link/Direct.jwc</td>
     *		<td>{@link net.sf.tapestry.link.Direct}</td>
     * </tr>
     * <tr>
     *     <td>/net/sf/tapestry/html/ExceptionDisplay.jwc</td>
     *	 	<td>{@link net.sf.tapestry.BaseComponent}</td> </tr>
     *
     *  <tr>
     *       <td>/net/sf/tapestry/valid/FieldLabel.jwc</td>
     *       <td>{@link net.sf.tapestry.valid.FieldLabel}</td>
     * </tr>
     *
     * <tr>
     *		<td>/net/sf/tapestry/components/Foreach.jwc</td>
     *		<td>{@link net.sf.tapestry.components.Foreach}</td> </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/Form.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Form}</td> </tr>
     * <tr>
     *  <tr>
     *      <td>/net/sf/tapestry/html/Frame.jwc</td>
     *      <td?{@link net.sf.tapestry.html.Frame}</td>
     *  </tr>
     *         <td>/net/sf/tapestry/link/GenericLink.jwc</td>
     *         <td>{@link net.sf.tapestry.link.GenericLink}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/Hidden.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Hidden}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/html/Image.jwc</td>
     *		<td>{@link net.sf.tapestry.html.Image}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/ImageSubmit.jwc</td>
     *		<td>{@link net.sf.tapestry.form.ImageSubmit}</td>
     * </tr>
     *  <tr>
     * <td>/net/sf/tapestry/components/Insert.jwc </td> 
     *		<td>{@link net.sf.tapestry.components.Insert}</td></tr>
     *
     * <tr>
     * <td>/net/sf/tapestry/components/InsertBlock.jwc </td> 
     *		<td>{@link net.sf.tapestry.components.InsertBlock}</td></tr>
     *
     *   <tr>
     *       <td>/net/sf/tapestry/html/InsertText.jwc</td>
     *       <td>{@link net.sf.tapestry.html.InsertText}</td>
     *   </tr>
     *
     *  <tr>
     * <td>/net/sf/tapestry/components/InsertWrapped.jwc</td> 
     *		<td>{@link net.sf.tapestry.components.InsertWrapped}</td> </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/ListEdit.jwc</td>
     *		<td>{@link net.sf.tapestry.form.ListEdit}</td>
     * </tr>
     *
     * <tr>
     *		<td>/net/sf/tapestry/form/Option.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Option}</td>
     * </tr>
     *  <tr>
     *	 <td>/net/sf/tapestry/link/Page.jwc</td>
     *	 <td>{@link net.sf.tapestry.link.Page}</td></tr>
     * <tr>
     * <tr>
     *  <td>/net/sf/tapestry/form/PropertySelection.jwc</td>
     *  <td>{@link net.sf.tapestry.form.PropertySelection}</td> </tr>
     *
     *		<td>/net/sf/tapestry/form/Radio.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Radio}</td> </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/RadioGroup.jwc</td>
     *		<td>{@link net.sf.tapestry.form.RadioGroup}</td> </tr>
     * <tr>
     *		<td>/net/sf/tapestry/html/Rollover.jwc</td>
     *		<td>{@link net.sf.tapestry.html.Rollover} </td> </tr>
     *
     * <tr>
     *       <td>/net/sf/tapestry/html/Script.jwc</td>
     *       <td>{@link net.sf.tapestry.html.Script}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/Select.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Select}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/link/Service.jwc</td>
     *		<td>{@link net.sf.tapestry.link.Service}</td>
     * </tr>
     * <tr>
     *       <td>/net/sf/tapestry/html/Shell.jwc</td>
     *       <td>{@link net.sf.tapestry.html.Shell}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/inspector/ShowInspector.jwc</td>
     *		<td>{@link net.sf.tapestry.inspector.ShowInspector}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/Submit.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Submit}</td>
     * </tr>
     * <tr>
     *		<td>/net/sf/tapestry/form/Text.jwc</td>
     *		<td>{@link net.sf.tapestry.form.Text}</td>	</tr>
     *
     * <tr>
     *		<td>/net/sf/tapestry/form/TextField.jwc</td>
     *		<td>{@link net.sf.tapestry.form.TextField}</td> </tr>
     *
     *  <tr>
     * 	<td>/net/sf/tapestry/form/Upload.jwc</td>
     * 	<td>{@link net.sf.tapestry.form.Upload}</td>
     *  </tr>
     * 
     *  <tr>
     *       <td>/net/sf/tapestry/valid/ValidField.jwc</td>
     *       <td>{@link net.sf.tapestry.valid.ValidField}</td>
     * </tr>
     *
     *  </table>
     *
     **/

    public String getComponentAlias(String alias)
    {
        String result = null;

        if (componentMap != null)
            result = (String) componentMap.get(alias);

        if (result == null)
            result = (String) defaultComponentMap.get(alias);

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
     *  by the application.  The Collection is ordered so
     *  that the names are sorted alphabetically.
     *
     **/

    public Collection getPageNames()
    {
        Collection result;

        result = new TreeSet(defaultPageMap.keySet());

        // Add any pages specific to this application (a running application
        // will always have at least one page, Home, but we check for null
        // anyway).

        if (pageMap != null)
            result.addAll(pageMap.keySet());

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
     *	 <td>/net/sf/tapestry/pages/Exception.jwc</td>
     *	 <td>{@link Exception}</td></tr>
     *  <tr>
     *	 <td>/net/sf/tapestry/pages/StaleLink.jwc</td>
     *	 <td>StaleLink</td></tr>
     *  <tr>
     *	 <td>/net/sf/tapestry/pages/StaleSession.jwc</td>
     *	 <td>StaleSession</td></tr>
     *
     *  <tr>
     *  <td>/net/sf/tapestry/inspector/Inspector.jwc</td>
     *  <td>{@link net.sf.tapestry.inspector.Inspector}</td>
     *  </tr>
     * </table>
     *
     *
     **/

    public PageSpecification getPageSpecification(String name)
    {
        PageSpecification result = null;

        if (pageMap != null)
            result = (PageSpecification) pageMap.get(name);

        if (result == null)
            result = (PageSpecification) defaultPageMap.get(name);

        return result;
    }

    public void setComponentAlias(String alias, String resourceName)
    {
        if (defaultComponentMap.containsKey(alias))
            throw new IllegalArgumentException(
                Tapestry.getString("ApplicationSpecification.duplicate-alias", alias));

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
     **/

    public void setPageSpecification(String name, PageSpecification spec)
    {
        if (pageMap == null)
            pageMap = new HashMap(MAP_SIZE);

        if (pageMap.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.getString("ApplicationSpecification.duplicate-page", name));

        pageMap.put(name, spec);
    }

    /**
     *  Adds a new engine service.  May override a default service, but may
     *  not duplicate an existing service.
     * 
     *  @param name the name of the service
     *  @param className the JavaBean class that implements the service
     *  @since 1.0.9
     **/

    public void addService(String name, String className)
    {
        if (serviceMap == null)
            serviceMap = new HashMap(MAP_SIZE);

        if (serviceMap.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.getString("ApplicationSpecification.duplicate-service", name));

        serviceMap.put(name, className);
    }

    /**
     *  Returns the Java class to instantiate for a given service name, or null
     *  if the service does not exist.
     * 
     *  <p>Applications have a number of default services:
     * 
     *  <p>
     *  <table border="1">
     * 	<tr> <th>Name</th> <th>Class</th> </tr>
     *  <tr>
     * 		<td>action</td> <td>{@link net.sf.tapestry.engine.ActionService}</td> 
     *  </tr>
     *  <tr>
     * 		<td>asset</td> <td>{@link net.sf.tapestry.asset.AssetService} </td>
     *  </tr>
     *  <tr>
     * 		<td>direct</td> <td>{@link net.sf.tapestry.engine.DirectService}</td>
     *	</tr>
     *  <tr>
     * 		<td>home</td> <td>{@link net.sf.tapestry.engine.HomeService}</td> 
     *  </tr>
     *  <tr>
     * 		<td>page</td> <td>{@link net.sf.tapestry.engine.PageService}</td>
     *  </tr>
     *  <tr>
     * 		<td>reset</td> <td> {@link net.sf.tapestry.engine.ResetService} </td>
     *  </tr>
     *  <tr>
     * 		<td>restart</td> <td>{@link net.sf.tapestry.engine.RestartService}</td>
     *  </tr>
     *  </table>
     * 
     *  @since 1.0.9
     **/

    public String getServiceClassName(String serviceName)
    {
        String result = null;

        if (serviceMap != null)
            result = (String) serviceMap.get(serviceName);

        if (result == null)
            result = (String) defaultServiceMap.get(serviceName);

        return result;
    }

    /**
     *  Returns the names of all services (default and specific to the application).
     *  The collection returned will order the names alphabetically.
     * 
     *  @since 1.0.9
     * 
     **/

    public Collection getServiceNames()
    {
        Collection result = new TreeSet(defaultServiceMap.keySet());

        if (serviceMap != null)
            result.addAll(serviceMap.keySet());

        return result;
    }

    public String toString()
    {
        return "ApplicationSpecification[" + name + " " + engineClassName + "]";
    }

    /**
     * 
     *  Returns the documentation for the application..
     * 
     *  @since 1.0.9
     * 
     **/

    public String getDescription()
    {
        return description;
    }

    /**
     *  
     *  Sets the documentation for the application..
     * 
     *  @since 1.0.9
     * 
     **/

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * 
     *  Provide an unmodifiable copy of the default component
     *  map to subclasses
     * 
     **/

    protected Map getDefaultComponentMap()
    {
        return Collections.unmodifiableMap(defaultComponentMap);
    }

    /**
     * 
     *  Provide an unmodifiable copy of the default page
     *  map to subclasses
     * 
     **/

    protected Map getDefaultPageMap()
    {
        return Collections.unmodifiableMap(defaultPageMap);
    }

    /**
     * 
     *  Provide an unmodifiable copy of the default service
     *  map to subclasses
     *
     **/

    protected Map getDefaultServiceMap()
    {
        return Collections.unmodifiableMap(defaultServiceMap);
    }

}