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