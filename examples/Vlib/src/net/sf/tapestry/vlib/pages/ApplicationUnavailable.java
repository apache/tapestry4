package net.sf.tapestry.vlib.pages;

import net.sf.tapestry.pages.Exception;
import net.sf.tapestry.vlib.IErrorProperty;

/**
 *  A page only displayed when the application is unavailable
 *  (typically because of repeated {@link java.rmi.RemoteException}s
 *  or {@link javax.naming.NamingException}s accessing EJBs.
 * 
 *  @see net.sf.tapestry.vlib.VirtualLibraryEngine#rmiFailure(String, RemoteException, boolean)
 *  @see net.sf.tapestry.vlib.VirtualLibraryEngine#namingFailure(String, NamingException, boolean)
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class ApplicationUnavailable extends Exception
implements IErrorProperty
{
    private String _error;
    
    public void detach()
    {
        _error = null;
        
        super.detach();
    }
    
    public String getError()
    {
        return _error;
    }

    public void setError(String value)
    {
        _error = value;
    }

    public void activate(String message, Throwable ex)
    {
        setError(message);
        
        setException(ex);
    }
}
