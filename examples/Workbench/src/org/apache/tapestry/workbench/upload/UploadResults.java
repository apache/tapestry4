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

package org.apache.tapestry.workbench.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.util.io.BinaryDumpOutputStream;

/**
 *  Displays the uploaded file as a hexadecimal dump.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class UploadResults extends BasePage
{
    private IUploadFile file;
    private String fileDump;

    public void detach()
    {
        file = null;
        fileDump = null;

        super.detach();
    }

    public IUploadFile getFile()
    {
        return file;
    }

    public String getFileDump()
    {
        return fileDump;
    }

    public void activate(
        IUploadFile file,
        boolean showAscii,
        int bytesPerLine,
        IRequestCycle cycle)
    {
        this.file = file;

        StringWriter writer = null;
        BinaryDumpOutputStream out = null;
        InputStream in = null;

        try
        {
            in = file.getStream();

            writer = new StringWriter();
            out = new BinaryDumpOutputStream(writer);

            out.setShowAscii(showAscii);
            out.setBytesPerLine(bytesPerLine);

            byte[] buffer = new byte[1000];

            while (true)
            {
                int length = in.read(buffer);

                if (length < 0)
                    break;

                out.write(buffer, 0, length);
            }

            in.close();
            in = null;

            out.close();
            out = null;

            fileDump = writer.getBuffer().toString();

            writer.close();
            writer = null;
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException("Unable to display file.", this, null, ex);
        }
        finally
        {
            close(in);
            close(out);
            close(writer);
        }

        cycle.activate(this);
    }

    private void close(InputStream stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            {
            }
        }
    }

    private void close(OutputStream stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            {
            }
        }
    }

    private void close(Writer writer)
    {
        if (writer != null)
        {
            try
            {
                writer.close();
            }
            catch (IOException ex)
            {
            }
        }
    }
}
