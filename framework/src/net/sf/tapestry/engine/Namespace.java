package net.sf.tapestry.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.resource.ClasspathResourceLocation;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.LibrarySpecification;

/**
 *  Implementation of {@link net.sf.tapestry.INamespace}
 *  that works with a {@link net.sf.tapestry.ISpecificationSource} to
 *  obtain page and component specifications as needed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class Namespace implements INamespace
{
    private ILibrarySpecification _specification;
    private ISpecificationSource _specificationSource;
    private String _id;
    private String _extendedId;
    private INamespace _parent;
    private boolean _frameworkNamespace;
    private boolean _applicationNamespace;

    /**
     *  Map of {@link ComponentSpecification} keyed on page name.
     *  The map is synchronized because different threads may
     *  try to update it simultaneously (due to dynamic page
     *  discovery in the application namespace).
     * 
     **/

    private Map _pages = Collections.synchronizedMap(new HashMap());

    /**
     *  Map of {@link ComponentSpecification} keyed on
     *  component alias.
     * 
     **/

    private Map _components = Collections.synchronizedMap(new HashMap());

    /**
     *  Map, keyed on id, of {@link INamespace}.
     * 
     **/

    private Map _children = Collections.synchronizedMap(new HashMap());

    public Namespace(
        String id,
        INamespace parent,
        ILibrarySpecification specification,
        ISpecificationSource specificationSource)
    {
        _id = id;
        _parent = parent;
        _specification = specification;
        _specificationSource = specificationSource;

        _applicationNamespace = (_id == null);
        _frameworkNamespace = FRAMEWORK_NAMESPACE.equals(_id);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Namespace@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append('[');

        if (_applicationNamespace)
            buffer.append("<application>");
        else
            buffer.append(getExtendedId());

        buffer.append(']');

        return buffer.toString();
    }

    public String getId()
    {
        return _id;
    }

    public String getExtendedId()
    {
        if (_applicationNamespace)
            return null;

        if (_extendedId == null)
            _extendedId = buildExtendedId();

        return _extendedId;
    }

    public INamespace getParentNamespace()
    {
        return _parent;
    }

    public INamespace getChildNamespace(String id)
    {
        String firstId = id;
        String nextIds = null;

        // Split the id into first and next if it is a dot separated sequence
        int index = id.indexOf('.');
        if (index >= 0)
        {
            firstId = id.substring(0, index);
            nextIds = id.substring(index + 1);
        }

        // Get the first namespace
        INamespace result = (INamespace) _children.get(firstId);

        if (result == null)
        {
            result = createNamespace(firstId);

            _children.put(firstId, result);
        }

        // If the id is a dot separated sequence, recurse to find 
        // the needed namespace
        if (result != null && nextIds != null)
            result = result.getChildNamespace(nextIds);

        return result;
    }

    public List getChildIds()
    {
        return _specification.getLibraryIds();
    }

    public ComponentSpecification getPageSpecification(String name)
    {
        ComponentSpecification result = (ComponentSpecification) _pages.get(name);

        if (result == null)
        {
            result = locatePageSpecification(name);

            _pages.put(name, result);
        }

        return result;
    }

    public List getPageNames()
    {
        Set names = new HashSet();

        names.addAll(_pages.keySet());
        names.addAll(_specification.getPageNames());

        List result = new ArrayList(names);

        Collections.sort(result);

        return result;
    }

    public ComponentSpecification getComponentSpecification(String alias)
    {
        ComponentSpecification result = (ComponentSpecification) _components.get(alias);

        if (result == null)
        {
            result = locateComponentSpecification(alias);
            _components.put(alias, result);
        }

        return result;
    }

    public List getComponentAliases()
    {
        Set types = new HashSet();
        
        types.addAll(_components.keySet());
        types.addAll(_specification.getComponentTypes());
        
        List result = new ArrayList(types);
        
        Collections.sort(result);
        
        return result;
    }

    public String getServiceClassName(String name)
    {
        return _specification.getServiceClassName(name);
    }

    public List getServiceNames()
    {
        return _specification.getServiceNames();
    }

    public ILibrarySpecification getSpecification()
    {
        return _specification;
    }

    private String buildExtendedId()
    {
        if (_parent == null)
            return _id;

        String parentId = _parent.getExtendedId();

        // If immediate child of application namespace

        if (parentId == null)
            return _id;

        return parentId + "." + _id;
    }

    /**
     *  Returns a string identifying the namespace, for use in
     *  error messages.  I.e., "Application namespace" or "namespace 'foo'".
     * 
     **/

    public String getNamespaceId()
    {
        if (_frameworkNamespace)
            return Tapestry.getString("Namespace.framework-namespace");

        if (_applicationNamespace)
            return Tapestry.getString("Namespace.application-namespace");

        return Tapestry.getString("Namespace.nested-namespace", getExtendedId());
    }

    /**
     *  Gets the specification from the specification source.
     * 
     *  @throws ApplicationRuntimeException if the named page is not defined.
     * 
     **/

    private ComponentSpecification locatePageSpecification(String name)
    {
        String path = _specification.getPageSpecificationPath(name);

        if (path == null)
            throw new ApplicationRuntimeException(Tapestry.getString("Namespace.no-such-page", name, getNamespaceId()));

        IResourceLocation location = getSpecificationLocation().getRelativeLocation(path);

        return _specificationSource.getPageSpecification(location);
    }

    private ComponentSpecification locateComponentSpecification(String type)
    {
        String path = _specification.getComponentSpecificationPath(type);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("Namespace.no-such-alias", type, getNamespaceId()));

        IResourceLocation location = getSpecificationLocation().getRelativeLocation(path);

        return _specificationSource.getComponentSpecification(location);
    }

    private INamespace createNamespace(String id)
    {
        String path = _specification.getLibrarySpecificationPath(id);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("Namespace.library-id-not-found", id, getNamespaceId()));

        IResourceLocation location = getSpecificationLocation().getRelativeLocation(path);

        // Ok, an absolute path to a library for an application whose specification
        // is in the context root is problematic, cause getRelativeLocation()
        // will still be looking in the context.  Handle this case with the
        // following little kludge:

        if (location.getResourceURL() == null && path.startsWith("/"))
            location = new ClasspathResourceLocation(_specification.getResourceResolver(), path);

        ILibrarySpecification ls = _specificationSource.getLibrarySpecification(location);

        return new Namespace(id, this, ls, _specificationSource);
    }

    public boolean containsAlias(String type)
    {
        return _components.containsKey(type) || (_specification.getComponentSpecificationPath(type) != null);
    }

    public boolean containsPage(String name)
    {
        return _pages.containsKey(name) || (_specification.getPageSpecificationPath(name) != null);
    }

    /** @since 2.3 **/

    public String constructQualifiedName(String pageName)
    {
        String prefix = getExtendedId();

        if (prefix == null)
            return pageName;

        return prefix + SEPARATOR + pageName;
    }

    /** @since 2.4 **/

    public IResourceLocation getSpecificationLocation()
    {
        return _specification.getSpecificationLocation();
    }

    /** @since 2.4 **/

    public boolean isApplicationNamespace()
    {
        return _applicationNamespace;
    }

    /** @since 2.4 **/

    public synchronized void installPageSpecification(String pageName, ComponentSpecification specification)
    {
        _pages.put(pageName, specification);
    }

    /** @since 2.4 **/

    public synchronized void installComponentSpecification(String type, ComponentSpecification specification)
    {
        _components.put(type, specification);
    }

    // On these renamed methods, we simply invoke the old, deprecated method
    // for code coverage reasons.  In 2.5 we delete the deprecated method
    // and move its body here.

    /** @since 2.4 **/

    public boolean containsComponentType(String type)
    {
        return containsAlias(type);
    }

    /** @since 2.4 **/

    public List getComponentTypes()
    {
        return getComponentAliases();
    }

}
