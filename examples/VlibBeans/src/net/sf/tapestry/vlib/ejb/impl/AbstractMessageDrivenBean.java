//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.vlib.ejb.impl;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.tapestry.util.ejb.XEJBException;

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