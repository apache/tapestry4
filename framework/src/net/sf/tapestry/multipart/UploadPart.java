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
import org.apache.log4j.Category;

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
    private static final Category CAT = Category.getInstance(UploadPart.class);

    private byte[] content;
    private File contentFile;
    private String filePath;

    UploadPart(String filePath, byte[] content)
    {
        this.filePath = filePath;
        this.content = content;
    }

    UploadPart(String filePath, File contentFile)
    {
        this.filePath = filePath;
        this.contentFile = contentFile;
    }

	/**
	 *  @since 2.0.4
	 * 
	 **/
	
    public String getFilePath()
    {
        return filePath;
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
        if (content != null)
            return new ByteArrayInputStream(content);

        try
        {
            return new FileInputStream(contentFile);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "UploadPart.unable-to-open-content-file",
                    filePath,
                    contentFile.getAbsolutePath()),
                ex);
        }
    }

    /**
     *  Deletes the external content file, if one exists.
     * 
     **/

    public void cleanup()
    {
        if (contentFile != null)
        {
            if (CAT.isDebugEnabled())
                CAT.debug("Deleting upload file " + contentFile + ".");

            boolean success = contentFile.delete();

            if (!success)
                CAT.warn(
                    Tapestry.getString(
                        "UploadPart.temporary-file-not-deleted",
                        contentFile.getAbsolutePath()));

            // In rare cases (when exceptions are thrown while the request
            // is decoded), cleanup() may be called multiple times.

            contentFile = null;
        }

    }
    
    
    /**
     *  Leverages {@link File} to convert the full file path and extract
     *  the name.
     * 
     **/
    
    public String getFileName()
    {
  		File file = new File(filePath);
  		
  		return file.getName();
    }

}