/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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