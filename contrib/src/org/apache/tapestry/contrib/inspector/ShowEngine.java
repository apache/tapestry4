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

package org.apache.tapestry.contrib.inspector;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.util.io.BinaryDumpOutputStream;

/**
 *  Component of the {@link Inspector} page used to display
 *  the properties of the {@link org.apache.tapestry.IEngine} as well as a serialized view of it.
 *  Also, the {@link org.apache.tapestry.request.RequestContext} is dumped out.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowEngine extends BaseComponent implements PageDetachListener
{
    private byte[] serializedEngine;

    public void pageDetached(PageEvent event)
    {
        serializedEngine = null;
    }

    /**
     *  Workaround for OGNL limitation --- OGNL can't dereference
     *  past class instances.
     * 
     *  @since 2.2
     * 
     **/

    public String getEngineClassName()
    {
        return getPage().getEngine().getClass().getName();
    }

    private byte[] getSerializedEngine()
    {
        if (serializedEngine == null)
            buildSerializedEngine();

        return serializedEngine;
    }

    private void buildSerializedEngine()
    {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            // Write the application object to the stream.

            oos.writeObject(getPage().getEngine());

            // Extract the application as an array of bytes.

            serializedEngine = bos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("ShowEngine.could-not-serialize"),
                ex);
        }
        finally
        {
            close(oos);
            close(bos);
        }

        // It would be nice to deserialize the application object now, but in
        // practice, that fails due to class loader problems.
    }

    private void close(OutputStream stream)
    {
        if (stream == null)
            return;

        try
        {
            stream.close();
        }
        catch (IOException ex)
        {
            // Ignore.
        }
    }

    public int getEngineByteCount()
    {
        return getSerializedEngine().length;
    }

    public IRender getEngineDumpDelegate()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                dumpSerializedEngine(writer);
            }
        };
    }

    private void dumpSerializedEngine(IMarkupWriter responseWriter)
    {
        CharArrayWriter writer = null;
        BinaryDumpOutputStream bos = null;

        try
        {
            // Because IReponseWriter doesn't implement the
            // java.io.Writer interface, we have to buffer this
            // stuff then pack it in all at once.  Kind of a waste!

            writer = new CharArrayWriter();

            bos = new BinaryDumpOutputStream(writer);
            bos.setBytesPerLine(32);

            bos.write(getSerializedEngine());
            bos.close();

            responseWriter.print(writer.toString());
        }
        catch (IOException ex)
        {
            // Ignore.
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException ex)
                {
                    // Ignore.
                }
            }

            if (writer != null)
            {
                writer.reset();
                writer.close();
            }
        }
    }

}