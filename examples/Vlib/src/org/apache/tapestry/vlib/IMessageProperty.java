package org.apache.tapestry.vlib;

/**
 *  String message property, for many pages that can display an informational message.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public interface IMessageProperty
{
    public String getMessage();
    public void setMessage(String message);
}
