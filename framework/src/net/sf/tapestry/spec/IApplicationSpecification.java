package net.sf.tapestry.spec;



/**
 *  Defines and interface for the configuration for a Tapestry application.  An ApplicationSpecification
 *  extends {@link ILibrarySpecification} by adding new properties 
 *  name and engineClassName.
 *
 *  @author Geoffrey Longman
 *  @version $Id$
 *
 **/

public interface IApplicationSpecification extends ILibrarySpecification
{
    /**
     *  Returns a "user friendly" name for the application (which is optional).
     * 
     **/
    
    public String getName();

    public void setEngineClassName(String value);
    
    /**
     *  Returns the name of the class (which implements {@link net.sf.tapestry.IEngine}).
     *  May return null, in which case a default is used.
     * 
     **/
    
    public String getEngineClassName();
    
    public void setName(String name);
}