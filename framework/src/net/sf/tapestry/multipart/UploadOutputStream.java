//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.multipart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Category;

/**
  *  An OutputStream that, when a certain limit is reached,
  *  opens a temporary file and writes to that temporary file.
  * 
  *  @author Howard Lewis Ship
  *  @version $Id$
  *  @since 2.0.1
  *
  **/

class UploadOutputStream extends OutputStream
{
    private static final Category CAT =
        Category.getInstance(UploadOutputStream.class);

    public static final int DEFAULT_LIMIT = 2000;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private OutputStream active = baos;
    private int limit;
    private int count = 0;
    private FileOutputStream fos;
    private File file;

    UploadOutputStream()
    {
        this(DEFAULT_LIMIT);
    }

    UploadOutputStream(int limit)
    {
        this.limit = limit;
    }

    public void close() throws IOException
    {
        active.close();
    }

    public void flush() throws IOException
    {
        active.close();
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        check(len);

        active.write(b, off, len);
    }

    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException
    {
        check(1);

        active.write(b);
    }

    private void check(int length) throws IOException
    {
        if (fos != null)
            return;

        count += length;

        if (count < limit)
            return;

        if (CAT.isDebugEnabled())
            CAT.debug("Limit of " + limit + " bytes reached.");

        file = File.createTempFile("upload-", ".bin");

        fos = new FileOutputStream(file);

        baos.close();

        baos.writeTo(fos);

        if (CAT.isDebugEnabled())
            CAT.debug("Writing upload content to " + file + ".");

        baos = null;
        active = fos;
    }

    public File getContentFile()
    {
        return file;
    }

    public byte[] getContent()
    {
        return baos.toByteArray();
    }
}