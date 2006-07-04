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

import java.io.BufferedOutputStream;
import java.io.CharArrayWriter;
import java.io.ObjectOutputStream;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.util.io.BinaryDumpOutputStream;

/**
 * Used to dump out an object's serialized representation in a mix of hex and ascii. The output is
 * formatted for a fixed width font, typically should be enclosed in &lt;pre&gt; tags.
 * 
 * @see org.apache.tapestry.util.io.BinaryDumpOutputStream
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public abstract class DumpObject extends AbstractComponent
{
    // Parameters:

    public abstract Object getObject();

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        String asText = convert(getObject());

        writer.print(asText);
    }

    String convert(Object object)
    {
        try
        {
            CharArrayWriter writer = new CharArrayWriter();
            BinaryDumpOutputStream bdos = new BinaryDumpOutputStream(writer);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bdos));

            oos.writeObject(object);

            oos.close();

            return writer.toString();
        }
        catch (Exception ex)
        {
            return ex.toString();
        }
    }
}
