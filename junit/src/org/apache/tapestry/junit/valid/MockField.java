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

package org.apache.tapestry.junit.valid;

import java.util.Locale;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;


/**
 *  Used as a stand-in for a real component when testing the 
 *  {@link org.apache.tapestry.valid.IValidator}
 *  implementations.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class MockField extends AbstractFormComponent implements IFormComponent
{
	private String _displayName;
    private IForm _form;

    public MockField(String displayName)
    {
        this(displayName, new MockForm());
    }
    

	public MockField(String displayName, IForm form)
	{
		_displayName = displayName;
        _form = form;
        
        IPage page = new BasePage();
        page.setLocale(Locale.ENGLISH);
        page.addComponent(this);
        
        setPage(page);
	}
	
	public void setForm(IForm form)
	{
		_form = form;
	}

	public String getDisplayName()
	{
		return _displayName;
	}

	public String getName()
	{
		return _displayName;
	}


    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public IForm getForm()
    {
        return _form;
    }

    public boolean isDisabled()
    {
        return false;
    }

	public String toString()
	{
		return "MockField[" + _displayName + "]";
	}
	
    public void setName(String name)
    {
    }

}