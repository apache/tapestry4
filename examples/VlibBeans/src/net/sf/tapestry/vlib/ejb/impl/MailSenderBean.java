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

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.tapestry.util.ejb.XEJBException;

/**
 *  Implementation of a stateless session bean that sends mail.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class MailSenderBean implements SessionBean
{
    private static final Logger LOG = LogManager.getLogger(MailSenderBean.class);

    private SessionContext context;

    /**
     *  Mail Session, obtained via JNDI.
     *
     **/

    private Session session;

    public void setSessionContext(SessionContext value)
    {
        context = value;
    }

    public void ejbCreate()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("ejbCreate()");

        try
        {
            Context initialContext = new InitialContext();
            Context environment = (Context) initialContext.lookup("java:comp/env");

            Object raw = environment.lookup("vlib/Mail");

            session = (Session) PortableRemoteObject.narrow(raw, Session.class);
        }
        catch (NamingException ex)
        {
            throw new XEJBException("Unable to lookup mail session in JNDI.", ex);
        }
    }

    public void create()
    {
    }

    public void ejbRemove()
    {
        context = null;
    }

    public void ejbActivate()
    {
    }

    public void ejbPassivate()
    {
    }

    public void sendMail(String emailAddress, String subject, String content)
    {
        MimeMessage message = new MimeMessage(session);

        try
        {
            message.setFrom();

            Address[] to = new Address[] { new InternetAddress(emailAddress)};

            message.addRecipients(Message.RecipientType.TO, to);

            message.setSubject(subject);

            message.setContent(content, "text/plain");

            Transport.send(message);
        }
        catch (MessagingException ex)
        {
            ex.printStackTrace();
        }
    }
}