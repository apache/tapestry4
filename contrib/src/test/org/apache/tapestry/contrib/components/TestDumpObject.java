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

package org.apache.tapestry.contrib.components;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.contrib.components.DumpObject}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestDumpObject extends HiveMindTestCase
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

    public void testNotSerializable()
    {
        Creator creator = new Creator();
        Object object = new Object();

        DumpObject dumpObject = (DumpObject) creator.newInstance(DumpObject.class);

        assertEquals("java.io.NotSerializableException: java.lang.Object", dumpObject
                .convert(object));
    }

    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(true);

        Creator creator = new Creator();
        DumpObject dumpObject = (DumpObject) creator.newInstance(DumpObject.class);

        replayControls();

        dumpObject.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testNormal() throws Exception
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);

        Creator creator = new Creator();
        DumpObject dumpObject = (DumpObject) creator.newInstance(DumpObject.class, new Object[]
        { "object", "a serialized string" });

        String expected = contentsOf("Normal.txt");

        writer.print(expected);

        replayControls();

        dumpObject.renderComponent(writer, cycle);

        verifyControls();
    }

    private IRequestCycle newCycle(boolean isRewinding)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(isRewinding);

        return cycle;
    }

    private IMarkupWriter newWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }
}
