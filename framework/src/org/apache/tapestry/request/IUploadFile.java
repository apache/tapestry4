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

package org.apache.tapestry.request;

import java.io.File;
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
    
    /**
     * Writes the content of the file to a known location.  This should
     * be invoked at most once.  In a standard
     * implementation based on Jakarta FileUpload, this will often
     * be implemented efficiently as a file rename.
     * 
     * @since 3.0
     */
    
    public void write(File file);
    
    /**
     * Returns true if the uploaded content is in memory.  False generally
     * means the content is stored in a temporary file.
     */
    
    public boolean isInMemory();
    
    /**
     * Returns the size, in bytes, of the uploaded content.
     * 
     * @since 3.0
     **/
    
    public long getSize(); 
}