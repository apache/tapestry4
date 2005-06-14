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

import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.junit.TapestryTestCase;
import org.easymock.MockControl;

/**
 * Abstract test case for {@link FormComponentContributor}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public abstract class FormComponentContributorTestCase extends TapestryTestCase
{
    protected MockControl _componentControl = MockControl.createControl(IFormComponent.class);
    protected IFormComponent _component = (IFormComponent) _componentControl.getMock();

    protected MockControl _pageControl = MockControl.createControl(IPage.class);
    protected IPage _page = (IPage) _pageControl.getMock();

    protected MockControl _cycleControl = MockControl.createControl(IRequestCycle.class);
    protected IRequestCycle _cycle = (IRequestCycle) _cycleControl.getMock();
    
    protected MockControl _formControl = MockControl.createControl(IForm.class);
    protected IForm _form = (IForm) _formControl.getMock();

    protected MockControl _engineControl = MockControl.createControl(IEngine.class);
    protected IEngine _engine = (IEngine) _engineControl.getMock();
    
    protected MockControl _pageRenderSupportControl = MockControl.createControl(PageRenderSupport.class);
    protected PageRenderSupport _pageRenderSupport = (PageRenderSupport) _pageRenderSupportControl.getMock();
    
    /**
     * @see org.apache.hivemind.test.HiveMindTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        _componentControl.reset();
        _pageControl.reset();
        _cycleControl.reset();
        _formControl.reset();
        _engineControl.reset();
        _pageRenderSupportControl.reset();
        
        super.tearDown();
    }

    protected void replay()
    {
        _componentControl.replay();
        _pageControl.replay();
        _cycleControl.replay();
        _formControl.replay();
        _engineControl.replay();
        _pageRenderSupportControl.replay();
    }
    
    protected void verify()
    {
        _componentControl.verify();
        _pageControl.verify();
        _cycleControl.verify();
        _formControl.verify();
        _engineControl.verify();
        _pageRenderSupportControl.verify();
    }
    
    protected void addScript(String script)
    {
        _cycle.getEngine();
        _cycleControl.setReturnValue(_engine);
        
        _engine.getClassResolver();
        _engineControl.setReturnValue(null);
        
        _cycle.getAttribute("org.apache.tapestry.PageRenderSupport");
        _cycleControl.setReturnValue(_pageRenderSupport);
        
        _pageRenderSupport.addExternalScript(new ClasspathResource(null, script));
        _pageRenderSupportControl.setVoidCallable();
    }
}
