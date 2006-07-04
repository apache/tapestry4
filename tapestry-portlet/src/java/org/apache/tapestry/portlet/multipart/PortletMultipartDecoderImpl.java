// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry.portlet.multipart;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.multipart.AbstractMultipartDecoder;
import org.apache.tapestry.multipart.MultipartMessages;

/**
 * @author Raphael Jean
 */
public class PortletMultipartDecoderImpl extends AbstractMultipartDecoder
        implements PortletMultipartDecoder
{

    public ActionRequest decode(ActionRequest request)
    {
        _encoding = request.getCharacterEncoding();

        PortletFileUpload upload = createFileUpload();

        try
        {
            List fileItems = upload.parseRequest(request);

            processFileItems(fileItems);
        }
        catch (FileUploadException ex)
        {
            throw new ApplicationRuntimeException(MultipartMessages
                    .unableToDecode(ex), ex);
        }

        Map parameterMap = buildParameterMap();

        return new UploadFormPortletParametersWrapper(request, parameterMap);
    }

    private PortletFileUpload createFileUpload()
    {
        FileItemFactory factory = new DiskFileItemFactory(_thresholdSize,
                new File(_repositoryPath));
        PortletFileUpload upload = new PortletFileUpload(factory);

        if (_encoding != null) upload.setHeaderEncoding(_encoding);

        return upload;
    }

}
