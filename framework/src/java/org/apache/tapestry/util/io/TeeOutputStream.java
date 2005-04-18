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

package org.apache.tapestry.util.io;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.hivemind.util.Defense;

/**
 * An output stream that copies bytes pushed through it to two other output streams.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TeeOutputStream extends OutputStream
{
    private final OutputStream _os1;

    private final OutputStream _os2;

    public TeeOutputStream(OutputStream os1, OutputStream os2)
    {
        Defense.notNull(os1, "os1");
        Defense.notNull(os2, "os2");

        _os1 = os1;
        _os2 = os2;
    }

    public void close() throws IOException
    {
        _os1.close();
        _os2.close();
    }

    public void flush() throws IOException
    {
        _os1.flush();
        _os2.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        _os1.write(b, off, len);
        _os2.write(b, off, len);
    }

    public void write(byte[] b) throws IOException
    {
        _os1.write(b);
        _os1.write(b);
    }

    public void write(int b) throws IOException
    {
        _os1.write(b);
        _os2.write(b);
    }
}