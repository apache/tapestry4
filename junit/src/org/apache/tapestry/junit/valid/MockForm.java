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

import java.util.Collection;
import java.util.Map;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMessages;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.spec.BaseLocatable;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 *  Simulates an {@link IForm} for the test suite.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.1
 *
 **/

public class MockForm extends BaseLocatable implements IForm
{
    private String _name;

    public MockForm()
    {
        this("DefaultFormName");
    }

    public MockForm(String name)
    {
        _name = name;
    }

    public void rewind(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public void addEventHandler(FormEventType type, String functionName)
    {
    }

    public String getElementId(IFormComponent component)
    {
        return null;
    }

    public String getElementId(IFormComponent component, String baseId)
    {
        return null;
    }

    public String getName()
    {
        return _name;
    }

    public boolean isRewinding()
    {
        return false;
    }

    public IValidationDelegate getDelegate()
    {
        return null;
    }

    public boolean getRequiresSession()
    {
        return false;
    }

    public void addAsset(String name, IAsset asset)
    {
    }

    public void addComponent(IComponent component)
    {
    }

    public void addWrapped(IRender element)
    {
    }

    public Map getAssets()
    {
        return null;
    }

    public IAsset getAsset(String name)
    {
        return null;
    }

    public IBinding getBinding(String name)
    {
        return null;
    }

    public Collection getBindingNames()
    {
        return null;
    }

    public Map getBindings()
    {
        return null;
    }

    public IComponent getComponent(String id)
    {
        return null;
    }

    public IComponent getContainer()
    {
        return null;
    }

    public void setContainer(IComponent value)
    {
    }

    public String getExtendedId()
    {
        return null;
    }

    public String getId()
    {
        return null;
    }

    public void setId(String value)
    {
    }

    public String getIdPath()
    {
        return null;
    }

    public IPage getPage()
    {
        return null;
    }

    public void setPage(IPage value)
    {
    }

    public IComponentSpecification getSpecification()
    {
        return null;
    }

    public void setSpecification(IComponentSpecification value)
    {
    }

    public void renderWrapped(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public void setBinding(String name, IBinding binding)
    {
    }

    public Map getComponents()
    {
        return null;
    }

    public void finishLoad(
        IRequestCycle cycle,
        IPageLoader loader,
        IComponentSpecification specification)
    {
    }

    public String getString(String key)
    {
        return null;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public INamespace getNamespace()
    {
        return null;
    }

    public void setNamespace(INamespace namespace)
    {
    }

    public void addBody(IRender element)
    {
    }

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public IMessages getMessages()
    {
        return null;
    }

    public void setEncodingType(String encodingType)
    {
    }

    /** @since 3.0 */
    public void addHiddenValue(String name, String value)
    {

    }
    
    /**
	 * @see org.apache.tapestry.IForm#addHiddenValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addHiddenValue(String name, String id, String value) {

	}


    public String getMessage(String key)
    {
        return null;
    }

	public void setProperty(String propertyName, Object value)
	{		
	}

	public Object getProperty(String propertyName)
	{
		return null;
	}

}
