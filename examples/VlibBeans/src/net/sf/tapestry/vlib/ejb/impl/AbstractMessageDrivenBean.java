package net.sf.tapestry.vlib.ejb.impl;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.tapestry.contrib.ejb.XEJBException;

/**
 *  Abstract base class for implementing message driven beans.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class AbstractMessageDrivenBean implements MessageDrivenBean, MessageListener
{
    private MessageDrivenContext context;

    /**
     *  Set by {@link #ejbCreate()}, this contains
     *  the <code>java:comp/env</code> context.
     *
     **/

    protected Context environment;

    public void setMessageDrivenContext(MessageDrivenContext value) throws EJBException
    {
        context = value;
    }

    /**
     *  Sets up the {@link #environment} instance variable.
     *
     **/

    public void ejbCreate() throws EJBException
    {
        try
        {
            Context initial = new InitialContext();
            environment = (Context) initial.lookup("java:comp/env");
        }
        catch (NamingException ex)
        {
            throw new XEJBException("Could not lookup environment.", ex);
        }
    }

    /**
     *   Clears the {@link MessageDrivenContext} attribute.
     *
     **/

    public void ejbRemove() throws EJBException
    {
        context = null;
    }
}