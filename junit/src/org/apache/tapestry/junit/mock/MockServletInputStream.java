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

package org.apache.tapestry.junit.mock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 *  Implementation of {@link ServletInputStream} used in mock object testing.
 *  The data in the stream is provided by a binary file.  The implemenation
 *  wraps around a {@link java.io.FileInputStream} redirecting all method
 *  invocations to the inner stream.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class MockServletInputStream extends ServletInputStream
{
    private InputStream _inner;

    public MockServletInputStream(String path) throws IOException
    {
        _inner = new FileInputStream(path);
    }

    public int read() throws IOException
    {
        return _inner.read();
    }

    public int available() throws IOException
    {
        return _inner.available();
    }

    public void close() throws IOException
    {
        _inner.close();
    }

    public synchronized void mark(int readlimit)
    {
        _inner.mark(readlimit);
    }

    public boolean markSupported()
    {
        return _inner.markSupported();
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
        return _inner.read(b, off, len);
    }

    public int read(byte[] b) throws IOException
    {
        return _inner.read(b);
    }

    public synchronized void reset() throws IOException
    {
        _inner.reset();
    }

    public long skip(long n) throws IOException
    {
        return _inner.skip(n);
    }

}
