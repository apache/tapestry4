package net.sf.tapestry.vlib.ejb;

/**
 *  Provides property names used with a {@link javax.jms.MapMessage} when
 *  communicating between beans via queues.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IMailMessageConstants
{
    /**
     *  The address to which the mail should be sent.
     *
     **/

    public static final String EMAIL_ADDRESS = "emailAddress";

    /**
     *  The subject line for the mail message (should be short).
     *
     **/

    public static final String SUBJECT = "subject";

    /**
     *  The content (or body) of the mail message.  Should be simple ASCII (for now).
     *
     **/

    public static final String CONTENT = "content";
}