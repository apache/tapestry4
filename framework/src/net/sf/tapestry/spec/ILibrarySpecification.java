package net.sf.tapestry.spec;

import java.util.List;
import java.util.Map;

import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.util.IPropertyHolder;

/**
 *  Interface for the Specification for a library.  {@link net.sf.tapestry.spec.ApplicationSpecification}
 *  is a specialized kind of library.
 *
 *  @author Geoffrey Longman
 *  @version $Id$
 *  @since 2.2
 *
 **/

public interface ILibrarySpecification extends IPropertyHolder
{

    /**
     *  Returns the specification path (within the classpath) for
     *  an embedded library, or null if
     *  no such library has been defined.
     * 
     **/
    
    public String getLibrarySpecificationPath(String id);

    /**
     *  Sets the specification path for an embedded library.
     * 
     *  @throws IllegalArgumentException if a library with the given
     *  id already exists
     * 
     **/

    public void setLibrarySpecificationPath(String id, String path);

    /**
     *  Returns a sorted list of library ids (or the empty list, but not null).
     * 
     **/
    
    public List getLibraryIds();
    
    public String getPageSpecificationPath(String name);

    public void setPageSpecificationPath(String name, String path);

    /**
     *  Returns a sorted list of page names explicitly defined by this library,
     *  or an empty list (but not null).
     * 
     **/
    
    public List getPageNames();
    
    public void setComponentSpecificationPath(String type, String path);

    public String getComponentSpecificationPath(String type);

    /**
     *  Returns the simple types ('alias' is an archaic term) of
     *  all components defined in this library.
     * 
     *  @deprecated To be removed after release 2.4, use
     *  {@link #getComponentTypes()} instead.
     * 
     **/
    
    public List getComponentAliases();

    /**
     *  Returns the simple types of all components defined in
     *  this library.  Returns a list of strings in sorted order,
     *  or an empty list (but not null).
     * 
     *  @since 2.4
     * 
     **/

    public List getComponentTypes();
    
    public String getServiceClassName(String name);

    /**
     *  Returns a sorted list of service names (or an empty list, but
     *  not null).
     * 
     **/
    
    public List getServiceNames();

    public void setServiceClassName(String name, String className);


    /**
     * 
     *  Returns the documentation for this library..
     * 
     * 
     **/

    public String getDescription();

    /**
     *  
     *  Sets the documentation for this library.
     * 
     * 
     **/

    public void setDescription(String description);
    
    /**
     *  Returns a Map of extensions; key is extension name, value is
     *  {@link net.sf.tapestry.spec.ExtensionSpecification}.
     *  May return null.  The returned Map is immutable.
     * 
     **/

    public Map getExtensionSpecifications();

    /**
     *  Adds another extension specification.
     *  
     **/

    public void addExtensionSpecification(String name, ExtensionSpecification extension);
    
    /**
     *  Returns a sorted List of the names of all extensions.  May return the empty list,
     *  but won't return null.
     * 
     **/

    public List getExtensionNames();
    
    /**
     *  Returns the named ExtensionSpecification, or null if it doesn't exist.
     * 
     **/

    public ExtensionSpecification getExtensionSpecification(String name);
    

    /**
     *  Returns an instantiated extension.  Extensions are created as needed and
     *  cached for later use.
     * 
     *  @throws IllegalArgumentException if no extension specification exists for the
     *  given name.
     * 
     **/

    public Object getExtension(String name);

    /**
     *  Returns an instantiated extension, performing a check to ensure
     *  that the extension is a subtype of the given class (or extends the given
     *  interface).
     * 
     *  @throws IllegalArgumentException if no extension specification exists for
     *  the given name, or if the extension fails the type check.
     * 
     *  @since 2.4
     * 
     **/
    
    public Object getExtension(String name, Class typeConstraint);

    /**
     *  Returns true if the named extension exists (or can be instantiated),
     *  returns false if the named extension has no specification.
     * 
     **/
    
    public boolean checkExtension(String name);

    /**
     *  Invoked after the entire specification has been constructed
     *  to instantiate any extensions marked immediate.
     * 
     **/

    public void instantiateImmediateExtensions();

    public IResourceResolver getResourceResolver();

    public void setResourceResolver(IResourceResolver resolver);
    
    public String getPublicId();
    
    public void setPublicId(String value);

    /**
     *  Returns the location from which the specification was read.
     * 
     *  @since 2.4
     * 
     **/
    
    public IResourceLocation getSpecificationLocation();
    
    /** @since 2.4 **/
    
    public void setSpecificationLocation(IResourceLocation specificationLocation);    
}
