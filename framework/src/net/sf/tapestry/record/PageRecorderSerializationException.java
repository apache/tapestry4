package net.sf.tapestry.record;

import java.io.IOException;

/**
 *  A subclass of {@link IOException} used when page recorders are unable to
 *  serialize or deserialize their state.  This is necessary to store the
 *  {@link #getRootCause() root cause exception}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 * 
 **/

public class PageRecorderSerializationException extends IOException
{
    private Throwable rootCause;

    public PageRecorderSerializationException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}