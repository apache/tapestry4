package net.sf.tapestry;

import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.LibrarySpecification;

/**
 *  Defines access to component specifications.
 *
 *  @see ComponentSpecification
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface ISpecificationSource
{    
    /**
     *  Retrieves a component specification, parsing it as necessary.
     *  
     *  @param resourcePath a full resource path of a
     *  component specification file.
     * 
     *  @throws ApplicationRuntimeException if the specification doesn't
     *  exist, is unreadable or invalid.
     * 
     *  @since 2.2
     * 
     **/
    
    public ComponentSpecification getComponentSpecification(String resourcePath);  
 
    /**
     *  Retrieves a component specification, parsing it as necessary.
     *  
     *  @param resourcePath a full resource path of a
     *  page specification file.
     * 
     *  @throws ApplicationRuntimeException if the specification doesn't
     *  exist, is unreadable or invalid.
     * 
     *  @since 2.2
     * 
     **/
        
    public ComponentSpecification getPageSpecification(String resourcePath);
            
	/**
	 *  Gets a specification from the cache, possibly parsing it at the same time.
	 *
	 *  <p>The type is used to locate the resource that defines the specification.  In
	 *  practical terms, this is the XML file which contains the specification.
	 *
	 *  @throws ApplicationRuntimeException if the specification cannot be located or loaded.
	 *
     *  @deprecated To be removed in 2.3.  
     *  Use {@link #getComponentSpecification(String)} or {@link #getPageSpecification(String)}
     *  instead.
     * 
	 **/

	public ComponentSpecification getSpecification(String type);
    
	/**
	 *  Invoked to have the source clear any internal cache.  This is most often
	 *  used when debugging an application.
	 *
	 **/

	public void reset();
    
    /**
     *  Returns a {@link INamespace} for the given id.
     * 
     *  @param id the name of the namespace, possibly as a dotted name
     *  sequence.  Null for the application namespace, "framework"
     *  for the framework namespace.
     *  @return the namespace
     *  @throws ApplicationRuntimeException if the namespace cannot
     *  be located.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getNamespace(String id);
    
    /**
     *  Returns a {@link LibrarySpecification} with the given path.
     * 
     *  @param resourcePath the resource path of the specification
     *  to return
     *  @throws ApplicationRuntimeException if the specification
     *  cannot be read
     * 
     *  @since 2.2
     * 
     **/
    
    public ILibrarySpecification getLibrarySpecification(String resourcePath);
    
    /**
     *  Returns the {@link INamespace} for the application.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getApplicationNamespace();
    
    
    /**
     *  Returns the {@link INamespace} for the framework itself.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getFrameworkNamespace();
    
}