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

package org.apache.tapestry.junit.mock.c20;

import java.util.ResourceBundle;

import org.apache.commons.lang.enum.Enum;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.EnumPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.html.InsertTextMode;

/**
 *  Page used to test {@link org.apache.tapestry.form.TextArea}
 *  and {@link org.apache.tapestry.html.InsertText}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class Home extends BasePage
{
    public abstract String getText();
    public abstract InsertTextMode getMode();
    public abstract void setTextAreaDisabled(boolean disableTextArea);

    public IPropertySelectionModel getModeModel()
    {
        ResourceBundle bundle =
            ResourceBundle.getBundle(
                getClass().getPackage().getName() + ".ModeStrings",
                getLocale());

        return new EnumPropertySelectionModel(
            new Enum[] { InsertTextMode.BREAK, InsertTextMode.PARAGRAPH },
            bundle,
            "mode");
    }
    
    public void formSubmit(IRequestCycle cycle)
    {
    	Two page = (Two)cycle.getPage("Two");
    	
    	page.setMode(getMode());
    	page.setText(getText());
    	
    	cycle.activate(page);
    }
    
    public void disableTextArea(IRequestCycle cycle)
    {
    	setTextAreaDisabled(true);
    }
    	
}
