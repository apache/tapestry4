/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.spec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;

/**
 *  Specification for a library.  {@link org.apache.tapestry.spec.ApplicationSpecification}
 *  is a specialized kind of library.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class LibrarySpecification extends LocatablePropertyHolder implements ILibrarySpecification
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

    /**
     *  The location of the specification.
     * 
     **/

    private IResourceLocation _specificationLocation;

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
            throw new IllegalArgumentException(
                Tapestry.getString("LibrarySpecification.duplicate-page-name", name));

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

    /**
     *  @since 2.4
     * 
     **/

    public List getComponentTypes()
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
            throw new IllegalArgumentException(
                Tapestry.getString("LibrarySpecification.duplicate-service-name", name));

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

    /**
     *  Returns a Map of extensions; key is extension name, value is
     *  {@link org.apache.tapestry.spec.ExtensionSpecification}.
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
     *  Returns true if this library specification has a specification
     *  for the named extension.
     * 
     **/

    public boolean checkExtension(String name)
    {
        if (_extensions == null)
            return false;

        return _extensions.containsKey(name);
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
        return getExtension(name, null);
    }

    /** @since 2.4 **/

    public synchronized Object getExtension(String name, Class typeConstraint)
    {
        if (_instantiatedExtensions == null)
            _instantiatedExtensions = new HashMap();

        Object result = _instantiatedExtensions.get(name);

        if (result == null)
        {
            ExtensionSpecification spec = getExtensionSpecification(name);

            if (spec == null)
                throw new IllegalArgumentException(
                    Tapestry.getString("LibrarySpecification.no-such-extension", name));

            result = spec.instantiateExtension(_resolver);

            _instantiatedExtensions.put(name, result);
        }

        if (typeConstraint != null)
            applyTypeConstraint(name, result, typeConstraint);

        return result;
    }

    /**
     *  Checks that an extension conforms to the supplied type constraint.
     * 
     *  @throws IllegalArgumentException if the extension fails the check.
     * 
     *  @since 2.4
     *  
     **/

    protected void applyTypeConstraint(String name, Object extension, Class typeConstraint)
    {
        Class extensionClass = extension.getClass();

        // Can you assign an instance of the extension to a variable
        // of type typeContraint legally?

        if (typeConstraint.isAssignableFrom(extensionClass))
            return;

        String key =
            typeConstraint.isInterface()
                ? "LibrarySpecification.extension-does-not-implement-interface"
                : "LibrarySpecification.extension-not-a-subclass";

        throw new IllegalArgumentException(
            Tapestry.getString(key, name, extensionClass.getName(), typeConstraint.getName()));
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
     *  Returns the extensions map.
     *  @return Map of objects.
     * 
     **/

    protected Map getExtensions()
    {
        return _extensions;
    }

    /**
     *  Updates the extension map.
     *  @param extensions A Map of extension specification paths 
     *  keyed on extension id.
     * 
     * <p>The map is retained, not copied.
     *
     **/

    protected void setExtensions(Map extension)
    {
        _extensions = extension;
    }

    /**
     *  Returns the libraries map.
     *  @return Map of {@link LibrarySpecification}.
     * 
     **/

    protected Map getLibraries()
    {
        return _libraries;
    }

    /**
     *  Updates the library map.
     *  @param libraries A Map of library specification paths 
     *  keyed on library id.
     * 
     *  <p>The map is retained, not copied.
     *
     **/

    protected void setLibraries(Map libraries)
    {
        _libraries = libraries;
    }

    /**
     *  Returns the pages map.
     *  @return Map of {@link ComponentSpecification}.
     * 
     **/

    protected Map getPages()
    {
        return _pages;
    }

    /**
     *  Updates the page map.
     *  @param pages A Map of page specification paths 
     *  keyed on page id.
     * 
     *  <p>The map is retained, not copied.
     *
     **/

    protected void setPages(Map pages)
    {
        _pages = pages;
    }

    /**
     * Returns the services.
     * @return Map of service class names.
     * 
     **/

    protected Map getServices()
    {
        return _services;
    }

    /**
     *  Updates the services map.
     *  @param services A Map of the fully qualified names of classes 
     *  which implement
     *  {@link org.apache.tapestry.IEngineService} 
     *  keyed on service id.
     * 
     *  <p>The map is retained, not copied.
     *
     **/

    protected void setServices(Map services)
    {
        _services = services;
    }

    /**
     *  Returns the components map.
     *  @return Map of {@link ContainedComponent}.
     * 
     **/

    protected Map getComponents()
    {
        return _components;
    }

    /**
     *  Updates the components map.
     *  @param components A Map of {@link ContainedComponent} keyed on component id.
     *  The map is retained, not copied.
     *
     **/

    protected void setComponents(Map components)
    {
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
     * 
     **/

    public String getPublicId()
    {
        return _publicId;
    }

    public void setPublicId(String publicId)
    {
        _publicId = publicId;
    }

    /** @since 2.4 **/

    public IResourceLocation getSpecificationLocation()
    {
        return _specificationLocation;
    }

    /** @since 2.4 **/

    public void setSpecificationLocation(IResourceLocation specificationLocation)
    {
        _specificationLocation = specificationLocation;
    }

    /** @since 2.4 **/

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("components", _components);
        builder.append("description", _description);
        builder.append("instantiatedExtensions", _instantiatedExtensions);
        builder.append("libraries", _libraries);
        builder.append("pages", _pages);
        builder.append("publicId", _publicId);
        builder.append("resolver", _resolver);
        builder.append("services", _services);
        builder.append("specificationLocation", _specificationLocation);

        extendDescription(builder);

        return builder.toString();
    }

    /**
     *  Does nothing, subclasses may override to add additional
     *  description.
     * 
     *  @see #toString()
     *  @since 2.4
     * 
     **/

    protected void extendDescription(ToStringBuilder builder)
    {
    }

}
