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

package org.apache.tapestry.junit.mock.c19;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

/**
 *  Used in tests of upload and the {@link org.apache.tapestry.form.Upload}
 *  component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class Home extends BasePage
{
    public abstract IUploadFile getUploadFile();
	public abstract void setUploadDisabled(boolean uploadDisabled);

    public void formSubmit(IRequestCycle cycle)
    {
    	IUploadFile file = getUploadFile();
    	
    	if (file == null)
    	{
    		cycle.activate("Three");
    		return;
    	}
    	
        Two page = (Two) cycle.getPage("Two");

        page.setFile(file);
        cycle.activate(page);
    }
    
    public void disableUpload(IRequestCycle cycle)
    {
    	setUploadDisabled(true);
    }
}
