package net.sf.tapestry.multipart;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.Tapestry;

/**
 *  Portion of a multi-part request representing an uploaded file.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 *
 **/

public class UploadPart implements IUploadFile, IPart
{
    private static final Log LOG = LogFactory.getLog(UploadPart.class);

    private byte[] _content;
    private File _contentFile;
    private String _filePath;
    private String _contentType;

    UploadPart(String filePath, String contentType, byte[] content)
    {
        _filePath = filePath;
        _contentType = contentType;
        _content = content;
    }

    UploadPart(String filePath, String contentType, File contentFile)
    {
        _filePath = filePath;
        _contentType = contentType;        
        _contentFile = contentFile;
    }

	/**
	 *  @since 2.0.4
	 * 
	 **/
	
    public String getFilePath()
    {
        return _filePath;
    }

	/**
	 *  Always returns false, at least so far.  Future enhancements
	 *  may involve truncating the input if it exceeds a certain
	 *  size or upload time (such things may be used for denial
	 *  of service attacks).
	 * 
	 **/
	
    public boolean isTruncated()
    {
        return false;
    }

    public InputStream getStream()
    {
        if (_content != null)
            return new ByteArrayInputStream(_content);

        try
        {
            return new FileInputStream(_contentFile);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "UploadPart.unable-to-open-content-file",
                    _filePath,
                    _contentFile.getAbsolutePath()),
                ex);
        }
    }

    /**
     *  Deletes the external content file, if one exists.
     * 
     **/

    public void cleanup()
    {
        if (_contentFile != null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Deleting upload file " + _contentFile + ".");

            boolean success = _contentFile.delete();

            if (!success)
                LOG.warn(
                    Tapestry.getString(
                        "UploadPart.temporary-file-not-deleted",
                        _contentFile.getAbsolutePath()));

            // In rare cases (when exceptions are thrown while the request
            // is decoded), cleanup() may be called multiple times.

            _contentFile = null;
        }

    }
    
    
    /**
     *  Leverages {@link File} to convert the full file path and extract
     *  the name.
     * 
     **/
    
    public String getFileName()
    {
  		File file = new File(_filePath);
  		
  		return file.getName();
    }

    public String getContentType()
    {
        return _contentType;
    }

}