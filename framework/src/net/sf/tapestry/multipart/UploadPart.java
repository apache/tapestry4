//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.multipart;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.Tapestry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
    private static final Logger LOG = LogManager.getLogger(UploadPart.class);

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