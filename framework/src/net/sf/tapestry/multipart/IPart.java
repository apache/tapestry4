package net.sf.tapestry.multipart;

/**
 *  Common interface for data parts from multipart form submissions.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 * 
 **/

public interface IPart
{
    /**
     *  Invoked at the end of a request cycle to delete any resources held by
     *  the part.
     * 
     *  @see UploadPart#cleanup()
     * 
     **/

    public void cleanup();
}