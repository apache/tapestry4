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

package org.apache.tapestry.workbench.upload;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;

/**
 * Contains a form, including an {@link org.apache.tapestry.form.Upload}component.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Upload extends BasePage
{
    public abstract IUploadFile getFile();

    public abstract boolean isShowAscii();

    public abstract String getBytesPerLine();

    private static final String[] bytesPerLineOptions = new String[]
    { "8", "16", "24", "32", "40", "48" };

    private IPropertySelectionModel bplModel;

    public void formSubmit(IRequestCycle cycle)
    {
        IUploadFile file = getFile();

        if (HiveMind.isBlank(file.getFileName()))
        {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");

            delegate.setFormComponent((IFormComponent) getComponent("inputFile"));
            delegate.record("You must specify a file to upload.", ValidationConstraint.REQUIRED);
            return;
        }

        UploadResults results = (UploadResults) cycle.getPage("UploadResults");

        results.activate(file, isShowAscii(), Integer.parseInt(getBytesPerLine()), cycle);
    }

    public IPropertySelectionModel getBytesPerLineModel()
    {
        if (bplModel == null)
            bplModel = new StringPropertySelectionModel(bytesPerLineOptions);

        return bplModel;
    }
}