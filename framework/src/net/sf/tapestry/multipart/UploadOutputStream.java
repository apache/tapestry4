/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.multipart;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private static final Log LOG = LogFactory.getLog(UploadOutputStream.class);

    public static final int DEFAULT_LIMIT = 2000;

    private ByteArrayOutputStream _byteArrayStream = new ByteArrayOutputStream();
    private OutputStream _activeStream = _byteArrayStream;
    private int _limit;
    private int _count = 0;
    private OutputStream _fileStream;
    private File _file;

    UploadOutputStream()
    {
        this(DEFAULT_LIMIT);
    }

    UploadOutputStream(int limit)
    {
        _limit = limit;
    }

    public void close() throws IOException
    {
        _activeStream.close();
    }

    public void flush() throws IOException
    {
        _activeStream.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        check(len);

        _activeStream.write(b, off, len);
    }

    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException
    {
        check(1);

        _activeStream.write(b);
    }

    private void check(int length) throws IOException
    {
        if (_fileStream != null)
            return;

        _count += length;

        if (_count < _limit)
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Limit of " + _limit + " bytes reached.");

        _file = File.createTempFile("upload-", ".bin");

        _fileStream = new BufferedOutputStream(new FileOutputStream(_file));

        _byteArrayStream.close();

        _byteArrayStream.writeTo(_fileStream);

        if (LOG.isDebugEnabled())
            LOG.debug("Writing upload content to " + _file + ".");

        _byteArrayStream = null;
        _activeStream = _fileStream;
    }

    public File getContentFile()
    {
        return _file;
    }

    public byte[] getContent()
    {
        return _byteArrayStream.toByteArray();
    }
}