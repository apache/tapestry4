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

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.vlib.ejb.IMailMessageConstants;
import net.sf.tapestry.vlib.ejb.IMailSender;
import net.sf.tapestry.vlib.ejb.IMailSenderHome;

/**
 *  Message driven bean used to process outgoing mail (concerning the borrowing and return of
 *  books).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class MailQueueBean extends AbstractMessageDrivenBean implements IMailMessageConstants
{
    private static final Log LOG = LogFactory.getLog(MailQueueBean.class);

    private static IMailSenderHome mailSenderHome;
    private transient IMailSender mailSender;

    /**
     *  Logs the message receipt; more to come soon.
     *
     **/

    public void onMessage(Message message)
    {
        IMailSender sender = null;
        String emailAddress;
        String subject;
        String content;

        try
        {
            emailAddress = message.getStringProperty(EMAIL_ADDRESS);
            subject = message.getStringProperty(SUBJECT);

            TextMessage textMessage = (TextMessage) message;

            content = textMessage.getText();
        }
        catch (JMSException ex)
        {
            LOG.error("Unable to extract properties from message.", ex);

            return;
        }

        try
        {
            sender = getMailSender();
        }
        catch (RemoteException ex)
        {
            LOG.error("Unable to obtain IMailSender instance.", ex);
        }

        try
        {
            sender.sendMail(emailAddress, subject, content);
        }
        catch (RemoteException ex)
        {
            LOG.error("Remote exception sending mail.", ex);

            return;
        }
        catch (EJBException ex)
        {
            LOG.error("Error sending mail: " + ex.getMessage(), ex);

            return;
        }

        // Not clear if we have to acknowledge the message here.
        // What about TP stuff? 
    }

    private IMailSender getMailSender() throws RemoteException
    {
        if (mailSender != null)
            return mailSender;

        if (mailSenderHome == null)
        {
            try
            {
                Object raw = environment.lookup("ejb/MailSender");

                mailSenderHome = (IMailSenderHome) PortableRemoteObject.narrow(raw, IMailSenderHome.class);
            }
            catch (NamingException ex)
            {
                throw new RemoteException("Unable to obtain reference to IMailSenderHome.", ex);
            }
        }

        try
        {
            mailSender = mailSenderHome.create();
        }
        catch (CreateException ex)
        {
            throw new RemoteException("Unable to create new instance of MailSender bean.", ex);
        }

        return mailSender;
    }
}