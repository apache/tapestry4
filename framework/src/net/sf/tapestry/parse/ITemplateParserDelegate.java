package net.sf.tapestry.parse;

import net.sf.tapestry.PageLoaderException;

/**
 *  Provides a {@link TemplateParser} with additional information about
 *  dynamic components.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface ITemplateParserDelegate
{
    /**
     *  Returns true if the component id is valid, false if the
     *  component id is not recognized.
     *
     **/

    public boolean getKnownComponent(String componentId);

    /**
     *  Returns true if the specified component allows a body, false
     *  otherwise.  The parser uses this information to determine
     *  if it should ignore the body of a tag.
     *
     *  @throws NoSuchComponentException if no such component exists
     * 
     **/

    public boolean getAllowBody(String componentId);
    
    /**
     *  Used with implicit components to determine if the component
     *  allows a body or not.
     * 
     *  @param libraryId the specified library id, possibly null
     *  @param type the component type
     * 
     *  @throws PageLoaderException if the specification cannot be found
     * 
     *  @since 2.4
     * 
     **/
    
    public boolean getAllowBody(String libraryId, String type)
    throws PageLoaderException;
}