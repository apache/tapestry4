package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 *  Remote interface to the KeyAllocator stateless
 *  session bean.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 */

public interface IKeyAllocator extends EJBObject
{
    /**
     *  Allocates a new key, possibling reserving it from
     *  the database.  The value returned is guarenteed to
     *  not have been previously returned by any instance.
     *
     */

    public Integer allocateKey() throws RemoteException;

    /**
     * Allocates several keys, as if invoking {@link #allocateKey}
     * multiple times.  No guarentees are made that the
     * values are sequential or in any order, just that they
     * are unique.
     *
     */

    public Integer[] allocateKeys(int count) throws RemoteException;
}