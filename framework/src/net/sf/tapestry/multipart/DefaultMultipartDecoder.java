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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.StringSplitter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Decodes the data in a <code>multipart/form-data</code> HTTP request, handling
 *  file uploads and multi-valued parameters.  After decoding, the class is used
 *  to access the parameter values.
 * 
 *  <p>This implementation is partially based on the MultipartRequest from
 *  <a href="http://sf.net/projects/jetty">Jetty</a> (which is LGPL), and
 *  partly from research on the web, including a discussion of the 
 *  <a href="http://www.cis.ohio-state.edu/cgi-bin/rfc/rfc1867.html">RFC</a>.
 *  
 *  <p>Supports single valued parameters, multi-valued parameters and individual
 *  file uploads.  That is, for file uploads, each upload must be a unique parameter
 *  (that is all the {@link net.sf.tapestry.form.Upload} component needs).

 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 *
 **/

public class DefaultMultipartDecoder implements IMultipartDecoder
{
    private static final Log LOG = LogFactory.getLog(DefaultMultipartDecoder.class);

    public static final String MULTIPART_FORM_DATA_CONTENT_TYPE = "multipart/form-data";

    private static final String QUOTE = "\"";

    private Map partMap = new HashMap();

    public static boolean isMultipartRequest(HttpServletRequest request)
    {
        String contentType = request.getContentType();

        if (contentType == null)
            return false;

        return contentType.startsWith(MULTIPART_FORM_DATA_CONTENT_TYPE);
    }

    private void close(InputStream stream)
    {
        try
        {
            if (stream != null)
                stream.close();
        }
        catch (IOException ex)
        {
            // Ignore.
        }
    }

    private static final String BOUNDARY = "boundary=";

