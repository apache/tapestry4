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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.contrib.ejb.XEJBException;

/**
 *  Implementation of a stateless session bean that sends mail.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class MailSenderBean implements SessionBean
{
    private static final Log LOG = LogFactory.getLog(MailSenderBean.class);

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