// Copyright 2005 The Apache Software Foundation
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

import org.apache.tapestry.request.IUploadFile;

/**
 * Responsible for detecting and processing file upload requests, using Jakarta
 * Commons FileUpload. Implementations of this service typically use the
 * threaded service lifecycle model.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface MultipartDecoder {
	
	/**
	 * Gets a file upload with the given name, or returns null if no such file
	 * upload was in the request.
	 */

	public IUploadFile getFileUpload(String parameterName);

    /**
     * Sets the maximum upload file size on the resulting {@link FileUpload} 
     * object.
     * 
     * @see <a href="http://jakarta.apache.org/commons/fileupload/apidocs/org/apache/commons/fileupload/FileUploadBase.html#setSizeMax(long)">FileUpload</a>
     * @param sizeMax The maximum file size allowed for uploads, default is 10kb
     */
    
    public void setSizeMax(long sizeMax);
    
	/**
	 * Cleans up any temporary resources created during the request processing.
	 * This typically includes temporary files used to contain uploaded file
	 * content.
	 */

	void cleanup();
}
