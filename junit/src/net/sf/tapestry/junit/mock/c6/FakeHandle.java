package net.sf.tapestry.junit.mock.c6;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;
import javax.ejb.Handle;

/**
 *  Used for testing of page persistence.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class FakeHandle implements Handle
{
    private FakeEJBObject _ejb;
    
    public FakeHandle(FakeEJBObject ejb)
    {
        _ejb = ejb;
    }
    
    public EJBObject getEJBObject() throws RemoteException
    {
        return _ejb;
    }

}
