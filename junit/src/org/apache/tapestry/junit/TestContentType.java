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

package org.apache.tapestry.junit;

import org.apache.tapestry.util.ContentType;

/**
 *  Test the functionality of {@link org.apache.tapestry.util.ContentType}
 *
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestContentType extends TapestryTestCase
{
    public TestContentType(String name)
    {
        super(name);
    }

    public void testParsing1() throws Exception
    {
        ContentType contentType = new ContentType("text/html;charset=utf-8");

        assertEquals(
            "The base type of the ContentType is invalid",
            "text",
            contentType.getBaseType());
            
        assertEquals(
            "The html type of the ContentType is invalid",
            "html",
            contentType.getSubType());

        assertEquals(
            "The mime type of the ContentType is invalid",
            "text/html",
            contentType.getMimeType());
            
        String[] parameterNames = contentType.getParameterNames();
        assertEquals(
            "The number of parameter names of the ContentType is invalid",
            1,
            parameterNames.length);

        assertEquals(
            "The parameter names of the ContentType are invalid",
            "charset",
            parameterNames[0]);

        String charset = contentType.getParameter("charset");
        assertEquals("The charset parameter of the ContentType is invalid", "utf-8", charset);

        String nonexistant = contentType.getParameter("nonexistant");
        assertTrue(
            "ContentType does not return null for a non-existant parameter",
            nonexistant == null);
    }
    
    public void testParsing2() throws Exception
    {
        ContentType contentType = new ContentType("text/html");

        assertEquals(
            "The base type of the ContentType is invalid",
            "text",
            contentType.getBaseType());
            
        assertEquals(
            "The html type of the ContentType is invalid",
            "html",
            contentType.getSubType());

        assertEquals(
            "The mime type of the ContentType is invalid",
            "text/html",
            contentType.getMimeType());
            
        String[] parameterNames = contentType.getParameterNames();
        assertEquals(
            "The number of parameter names of the ContentType is invalid",
            0,
            parameterNames.length);

        String charset = contentType.getParameter("charset");
        assertTrue("The charset parameter of the ContentType is invalid", charset == null);
    }
    
    public void testUnparsing1() throws Exception
    {
        ContentType contentType = new ContentType();

        contentType.setBaseType("text");
        contentType.setSubType("html");
        contentType.setParameter("charset", "utf-8");
        
        assertEquals(
            "ContentType does not generate a valid String representation",
            "text/html;charset=utf-8",
            contentType.unparse());
    }

    public void testUnparsing2() throws Exception
    {
        ContentType contentType = new ContentType();

        contentType.setBaseType("text");
        contentType.setSubType("html");
        
        assertEquals(
            "ContentType does not generate a valid String representation",
            "text/html",
            contentType.unparse());
    }

}
