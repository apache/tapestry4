package net.sf.tapestry;

import java.io.InputStream;

/**
 *  Represents a file uploaded from a client side form.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public interface IUploadFile
{
	/**
	 *  Returns the name of the file that was uploaded.  This
	 *  is just the filename portion of the complete path.
	 * 
	 **/

	public String getFileName();

	/**
	 *  Returns the complete path, as reported by the client
	 *  browser.  Different browsers report different things
	 *  here.
	 * 
	 * 
	 *  @since 2.0.4
	 * 
	 **/
	
	public String getFilePath();

	/**
	 *  Returns true if the uploaded file was truncated.  In the current
	 *  implementation, truncation does not occur (which can result in uploaded
	 *  files eating a lot of memory).  A future enhancement may limit the
	 *  size of any single file uploaded, or various other measures.  Struts
	 *  (for example) has
	 *  a whole host of options targetted at defanging denial of service attacks.
	 * 
	 **/

	public boolean isTruncated();

	/**
	 *  Returns an input stream of the content of the file.  There is no guarantee
	 *  that this stream will be valid after the end of the current request cycle,
	 *  so it should be processed immediately.
	 * 
	 *  <p>As of release 1.0.8, this will be a a {@link java.io.ByteArrayInputStream},
	 *  but that, too, may change (a future implementation may upload the stream
	 *  to a temporary file and return an input stream from that).
	 * 
	 **/

	public InputStream getStream();
    
    /**
     *  Returns the MIME type specified when the file was uploaded.  May return null
     *  if the content type is not known.
     * 
     *  @since 2.2
     * 
     **/
    
    public String getContentType();
}