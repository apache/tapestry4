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

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.request.IUploadFile;

/**
 * Responsible for detecting and processing file upload requests, using Jakarta Commons FileUpload.
 * Implementations of this service typically use the threaded service lifecycle model.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface MultipartDecoder
{
    /**
     * Decodes the request, returning a new {@link javax.servlet.http.HttpServletRequest}
     * implementation that will allow access to the form fields submitted in the request (but omits
     * uploaded files.
     */

    public HttpServletRequest decode(HttpServletRequest request);

    /**
     * Gets a file upload with the given name, or returns null if no such file upload was in the
     * request.
     */

    public IUploadFile getFileUpload(String parameterName);

    /**
     * Cleans up any temporary resources created during the request processing. This typically
     * includes temporary files used to contain uploaded file content.
     */

    public void cleanup();
}