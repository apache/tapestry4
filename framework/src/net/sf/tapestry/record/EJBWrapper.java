package net.sf.tapestry.record;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.ejb.EJBObject;
import javax.ejb.Handle;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Wraps an EJBObject, serializing and deserializing the EJBObject's handle.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class EJBWrapper implements Externalizable
{
    private EJBObject _ejb;

    public EJBWrapper()
    {
    }

    public EJBWrapper(EJBObject ejb)
    {
        _ejb = ejb;
    }

    public EJBObject getEJBObject()
    {
        return _ejb;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        Handle handle = (Handle)in.readObject();
        
        _ejb = (EJBObject)handle.getEJBObject();
    }

    /**
     *  Writes the handle for the EJB.
     * 
     **/
    
    public void writeExternal(ObjectOutput out) throws IOException
    {
        Handle handle = _ejb.getHandle();

        out.writeObject(handle);
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("ejb", _ejb);
        
        return builder.toString();
    }
}