    public void decode(HttpServletRequest request)
    {
        if (!isMultipartRequest(request))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "MultipartDecoder.wrong-content-type",
                    request.getContentType()));

        String contentType = request.getContentType();
        int pos = contentType.indexOf(BOUNDARY);

        String boundaryString = "--" + contentType.substring(pos + BOUNDARY.length());
        byte[] boundary = (boundaryString + "--").getBytes();

        LineInput input = null;

        try
        {
            input = new LineInput(request.getInputStream());

            checkForInitialBoundary(input, boundaryString);

            boolean last = false;

            while (!last)
            {
                last = readNextPart(input, boundary);
            }
        }
        catch (IOException ex)
        {
            LOG.error(
                Tapestry.getString("MultipartDecoder.io-exception-reading-input", ex.getMessage()),
                ex);

            // Cleanup any partial upload files.

            cleanup();

            throw new ApplicationRuntimeException(ex);
        }
        finally
        {
            //		close(input);
        }

    }

    private void checkForInitialBoundary(LineInput input, String boundary) throws IOException
    {
        String line = input.readLine();

        if (line != null && line.equals(boundary))
            return;

        throw new ApplicationRuntimeException(
            Tapestry.getString("MultipartDecoder.missing-initial-boundary"));
    }

    private boolean readNextPart(LineInput input, byte[] boundary) throws IOException
    {
        String disposition = null;
        String contentType = null;

        // First read the various headers (before the content)

        while (true)
        {
            String line = input.readLine();

            if (line == null || line.length() == 0)
                break;

            int colonx = line.indexOf(':');

            if (colonx > 0)
            {
                String key = line.substring(0, colonx).toLowerCase();

                if (key.equals("content-disposition"))
                {
                    disposition = line.substring(colonx + 1).trim();
                    continue;
                }

                if (key.equals("content-type"))
                {
                    contentType = line.substring(colonx + 1).trim();
                    continue;
                }
            }

        }

        if (disposition == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("MultipartDecoder.missing-content-disposition"));

        Map dispositionMap = explodeDisposition(disposition);
        String name = (String) dispositionMap.get("name");

        if (Tapestry.isNull(name))
            throw new ApplicationRuntimeException(
                Tapestry.getString("MultipartDecoder.invalid-content-disposition", disposition));

        if (!dispositionMap.containsKey("filename"))
            return readValuePart(input, boundary, name);

        String fileName = (String) dispositionMap.get("filename");

        return readFilePart(input, boundary, name, fileName, contentType);
    }

    private static StringSplitter splitter = new StringSplitter(';');

    private Map explodeDisposition(String disposition)
    {
        Map result = new HashMap();

        String[] elements = splitter.splitToArray(disposition);

        for (int i = 0; i < elements.length; i++)
        {
            String element = elements[i];
            int x = element.indexOf('=');

            if (x < 0)
                continue;

            String key = element.substring(0, x).trim();
            String rawValue = element.substring(x + 1);

            if (!(rawValue.startsWith(QUOTE) && rawValue.endsWith(QUOTE)))
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "MultipartDecoder.invalid-content-disposition",
                        disposition));

            result.put(key, rawValue.substring(1, rawValue.length() - 1));

        }

        return result;
    }

    private boolean readFilePart(
        LineInput input,
        byte[] boundary,
        String name,
        String fileName,
        String contentType)
        throws IOException
    {
        UploadOutputStream uploadStream = new UploadOutputStream();

        boolean last = readIntoStream(input, boundary, uploadStream);

        uploadStream.close();

        File file = uploadStream.getContentFile();

        UploadPart p;

        if (LOG.isDebugEnabled())
            LOG.debug("Read file part '" + name + "'.");

        if (file != null)
            p = new UploadPart(fileName, contentType, file);
        else
            p = new UploadPart(fileName, contentType, uploadStream.getContent());

        partMap.put(name, p);

        return last;
    }

    private boolean readValuePart(LineInput input, byte[] boundary, String name) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        boolean last = readIntoStream(input, boundary, baos);

        baos.close();

        String value = baos.toString();

        if (LOG.isDebugEnabled())
            LOG.debug("Read value part '" + name + "' with value: " + value);

        ValuePart p = (ValuePart) partMap.get(name);

        if (p == null)
        {
            p = new ValuePart(value);
            partMap.put(name, p);
        }
        else
            p.add(value);

        return last;
    }

    private static final int CR = 13;
    private static final int LF = 10;
    private static final int SPECIAL = -2;

    /**
     *  Copies the input stream into the output stream, stopping once the boundary is seen
     *  (the boundary is not copied).  Returns true when the input stream is exhausted,
     *  false otherwise.
     * 
     *  This is an ugly cut and past of ugly code from Jetty.  This really needs to be fixed!
     * 
     **/

    private boolean readIntoStream(LineInput input, byte[] boundary, OutputStream stream)
        throws IOException
    {
        boolean result = false;
        int c = 0;
        boolean cr = false;
        boolean lf = false;
        int _char = SPECIAL;
        boolean more = true;

        while (true)
        {
            int b = 0;

            while (more)
            {
                c = (_char != SPECIAL) ? _char : input.read();

                if (c == -1)
                {
                    more = false;
                    continue;
                }

                _char = SPECIAL;

                // look for CR and/or LF
                if (c == CR || c == LF)
                {
                    if (c == CR)
                        _char = input.read();
                    break;
                }

                // look for boundary
                if (b >= 0 && b < boundary.length && c == boundary[b])
                    b++;
                else
                {
                    // this is not a boundary
                    if (cr)
                        stream.write(CR);
                    if (lf)
                        stream.write(LF);
                    cr = lf = false;

                    if (b > 0)
                        stream.write(boundary, 0, b);
                    b = -1;

                    stream.write(c);
                }
            }

            // check partial boundary
            if ((b > 0 && b < boundary.length - 2) || (b == boundary.length - 1))
            {
                stream.write(boundary, 0, b);
                b = -1;
            }

            // boundary match
            if (b > 0 || c == -1)
            {
                if (b == boundary.length)
                    result = true;

                if (_char == LF)
                    _char = SPECIAL;

                break;
            }

            // handle CR LF
            if (cr)
                stream.write(CR);
            if (lf)
                stream.write(LF);

            cr = (c == CR);
            lf = (c == LF || _char == LF);

            if (_char == LF)
                _char = SPECIAL;
        }

        return result;
    }

    /**
     *  Invokes {@link IPart#cleanup()} on each part.
     * 
     **/

    public void cleanup()
    {
        Iterator i = partMap.values().iterator();
        while (i.hasNext())
        {
            IPart part = (IPart) i.next();
            part.cleanup();
        }
    }

    public String getString(String name)
    {
        ValuePart p = (ValuePart) partMap.get(name);

        if (p == null)
            return null;

        return p.getValue();
    }

    public String[] getStrings(String name)
    {
        ValuePart p = (ValuePart) partMap.get(name);

        if (p == null)
            return null;

        return p.getValues();
    }

    public IUploadFile getUploadFile(String name)
    {
        return (UploadPart) partMap.get(name);
    }
}