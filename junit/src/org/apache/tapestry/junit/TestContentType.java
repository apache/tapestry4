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
