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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  Specification for a library.  {@link net.sf.tapestry.spec.ApplicationSpecification}
 *  is a specialized kind of library.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class LibrarySpecification extends BasePropertyHolder implements ILibrarySpecification
{
    /**
     *  Resource resolver (used to instantiate extensions).
     * 
     **/

    private IResourceResolver _resolver;

    /**
     *  Map of page name to page specification path.
     * 
     **/

    private Map _pages;

    /**
     *  Map of component alias to component specification path.
     * 
     **/
    private Map _components;

    /**
     *  Map of service name to service class name.
     * 
     **/

    private Map _services;

    /**
     *  Map of library id to library specification path.
     * 
     **/

    private Map _libraries;

    private String _dtdVersion;

    private String _description;

    /**
     *  Map of extension name to {@link ExtensionSpecification}.
     * 
     **/

    private Map _extensions;

    /**
     *  Map of extension name to Object for instantiated extensions.
     * 
     **/

    private Map _instantiatedExtensions;
    
    /**
     *  The XML Public Id used when the library specification was read
     *  (if applicable).
     * 
     *  @since 2.2
     * 
     **/

    private String _publicId;
    

    public String getLibrarySpecificationPath(String id)
    {
        return (String) get(_libraries, id);
    }

    /**
     *  Sets the specification path for an embedded library.
     * 
     *  @throws IllegalArgumentException if a library with the given
     *  id already exists
     * 
     **/

    public void setLibrarySpecificationPath(String id, String path)
    {
        if (_libraries == null)
            _libraries = new HashMap();

        if (_libraries.containsKey(id))
            throw new IllegalArgumentException(
                Tapestry.getString("LibrarySpecification.duplicate-child-namespace-id", id));

        _libraries.put(id, path);
    }

    public List getLibraryIds()
    {
        return sortedKeys(_libraries);
    }

    public String getPageSpecificationPath(String name)
    {
        return (String) get(_pages, name);
    }

    public void setPageSpecificationPath(String name, String path)
    {
        if (_pages == null)
            _pages = new HashMap();

        if (_pages.containsKey(name))
            throw new IllegalArgumentException(Tapestry.getString("LibrarySpecification.duplicate-page-name", name));

        _pages.put(name, path);
    }

    public List getPageNames()
    {
        return sortedKeys(_pages);
    }

    public void setComponentSpecificationPath(String alias, String path)
    {
        if (_components == null)
            _components = new HashMap();

        if (_components.containsKey(alias))
            throw new IllegalArgumentException(
                Tapestry.getString("LibrarySpecification.duplicate-component-alias", alias));

        _components.put(alias, path);
    }

    public String getComponentSpecificationPath(String alias)
    {
        return (String) get(_components, alias);
    }

    public List getComponentAliases()
    {
        return sortedKeys(_components);
    }

    public String getServiceClassName(String name)
    {
        return (String) get(_services, name);
    }

    public List getServiceNames()
    {
        return sortedKeys(_services);
    }

    public void setServiceClassName(String name, String className)
    {
        if (_services == null)
            _services = new HashMap();

        if (_services.containsKey(name))
            throw new IllegalArgumentException(Tapestry.getString("LibrarySpecification.duplicate-service-name", name));

        _services.put(name, className);
    }

    private List sortedKeys(Map map)
    {
        if (map == null)
            return Collections.EMPTY_LIST;

        List result = new ArrayList(map.keySet());

        Collections.sort(result);

        return result;
    }

    private Object get(Map map, Object key)
    {
        if (map == null)
            return null;

        return map.get(key);
    }

    /**
     * 
     *  Returns the documentation for this library..
     * 
     * 
     **/

    public String getDescription()
    {
        return _description;
    }

    /**
     *  
     *  Sets the documentation for this library.
     * 
     * 
     **/

    public void setDescription(String description)
    {
        _description = description;
    }

    public String getDTDVersion()
    {
        return _dtdVersion;
    }

    /**
     *  Sets the version number of the DTD from which this specification
     *  was created, if known.  This method exists as a convienience for
     *  the Spindle plugin.
     * 
     * 
     **/

    public void setDTDVersion(String dtdVersion)
    {
        _dtdVersion = dtdVersion;
    }

    /**
     *  Returns a Map of extensions; key is extension name, value is
     *  {@link net.sf.tapestry.spec.ExtensionSpecification}.
     *  May return null.  The returned Map is immutable.
     * 
     **/

    public Map getExtensionSpecifications()
    {
        if (_extensions == null)
            return null;

        return Collections.unmodifiableMap(_extensions);
    }

    /**
     *  Adds another extension specification.
     * 
     *  @throws IllegalArgumentException if an extension with the given name already exists.
     * 
     **/

    public void addExtensionSpecification(String name, ExtensionSpecification extension)
    {
        if (_extensions == null)
            _extensions = new HashMap();

        if (_extensions.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.getString("LibrarySpecification.duplicate-extension-name", this, name));

        _extensions.put(name, extension);
    }

    /**
     *  Returns a sorted List of the names of all extensions.  May return the empty list,
     *  but won't return null.
     * 
     **/

    public List getExtensionNames()
    {
        return sortedKeys(_instantiatedExtensions);
    }

    /**
     *  Returns the named ExtensionSpecification, or null if it doesn't exist.
     * 
     **/

    public ExtensionSpecification getExtensionSpecification(String name)
    {
        if (_extensions == null)
            return null;

        return (ExtensionSpecification) _extensions.get(name);
    }

    /**
     *  Returns an instantiated extension.  Extensions are created as needed and
     *  cached for later use.
     * 
     *  @throws IllegalArgumentException if no extension specification exists for the
     *  given name.
     * 
     **/

    public synchronized Object getExtension(String name)
    {
        if (_instantiatedExtensions == null)
            _instantiatedExtensions = new HashMap();

        Object result = _instantiatedExtensions.get(name);

        if (result == null)
        {
            ExtensionSpecification spec = getExtensionSpecification(name);

            if (spec == null)
                throw new IllegalArgumentException(
                    Tapestry.getString("LibrarySpecification.no-such-extension", this, name));

            result = spec.instantiateExtension(_resolver);

            _instantiatedExtensions.put(name, result);
        }

        return result;
    }

    /**
     *  Invoked after the entire specification has been constructed
     *  to instantiate any extensions marked immediate.
     * 
     **/

    public synchronized void instantiateImmediateExtensions()
    {
        if (_extensions == null)
            return;

        Iterator i = _extensions.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            ExtensionSpecification spec = (ExtensionSpecification) entry.getValue();

            if (!spec.isImmediate())
                continue;

            String name = (String) entry.getKey();

            getExtension(name);
        }

    }

    public IResourceResolver getResourceResolver()
    {
        return _resolver;
    }

    public void setResourceResolver(IResourceResolver resolver)
    {
        _resolver = resolver;
    }

    /**
     * Returns the extensions map.
     * @return Map
     */
    protected Map getExtensions() {
      return _extensions;
    }

   /**
    * Updates the extension map.
    * @param extensions A Map of extension specification paths 
    * keyed on extension id.
    * 
    * The map is retained, not copied.
    *
    */
   
    protected void setExtensions(Map extension) {
      _extensions = extension;
    }


    /**
     * Returns the libraries map.
     * @return Map
     */
    protected Map getLibraries() {
      return _libraries;
    }

   /**
    * Updates the library map.
    * @param libraries A Map of library specification paths 
    * keyed on library id.
    * 
    * The map is retained, not copied.
    *
    */
   
    protected void setLibraries(Map libraries) {
      _libraries = libraries;
    }

    /**
     * Returns the pages map.
     * @return Map
     */
    
    protected Map getPages() {
      return _pages;
    }
    
   /**
    * Updates the page map.
    * @param pages A Map of page specification paths 
    * keyed on page id.
    * 
    * The map is retained, not copied.
    *
    */
    
    protected void setPages(Map pages) {
      _pages = pages;
    }

    /**
     * Returns the services.
     * @return Map
     */
    
    protected Map getServices() {
      return _services;
    }

   /**
    * Updates the services map.
    * @param services A Map of the fully qualified names of classes of type {@link IEngineService} 
    * keyed on service id.
    * 
    * The map is retained, not copied.
    *
    */
   
    protected void setServices(Map services) {
      _services = services;
    }

    /**
     * Returns the components map.
     * @return Map
     */
    
    protected Map getComponents() {
      return _components;
    }

   /**
    * Updates the components map.
    * @param components A Map of {@link ContainedComponent} keyed on component id.
    * The map is retained, not copied.
    *
    */
   
    protected void setComponents(Map components) {
      _components = components;
    }
    
    /**
     *  Returns the XML Public Id for the library file, or null
     *  if not applicable.
     * 
     *  <p>
     *  This method exists as a convienience for the Spindle plugin.
     *  A previous method used an arbitrary version string, the
     *  public id is more useful and less ambiguous.
     *  
     *  @since 2.2
     * 
     **/

    public String getPublicId()
    {
        return _publicId;
    }

    /** @since 2.2 **/

    public void setPublicId(String publicId)
    {
        _publicId = publicId;
    }

}
