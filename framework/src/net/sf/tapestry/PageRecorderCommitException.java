package net.sf.tapestry;

/**
 *  Exception thrown when an {@link IPageRecorder} is unable to
 *  {@link IPageRecorder#commit()} its changes to external storage.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class PageRecorderCommitException extends Exception
{
    private transient IPageRecorder pageRecorder;
    private Throwable rootCause;

    public PageRecorderCommitException(String message, IPageRecorder pageRecorder)
    {
        super(message);

        this.pageRecorder = pageRecorder;
    }

    public PageRecorderCommitException(String message, IPageRecorder pageRecorder, Throwable rootCause)
    {
        super(message);

        this.pageRecorder = pageRecorder;
        this.rootCause = rootCause;
    }

    public IPageRecorder getPageRecorder()
    {
        return pageRecorder;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}