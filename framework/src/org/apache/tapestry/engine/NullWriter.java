/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.engine;

import org.apache.tapestry.IMarkupWriter;

/**
 *  A {@link IMarkupWriter} that does absolutely <em>nothing</em>; this
 *  is used during the rewind phase of the request cycle when output
 *  is discarded anyway.
 *
 *  @author Howard Lewis Ship, David Solis
 *  @version $Id$
 *  @since 0.2.9
 *
 **/

public class NullWriter implements IMarkupWriter
{
    private static IMarkupWriter shared;

    public static IMarkupWriter getSharedInstance()
    {
        if (shared == null)
            shared = new NullWriter();

        return shared;
    }

    public void printRaw(char[] buffer, int offset, int length)
    {
    }

    public void printRaw(String value)
    {
    }

    public void println()
    {
    }

    public void print(char[] data, int offset, int length)
    {
    }

    public void print(char value)
    {
    }

    public void print(int value)
    {
    }

    public void print(String value)
    {
    }

    /**
     *  Returns <code>this</code>: since a NullWriter doesn't actually
     *  do anything, one is as good as another!.
     *
     **/

    public IMarkupWriter getNestedWriter()
    {
        return this;
    }

    public String getContentType()
    {
        return null;
    }

    public void flush()
    {
    }

    public void end()
    {
    }

    public void end(String name)
    {
    }

    public void comment(String value)
    {
    }

    public void closeTag()
    {
    }

    public void close()
    {
    }

    /**
     *  Always returns false.
     *
     **/

    public boolean checkError()
    {
        return false;
    }

    public void beginEmpty(String name)
    {
    }

    public void begin(String name)
    {
    }

    public void attribute(String name, int value)
    {
    }

    public void attribute(String name, String value)
    {
    }

    /**
     *  @see org.apache.tapestry.IMarkupWriter#attribute(java.lang.String, boolean)
     *
     *  @since 3.0
     **/

    public void attribute(String name, boolean value)
    {
    }

    /**
     *  @see org.apache.tapestry.IMarkupWriter#attributeRaw(java.lang.String, java.lang.String)
     *
     *  @since 3.0
     **/

    public void attributeRaw(String name, String value)
    {
    }
}