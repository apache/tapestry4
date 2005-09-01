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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests for {@link org.apache.tapestry.util.io.BinaryDumpOutputStream}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestBinaryDumpOutputStream extends TestCase
{
    /**
     * Reads the content of a file, and forms a string. Converts line-number endings in the file
     * into the correct platform value (this should help the test run properly on both Windows and
     * *nix).
     */
    private String contentsOf(String path) throws Exception
    {
        String sep = System.getProperty("line.separator");

        InputStream is = getClass().getResourceAsStream(path);

        is = new BufferedInputStream(is);

        Reader ir = new InputStreamReader(is);

        ir = new BufferedReader(ir);

        LineNumberReader lnr = new LineNumberReader(ir);

        StringBuffer buffer = new StringBuffer();

        while (true)
        {
            String line = lnr.readLine();

            if (line == null)
                break;

            buffer.append(line);
            buffer.append(sep);
        }

        ir.close();

        return buffer.toString();
    }

    public void testBasic() throws Exception
    {
        CharArrayWriter writer = new CharArrayWriter();

        BinaryDumpOutputStream bdos = new BinaryDumpOutputStream(writer);

        ObjectOutputStream oos = new ObjectOutputStream(bdos);

        oos.writeObject(createOutputObject());

        oos.close();

        assertEquals(contentsOf("Basic.txt"), writer.toString());
    }

    /**
     * Creates and returns the object to be written out to the stream. The tests are dependenent on
     * the serialization of HashMap and String not changing between JDKs. If such a change does
     * occur, we'll need to devise an Externalizable object to write to the stream.
     */
    private Map createOutputObject()
    {
        Map map = new HashMap();
        map.put("alpha", "beta");
        map.put("gabba gabba", "we accept you");
        return map;
    }

    public void testOptions() throws Exception
    {

        CharArrayWriter writer = new CharArrayWriter();

        BinaryDumpOutputStream bdos = new BinaryDumpOutputStream(writer);

        bdos.setAsciiBegin(" { ");
        bdos.setAsciiEnd(" }");
        bdos.setOffsetSeperator(" = ");
        bdos.setSubstituteChar('?');
        bdos.setBytesPerLine(48);
        bdos.setSpacingInterval(8);

        ObjectOutputStream oos = new ObjectOutputStream(bdos);

        oos.writeObject(createOutputObject());

        oos.close();

        assertEquals(contentsOf("Options.txt"), writer.toString());
    }

    public void testNoOffset() throws Exception
    {
        CharArrayWriter writer = new CharArrayWriter();

        BinaryDumpOutputStream bdos = new BinaryDumpOutputStream(writer);
        bdos.setShowOffset(false);

        ObjectOutputStream oos = new ObjectOutputStream(bdos);

        oos.writeObject(createOutputObject());

        oos.close();

        assertEquals(contentsOf("NoOffset.txt"), writer.toString());
    }

    public void testNoAscii() throws Exception
    {
        CharArrayWriter writer = new CharArrayWriter();

        BinaryDumpOutputStream bdos = new BinaryDumpOutputStream(writer);
        bdos.setShowAscii(false);

        ObjectOutputStream oos = new ObjectOutputStream(bdos);

        oos.writeObject(createOutputObject());

        oos.close();

        assertEquals(contentsOf("NoAscii.txt"), writer.toString());
    }
}
