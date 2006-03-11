// Copyright 2005 The Apache Software Foundation
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

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.hivemind.ApplicationRuntimeException;

/**
 * Implementation of {@link org.apache.tapestry.multipart.MultipartDecoder}that is based on <a
 * href="http://jakarta.apache.org/commons/fileupload/">Jakarta FileUpload </a>.
 * 
 * @author Howard M. Lewis Ship
 * @author Joe Panico
 * @since 4.0
 */
public class MultipartDecoderImpl extends AbstractMultipartDecoder implements ServletMultipartDecoder
{
    public HttpServletRequest decode(HttpServletRequest request)
    {
        _encoding = request.getCharacterEncoding();

        ServletFileUpload upload = createFileUpload();

        try
        {
            List fileItems = upload.parseRequest(request);

            processFileItems(fileItems);
        }
        catch (FileUploadException ex)
        {
            throw new ApplicationRuntimeException(MultipartMessages.unableToDecode(ex), ex);
        }

        Map parameterMap = buildParameterMap();

        return new UploadFormParametersWrapper(request, parameterMap);
    }

	private ServletFileUpload createFileUpload() {
    	FileItemFactory factory = new DiskFileItemFactory(_thresholdSize, new File(_repositoryPath));
    	ServletFileUpload upload = new ServletFileUpload(factory);

        if (_encoding != null)
            upload.setHeaderEncoding(_encoding);

        return upload;
	}

}