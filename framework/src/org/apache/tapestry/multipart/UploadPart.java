//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.IUploadFile;

/**
 *  Portion of a multi-part request representing an uploaded file.
 *
 *  @author Joe Panico
 *  @version $Id$
 *  @since 2.0.1
 *
 **/
public class UploadPart extends Object implements IUploadFile, IPart
{
    private static final Log LOG = LogFactory.getLog(UploadPart.class);

    private FileItem _fileItem;

    public UploadPart(FileItem fileItem)
    {
        if (fileItem == null)
            throw new IllegalArgumentException(
                Tapestry.format("invalid-null-parameter", "fileItem"));

        _fileItem = fileItem;
    }

    public String getContentType()
    {
        return _fileItem.getContentType();
    }

    /**
     *  Leverages {@link File} to convert the full file path and extract
     *  the name.
     * 
     **/
    public String getFileName()
    {
        File file = new File(this.getFilePath());

        return file.getName();
    }

    /**
     *  @since 2.0.4
     * 
     **/

    public String getFilePath()
    {
        return _fileItem.getName();
    }

    public InputStream getStream()
    {
        try
        {
            return _fileItem.getInputStream();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("UploadPart.unable-to-open-content-file", _fileItem.getName()),
                ex);
        }
    }

    /**
     *  Deletes the external content file, if one exists.
     * 
     **/

    public void cleanup()
    {
        _fileItem.delete();
    }

    /**
     * Writes the uploaded content to a file.  This should be invoked at most once
     * (perhaps we should add a check for this).  This will often
     * be a simple file rename.
     * 
     * @since 3.0
     */
    public void write(File file)
    {
        try
        {
            _fileItem.write(file);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("UploadPart.write-failure", file, ex.getMessage()),
                ex);
        }
    }

    /**
     * @since 3.0
     */
    public long getSize()
    {
        return _fileItem.getSize();
    }

    /**
     * @since 3.0
     */
    public boolean isInMemory()
    {
        return _fileItem.isInMemory();
    }

}
