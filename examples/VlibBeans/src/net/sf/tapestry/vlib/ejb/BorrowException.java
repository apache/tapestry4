package net.sf.tapestry.vlib.ejb;

/**
 *  Throws when a book may not be borrowed.
 *
 *  @see IOperations#borrowBook(Integer,Integer)
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class BorrowException extends Exception
{
    public BorrowException(String message)
    {
        super(message);
    }
}