/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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