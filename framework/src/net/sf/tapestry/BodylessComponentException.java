package net.sf.tapestry;

/**
 *  Exception thrown when a template has a body-less {@link IComponent} wraps
 *  any content.  The exception is thrown by the container object.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 **/

public class BodylessComponentException extends PageLoaderException
{
    public BodylessComponentException(IComponent component)
    {
        super(Tapestry.getString("BodylessComponentException.message"), component);
    }
}