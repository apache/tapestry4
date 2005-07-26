// Copyright 2005 The Apache Software Foundation
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.listener.ListenerMap;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Mock object used by the {@link org.apache.tapestry.form.TestFormSupport}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MockForm implements IForm
{
    private Location _location;

    private IRender _body;

    private List _deferredRunnable = new ArrayList();

    private IValidationDelegate _delegate;

    private boolean _focus = true;

    public MockForm()
    {
        this(null, null);
    }

    public MockForm(Location location)
    {
        this(null, location);
    }

    public MockForm(IValidationDelegate delegate)
    {
        this(delegate, null);
    }

    public MockForm(IValidationDelegate delegate, Location location)
    {
        _delegate = delegate;
        _location = location;
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
        return "myform";
    }

    public boolean isRewinding()
    {
        return false;
    }

    public IValidationDelegate getDelegate()
    {
        return _delegate;
    }

    public void setEncodingType(String encodingType)
    {
    }

    public void addHiddenValue(String name, String value)
    {
    }

    public void addHiddenValue(String name, String id, String value)
    {
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

    public void addBody(IRender element)
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
        return "SomePage/myform";
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

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (_body != null)
            _body.render(writer, cycle);
    }

    public void setBinding(String name, IBinding binding)
    {
    }

    public Map getComponents()
    {
        return null;
    }

    public void finishLoad(IRequestCycle cycle, IPageLoader loader,
            IComponentSpecification specification)
    {
    }

    public Messages getMessages()
    {
        return null;
    }

    public INamespace getNamespace()
    {
        return null;
    }

    public void setNamespace(INamespace namespace)
    {
    }

    public void setProperty(String propertyName, Object value)
    {
    }

    public Object getProperty(String propertyName)
    {
        return null;
    }

    public boolean isRendering()
    {
        return false;
    }

    public void enterActiveState()
    {
    }

    public IBeanProvider getBeans()
    {
        return null;
    }

    public ListenerMap getListeners()
    {
        return null;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public void setLocation(Location arg0)
    {
    }

    public Location getLocation()
    {
        return _location;
    }

    public void setBody(IRender body)
    {
        _body = body;
    }

    public void prerenderField(IMarkupWriter writer, IComponent field, Location location)
    {
    }

    public boolean wasPrerendered(IMarkupWriter writer, IComponent field)
    {
        return false;
    }

    public void addDeferredRunnable(Runnable runnable)
    {
        _deferredRunnable.add(runnable);
    }

    void runDeferred()
    {
        Iterator i = _deferredRunnable.iterator();
        while (i.hasNext())
        {
            Runnable r = (Runnable) i.next();

            r.run();
        }
    }

    public boolean isClientValidationEnabled()
    {
        return false;
    }

    public String getMessage(String key)
    {
        return null;
    }

    public void registerForFocus(IFormComponent field, int priority)
    {
    }

    public boolean getFocus()
    {
        return _focus;
    }

    public void setFocus(boolean focus)
    {
        _focus = focus;
    }

    public IComponent getComponent()
    {
        return this;
    }
}