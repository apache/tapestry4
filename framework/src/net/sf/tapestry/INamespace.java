package net.sf.tapestry;

import java.util.List;

import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.LibrarySpecification;
import net.sf.tapestry.util.IPropertyHolder;

/**
 *  Organizes different libraries of Tapestry pages, components
 *  and services into "frameworks", used to disambiguate names.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public interface INamespace
{
    /**
     *  Reserved name of a the implicit Framework library.
     * 
     **/
    
    public static final String FRAMEWORK_NAMESPACE = "framework";
    
    /**
     *  Character used to seperate the namespace prefix from the page name
     *  or component type.
     * 
     *  @since 2.3
     * 
     **/
    
    public static final char SEPARATOR = ':';
    
    /**
     *  Returns an identifier for the namespace.  Identifiers
     *  are simple names (they start with a letter,
     *  and may contain letters, numbers, underscores and dashes).
     *  An identifier must be unique among a namespaces siblings.
     * 
     *  <p>The application namespace has a null id; the framework
     *  namespace has an id of "framework".
     * 
     **/
    
    public String getId();
    
    /**
     *  Returns the extended id for this namespace, which is
     *  a dot-seperated sequence of ids.
     * 
     **/
    
    public String getExtendedId();
    
    /**
     *  Returns the parent namespace; the namespace which
     *  contains this namespace.
     * 
     *  <p>
     *  The application and framework namespaces return null
     *  as the parent.
     * 
     **/
    
    public INamespace getParentNamespace();
    
      
    /**
     *  Returns a namespace contained by this namespace.
     * 
     *  @param id either a simple name (of a directly contained namespace),
     *  or a dot-seperarated name sequence.
     *  @return the child namespace
     *  @throws ApplicationRuntimeException if not such namespace exist.
     * 
     **/
    
    public INamespace getChildNamespace(String id);
        
    /**
     *  Returns a sorted, immutable list of the ids of the immediate
     *  children of this namespace.  May return the empty list,
     *  but won't return null.
     * 
     **/
    
    public List getChildIds();
    
    /**
     *  Returns the path for the page specification of the named
     *  page (defined within the namespace).
     * 
     *  @param name the name of the page
     *  @return the specification
     *  @throws ApplicationRuntimeException if the page specification
     *  doesn't exist or can't be loaded
     * 
     **/
    
    public ComponentSpecification getPageSpecification(String name);
    
    /**
     *  Returns true if this namespace contains the specified
     *  page name.
     * 
     **/
    
    public boolean containsPage(String name);
    
    /**
     *  Returns a sorted list of page names.  May return an empty
     *  list, but won't return null.  The return list is immutable.
     * 
     **/
    
    public List getPageNames();
    
    /**
     *  Returns the path for the named component (within the namespace).
     * 
     *  @param alias the component alias
     *  @return the specification path of the component
     *  @throws ApplicationRuntimeException if the specification
     *  doesn't exist or can't be loaded
     * 
     **/
 
   public ComponentSpecification getComponentSpecification(String alias);
   
   
   /**
    *  Returns true if the namespace contains the indicated alias.
    * 
    **/
   
   public boolean containsAlias(String alias);
   
   /**
    *  Returns a sorted list of component aliases.  May return 
    *  an empty list, but won't return null.  The return list
    *  is immutable.
    * 
    **/
   
   public List getComponentAliases();
   
   /**
    *  Returns the class name of a service provided by the
    *  namespace.
    * 
    *  @param name the name of the service.
    *  @return the complete class name of the service, or null
    *  if the namespace does not contain the named service.
    * 
    **/
   
   public String getServiceClassName(String name);
   
   /**
    *  Returns the names of all services provided by the
    *  namespace, as a sorted, immutable list.  May return
    *  the empty list, but won't return null.
    * 
    **/
   
   public List getServiceNames();
   
   /**
    *  Returns the {@link LibrarySpecification} from which
    *  this namespace was created.
    * 
    **/
   
   public ILibrarySpecification getSpecification();
   
   /**
    *  Constructs a qualified name for the given simple page name by
    *  applying the correct prefix (if any).
    * 
    *  @since 2.3
    * 
    **/
   
   public String constructQualifiedName(String pageName);
   
   /**
    *  Returns the location of the resource from which the
    *  specification for this namespace was read.
    * 
    **/
   
   public IResourceLocation getSpecificationLocation();
    
}
