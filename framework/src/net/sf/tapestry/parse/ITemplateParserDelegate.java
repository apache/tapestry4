package net.sf.tapestry.parse;

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
     **/

    public boolean getAllowBody(String componentId);
}