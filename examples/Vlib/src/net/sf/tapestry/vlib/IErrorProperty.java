package net.sf.tapestry.vlib;

/**
 *  Marks pages that have an error property (of type String).
 *
 *  @see SimpleValidationDelegate
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IErrorProperty
{
    public String getError();

    public void setError(String value);
}