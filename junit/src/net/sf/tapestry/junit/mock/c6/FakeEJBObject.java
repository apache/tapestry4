package net.sf.tapestry.junit.mock.c6;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

/**
 *  Used for testing of page persistence.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class FakeEJBObject implements EJBObject, Serializable
{
    private int _value;
    
    public FakeEJBObject(int value)
    {
        _value = value;
    }

    public EJBHome getEJBHome() throws RemoteException
    {
        return null;
    }

    public Handle getHandle() throws RemoteException
    {
        return new FakeHandle(this);
    }

    public Object getPrimaryKey() throws RemoteException
    {
        return null;
    }

    public boolean isIdentical(EJBObject arg0) throws RemoteException
    {
        return false;
    }

    public void remove() throws RemoteException, RemoveException
    {
    }

    public String toString()
    {
        return "FakeEJBObject[" + _value + "]";
    }
}
