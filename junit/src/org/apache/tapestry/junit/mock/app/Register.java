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

package org.apache.tapestry.junit.mock.app;

import java.util.ResourceBundle;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.EnumPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 *
 *  Page used to demonstate basic forms.  
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class Register extends BasePage
{
    private IPropertySelectionModel _ageRangeModel;

    public IPropertySelectionModel getAgeRangeModel()
    {
        if (_ageRangeModel == null)
            _ageRangeModel =
                new EnumPropertySelectionModel(
                    AgeRange.getAllValues(),
                    ResourceBundle.getBundle("org.apache.tapestry.junit.mock.app.AgeRangeStrings", getLocale()));

        return _ageRangeModel;
    }

    public void formSubmit(IRequestCycle cycle)
    {
        IValidationDelegate delegate = (IValidationDelegate)getBeans().getBean("delegate");
        
        if (delegate.getHasErrors())
            return;
            
        User user = (User)getBeans().getBean("user");
        
        RegisterConfirm page = (RegisterConfirm)cycle.getPage("RegisterConfirm");
        
        page.setUser(user);
        cycle.activate(page);
    }
}
