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

package org.apache.tapestry.workbench.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.tapestry.ApplicationRuntimeException;
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
