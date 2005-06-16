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

package org.apache.tapestry.form;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.multipart.MultipartDecoder;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Form element used to upload files.
 * [ <a href="../../../../../ComponentReference/Upload.html">Component Reference </a>]
 * 
 * As of 4.0, the Upload field can indicate that it is required.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 * @since 1.0.8
 */

public abstract class Upload extends AbstractRequirableField
{
    /**
     * @see org.apache.tapestry.form.validator.AbstractRequirableField#bind(org.apache.tapestry.IRequestCycle)
     */
    public void bind(IMarkupWriter writer, IRequestCycle cycle) throws ValidatorException
    {
        setFile(getUploadFile());
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#finishLoad()
     */
    protected void finishLoad()
    {
        setRequiredMessage(ValidationStrings.getMessagePattern(ValidationStrings.REQUIRED_FILE_FIELD, getPage().getLocale()));
    }

    private IUploadFile getUploadFile()
    {
        return getDecoder().getFileUpload(getName());
    }
    
    public String getSubmittedValue(IRequestCycle cycle)
    {
        return getUploadFile().getFilePath();
    }

    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderFormComponent(writer, cycle);
        
        // Force the form to use the correct encoding type for file uploads.
        IForm form = getForm();
        
        form.setEncodingType("multipart/form-data");

        renderDelegatePrefix(writer, cycle);
        
        writer.beginEmpty("input");
        writer.attribute("type", "file");
        writer.attribute("name", getName());

        if (isDisabled())
        {
            writer.attribute("disabled", "disabled");
        }

        form.getDelegate().writeAttributes(writer, cycle, this, null);
        
        renderDelegateAttributes(writer, cycle);
        
        renderDelegateSuffix(writer, cycle);
    }

    public abstract void setFile(IUploadFile file);

    public abstract MultipartDecoder getDecoder();
}