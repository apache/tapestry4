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

package org.apache.tapestry.workbench.upload;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;

/**
 *  Contains a form, including an {@link org.apache.tapestry.form.Upload}
 *  component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Upload extends BasePage
{
    private static final String[] bytesPerLineOptions =
        new String[] { "8", "16", "24", "32", "40", "48" };

    private static final String DEFAULT_BPL = "16";

    private String bytesPerLine = DEFAULT_BPL;
    private boolean showAscii;
    private IUploadFile file;
    private IPropertySelectionModel bplModel;

    public void detach()
    {
        bytesPerLine = DEFAULT_BPL;
        showAscii = false;
        file = null;

        super.detach();
    }

    public void formSubmit(IRequestCycle cycle)
    {
        if (Tapestry.isBlank(file.getFileName()))
        {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");

            delegate.setFormComponent((IFormComponent) getComponent("inputFile"));
            delegate.record("You must specify a file to upload.", ValidationConstraint.REQUIRED);
            return;
        }

        UploadResults results = (UploadResults) cycle.getPage("UploadResults");

        results.activate(file, showAscii, Integer.parseInt(bytesPerLine), cycle);
    }

    public String getBytesPerLine()
    {
        return bytesPerLine;
    }

    public void setBytesPerLine(String bytesPerLine)
    {
        this.bytesPerLine = bytesPerLine;

        fireObservedChange("bytesPerLine", bytesPerLine);
    }

    public boolean getShowAscii()
    {
        return showAscii;
    }

    public void setShowAscii(boolean showAscii)
    {
        this.showAscii = showAscii;

        fireObservedChange("showAscii", showAscii);
    }

    public IPropertySelectionModel getBytesPerLineModel()
    {
        if (bplModel == null)
            bplModel = new StringPropertySelectionModel(bytesPerLineOptions);

        return bplModel;
    }

    public IUploadFile getFile()
    {
        return file;
    }

    public void setFile(IUploadFile file)
    {
        this.file = file;
    }

    public void setMessage(String message)
    {

    }

}