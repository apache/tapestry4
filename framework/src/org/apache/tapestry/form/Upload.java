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

package org.apache.tapestry.form;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.IUploadFile;

/**
 *  Form element used to upload files.  For the momement, it is necessary to
 *  explicitly set the form's enctype to "multipart/form-data".
 * 
 *  [<a href="../../../../../ComponentReference/Upload.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public abstract class Upload extends AbstractFormComponent
{
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);
		
        String name = form.getElementId(this);

        if (form.isRewinding())
        {
        	if (!isDisabled())
	            setFile(cycle.getRequestContext().getUploadFile(name));

            return;
        }

		// Force the form to use the correct encoding type for
		// file uploads.
		
		form.setEncodingType("multipart/form-data");

        writer.beginEmpty("input");
        writer.attribute("type", "file");
        writer.attribute("name", name);

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        // Size, width, etc. can be specified as informal parameters
        // (Not making the same mistake here that was made with TextField
        // and friends).

        renderInformalParameters(writer, cycle);
    }

    public abstract boolean isDisabled();

    public abstract void setFile(IUploadFile file);
}