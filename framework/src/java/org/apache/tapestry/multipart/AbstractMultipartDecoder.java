// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.commons.fileupload.FileItem;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.request.IUploadFile;

/**
 * @author Raphael Jean
 */
public abstract class AbstractMultipartDecoder
{

    protected int _maxSize = 10000000;

    protected int _thresholdSize = 1024;

    protected String _repositoryPath = System.getProperty("java.io.tmpdir");

    protected String _encoding;
    
    /**
     * Map of UploadPart (which implements IUploadFile), keyed on parameter
     * name.
     */
    protected Map _uploadParts = new HashMap();

    /**
     * Map of ValuePart, keyed on parameter name.
     */
    private Map _valueParts = new HashMap();

    public IUploadFile getFileUpload(String parameterName)
    {
        return (IUploadFile) _uploadParts.get(parameterName);
    }

    public void cleanup()
    {
        Iterator i = _uploadParts.values().iterator();

        while(i.hasNext())
        {
            UploadPart part = (UploadPart) i.next();

            part.cleanup();
        }
    }

    protected Map buildParameterMap()
    {
        Map result = new HashMap();

        Iterator i = _valueParts.entrySet().iterator();
        while(i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String name = (String) e.getKey();
            ValuePart part = (ValuePart) e.getValue();

            result.put(name, part.getValues());
        }

        return result;
    }

    protected void processFileItems(List parts)
    {
        if (parts == null) return;

        Iterator i = parts.iterator();

        while(i.hasNext())
        {
            FileItem item = (FileItem) i.next();

            processFileItem(item);
        }
    }

    private void processFileItem(FileItem item)
    {
        if (item.isFormField())
        {
            processFormFieldItem(item);
            return;
        }

        processUploadFileItem(item);
    }

    private void processUploadFileItem(FileItem item)
    {
        String name = item.getFieldName();

        UploadPart part = new UploadPart(item);

        _uploadParts.put(name, part);
    }

    void processFormFieldItem(FileItem item)
    {
        String name = item.getFieldName();

        String value = extractFileItemValue(item);

        ValuePart part = (ValuePart) _valueParts.get(name);

        if (part == null)
            _valueParts.put(name, new ValuePart(value));
        else part.add(value);
    }

    private String extractFileItemValue(FileItem item)
    {
        try
        {
            return (_encoding == null) ? item.getString() : item
                    .getString(_encoding);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new ApplicationRuntimeException(MultipartMessages
                    .unsupportedEncoding(_encoding, ex), ex);
        }
    }
}
