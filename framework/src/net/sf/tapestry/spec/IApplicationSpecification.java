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
 
    public String getName();

    public void setEngineClassName(String value);
    
    public String getEngineClassName();
    
    public void setName(String name);


}