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

package org.apache.tapestry.multipart;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.IUploadFile;

/**
 *  Decodes the data in a <code>multipart/form-data</code> HTTP request, handling
 *  file uploads and multi-valued parameters.  After decoding, the class is used
 *  to access the parameter values.
 * 
 *  <p>This implementation is a thin wrapper around the Apache Jakarta
 *  <a href="http://jakarta.apache.org/commons/fileupload/">FileUpload</a>. 
 *  
 *  <p>Supports single valued parameters, multi-valued parameters and individual
 *  file uploads.  That is, for file uploads, each upload must be a unique parameter
 *  (that is all the {@link org.apache.tapestry.form.Upload} component needs).

 *
 *  @author Joe Panico
 *  @version $Id$
 *  @since 2.0.1
 *
 **/
public class DefaultMultipartDecoder implements IMultipartDecoder
{
    /**
     *  Request attribute key used to store the part map for this request.
     *  The part map is created in {@link #decode(HttpServletRequest)}.  By storing
     *  the part map in the request instead of an instance variable, DefaultMultipartDecoder
     *  becomes threadsafe (no client-specific state in instance variables).
     * 
     **/

    public static final String PART_MAP_ATTRIBUTE_NAME = "org.apache.tapestry.multipart.part-map";

    private int _maxSize = 10000000;
    private int _thresholdSize = 1024;
    private String _repositoryPath = System.getProperty("java.io.tmpdir");

    private static DefaultMultipartDecoder _shared;

    public static DefaultMultipartDecoder getSharedInstance()
    {
        if (_shared == null)
            _shared = new DefaultMultipartDecoder();

        return _shared;
    }

    public void setMaxSize(int maxSize)
    {
        _maxSize = maxSize;
    }

    public int getMaxSize()
    {
        return _maxSize;
    }

    public void setThresholdSize(int thresholdSize)
    {
        _thresholdSize = thresholdSize;
    }

    public int getThresholdSize()
    {
        return _thresholdSize;
    }

    public void setRepositoryPath(String repositoryPath)
    {
        _repositoryPath = repositoryPath;
    }

    public String getRepositoryPath()
    {
        return _repositoryPath;
    }

    public static boolean isMultipartRequest(HttpServletRequest request)
    {
        return FileUpload.isMultipartContent(request);
    }

    /**
     *  Invokes {@link IPart#cleanup()} on each part.
     * 
     **/
    public void cleanup(HttpServletRequest request)
    {
        Map partMap = getPartMap(request);

        Iterator i = partMap.values().iterator();
        while (i.hasNext())
        {
            IPart part = (IPart) i.next();
            part.cleanup();
        }
    }

    /**
     * Decodes the request, storing the part map (keyed on query parameter name, 
     * value is {@link IPart} into the request as an attribute.
     * 
     * @throws ApplicationRuntimeException if decode fails, for instance the
     * request exceeds getMaxSize()
     * 
     **/

    public void decode(HttpServletRequest request)
    {
        Map partMap = new HashMap();

        request.setAttribute(PART_MAP_ATTRIBUTE_NAME, partMap);

        // The encoding that will be used to decode the string parameters
        // It should NOT be null at this point, but it may be 
        // if the older Servlet API 2.2 is used
        String encoding = request.getCharacterEncoding();

        // DiskFileUpload is not quite threadsafe, so we create a new instance
        // for each request.

        DiskFileUpload upload = new DiskFileUpload();

        List parts = null;

        try
        {
            if (encoding != null)
                upload.setHeaderEncoding(encoding);
            parts = upload.parseRequest(request, _thresholdSize, _maxSize, _repositoryPath);
        }
        catch (FileUploadException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("DefaultMultipartDecoder.unable-to-decode", ex.getMessage()),
                ex);
        }

        int count = Tapestry.size(parts);

        for (int i = 0; i < count; i++)
        {
            FileItem uploadItem = (FileItem) parts.get(i);

            if (uploadItem.isFormField())
            {
                try
                {
                    String name = uploadItem.getFieldName();
                    String value;
                    if (encoding == null)
                        value = uploadItem.getString();
                    else
                        value = uploadItem.getString(encoding);
                        
                    ValuePart valuePart = (ValuePart) partMap.get(name);
                    if (valuePart != null)
                    {
                        valuePart.add(value);
                    }
                    else
                    {
                        valuePart = new ValuePart(value);
                        partMap.put(name, valuePart);
                    }
                }
                catch (UnsupportedEncodingException ex)
                {
                    throw new ApplicationRuntimeException(
                        Tapestry.format("illegal-encoding", encoding),
                        ex);
                }
            }
            else
            {
                UploadPart uploadPart = new UploadPart(uploadItem);

                partMap.put(uploadItem.getFieldName(), uploadPart);
            }
        }

    }

    public String getString(HttpServletRequest request, String name)
    {
        Map partMap = getPartMap(request);

        ValuePart part = (ValuePart) partMap.get(name);
        if (part != null)
            return part.getValue();

        return null;
    }

    public String[] getStrings(HttpServletRequest request, String name)
    {
        Map partMap = getPartMap(request);

        ValuePart part = (ValuePart) partMap.get(name);
        if (part != null)
            return part.getValues();

        return null;
    }

    public IUploadFile getUploadFile(HttpServletRequest request, String name)
    {
        Map partMap = getPartMap(request);

        return (IUploadFile) partMap.get(name);
    }

    private Map getPartMap(HttpServletRequest request)
    {
        return (Map) request.getAttribute(PART_MAP_ATTRIBUTE_NAME);
    }

}
