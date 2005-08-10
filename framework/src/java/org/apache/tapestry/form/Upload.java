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

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.multipart.MultipartDecoder;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Form element used to upload files. [ <a
 * href="../../../../../ComponentReference/Upload.html">Component Reference
 * </a>]
 * <p>
 * As of 4.0, this component can be validated.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 * @since 1.0.8
 */

public abstract class Upload extends AbstractFormComponent implements ValidatableField
{
    public abstract void setFile(IUploadFile file);

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
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

        renderIdAttribute(writer, cycle);

        renderDelegateAttributes(writer, cycle);

        getValidatableFieldSupport().renderContributions(this, writer, cycle);

        renderInformalParameters(writer, cycle);

        writer.closeTag();

        renderDelegateSuffix(writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IUploadFile file = getDecoder().getFileUpload(getName());
        
        if (HiveMind.isBlank(file.getFileName()))
        {
            file = null;
        }
        
        setFile(file);
        
        try
        {
            getValidatableFieldSupport().validate(this, writer, cycle, file);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }
    
    /**
     * Injected.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    /**
     * Injected.
     */
    public abstract MultipartDecoder getDecoder();

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
     */
    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }
}