/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.multipart;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.request.IUploadFile;

/**
 *  Defines how a multipart HTTP request can be broken into individual
 *  elements (including file uploads).
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
	
    public void cleanup();	
    
    /**
     *  Returns the single value (or first value) for the parameter
     *  with the specified name.  Returns null if no such parameter
     *  was in the request.
     * 
     **/

    public String getString(String name);  
    
    /**
     *  Returns an array of values (possibly a single element array).
     *  Returns null if no such parameter was in the request.
     * 
     **/

    public String[] getStrings(String name);  
    
    /**
     *  Returns the uploaded file with the specified parameter name,
     *  or null if no such parameter was in the request.
     * 
     **/

    public IUploadFile getUploadFile(String name);      
}
