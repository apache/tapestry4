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
 *  the properties of the {@link IEngine} as well as a serialized view of it.
 *  Also, the {@link org.apache.tapestry.RequestContext} is dumped out.
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
                Tapestry.getString("ShowEngine.could-not-serialize"),
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