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

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.request.IUploadFile;

/**
 *  Defines how a multipart HTTP request can be broken into individual
 *  elements (including file uploads).
 * 
 *  <p>Multipart decoder implementations must be threadsafe.
 *  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 **/

public interface IMultipartDecoder
{
    /**
     *  Decodes the incoming request, identifying all
     *  the parts (values and uploaded files) contained
     *  within.
     * 
     **/

    public void decode(HttpServletRequest request);

    /**
     *  Invoked to release any resources needed by tghe
     *  decoder.  In some cases, large incoming parts
     *  are written to temporary files; this method
     *  ensures those temporary files are deleted.
     * 
     **/

    public void cleanup(HttpServletRequest request);

    /**
     *  Returns the single value (or first value) for the parameter
     *  with the specified name.  Returns null if no such parameter
     *  was in the request.
     * 
     **/

    public String getString(HttpServletRequest request, String name);

    /**
     *  Returns an array of values (possibly a single element array).
     *  Returns null if no such parameter was in the request.
     * 
     **/

    public String[] getStrings(HttpServletRequest request, String name);

    /**
     *  Returns the uploaded file with the specified parameter name,
     *  or null if no such parameter was in the request.
     * 
     **/

    public IUploadFile getUploadFile(HttpServletRequest request, String name);
}
