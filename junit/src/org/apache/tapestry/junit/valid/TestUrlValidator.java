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

package org.apache.tapestry.junit.valid;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.valid.UrlValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests for {@link org.apache.tapestry.valid.EmailValidator}.
 *
 * @author Tsvetelin Saykov
 * @author Jimmy Dyson
 * @version $Id$
 * @since 3.0
 *
 **/

public class TestUrlValidator extends TapestryTestCase
{
	private UrlValidator v = new UrlValidator();
	
	/**
	 * @param name
	 */
	public TestUrlValidator(String name) {
		super(name);
	}

    public void testValidUrl() throws ValidatorException
    {
        Object result = v.toObject(new TestingField("url"), "http://www.google.com");
        assertEquals("http://www.google.com", result);
    }

    public void testInvalidUrl()
    {
        try
        {
            v.toObject(new TestingField("url"), "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.URL_FORMAT, ex.getConstraint());
            assertEquals(
                "Invalid URL.",
                ex.getMessage());
        }
    }

    public void testOverrideInvalidUrlFormatMessage()
    {
        v.setInvalidUrlFormatMessage(
            "Try a valid URL (for {0}), like \"http://www.google.com\"");

        try
        {
            v.toObject(new TestingField("url"), "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(
                "Try a valid URL (for url), like \"http://www.google.com\"",
                ex.getMessage());
        }
    }

    public void testTooShort()
    {
        v.setMinimumLength(20);

        try
        {
            v.toObject(new TestingField("short"), "http://www.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
            assertEquals("You must enter at least 20 characters for short.", ex.getMessage());
        }
    }

    public void testOverrideMinimumLengthMessage()
    {
        v.setMinimumLength(20);
        v.setMinimumLengthMessage("URLs must be at least 20 characters.");

        try
        {
            v.toObject(new TestingField("short"), "http://www.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("URLs must be at least 20 characters.", ex.getMessage());
        }
    }
    
    public void testDisallowedProtocol()
    {
    		v.setAllowedProtocols("http,https");
    		
    		try
    		{
    				v.toObject(new TestingField("short"), "ftp://ftp.test.com");
            unreachable();
    		}
    		catch (ValidatorException ex) {
    				assertEquals(ValidationConstraint.DISALLOWED_PROTOCOL, ex.getConstraint());
    				assertEquals("Disallowed protocol - protocol must be http or https.", ex.getMessage());
    		}
    }
}