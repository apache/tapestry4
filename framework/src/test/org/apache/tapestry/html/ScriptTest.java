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

package org.apache.tapestry.html;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.IScriptProcessor;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for the {@link Script} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ScriptTest extends BaseComponentTestCase
{
    private static class MockScript implements IScript
    {
        Map _symbols;

        public void execute(IRequestCycle cycle, IScriptProcessor processor, Map symbols)
        {
            _symbols = symbols;
        }

        public Resource getScriptResource()
        {
            // TODO Auto-generated method stub
            return null;
        }

    }

    /**
     * No input symbols, no informal parameters.
     */
    public void testMinimalRender()
    {
        IScriptSource source = newScriptSource();
        IScript script = newScript();

        PageRenderSupport support = newPageRenderSupport();
        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();
        Resource scriptLocation = newResource();
        IRender body = newRender();

        IComponent container = newComponent();

        String scriptPath = "MyScript.script";

        Script component = (Script) newInstance(Script.class, new Object[]
        { "specification", new ComponentSpecification(), "container", container, "scriptSource",
                source, "scriptPath", scriptPath });

        trainGetPageRenderSupport(cycle, support);
        
        trainGetScriptLocation(container, scriptPath, scriptLocation);
        
        trainGetScript(source, scriptLocation, script);

        script.execute(cycle, support, new HashMap());

        body.render(writer, cycle);

        trainResponseBuilder(cycle, writer);
        
        replay();

        component.addBody(body);

        component.renderComponent(writer, cycle);

        verify();
    }

    public void testWithSymbolsMap()
    {
        IScriptSource source = newScriptSource();
        MockScript script = new MockScript();

        PageRenderSupport support = newPageRenderSupport();
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);
        Resource scriptLocation = newResource();
        IRender body = newRender();

        IComponent container = newComponent();

        Map baseSymbols = new HashMap();
        baseSymbols.put("fred", "barney");

        String scriptPath = "MyScript.script";

        Script component = (Script) newInstance(Script.class, new Object[]
        { "specification", new ComponentSpecification(), "container", container, "scriptSource",
                source, "scriptPath", scriptPath, "baseSymbols", baseSymbols });

        trainGetPageRenderSupport(cycle, support);

        trainGetScriptLocation(container, scriptPath, scriptLocation);

        trainGetScript(source, scriptLocation, script);

        body.render(writer, cycle);

        trainResponseBuilder(cycle, writer);
        
        replay();

        component.addBody(body);

        component.renderComponent(writer, cycle);

        verify();

        assertEquals(baseSymbols, script._symbols);
        assertSame(script._symbols, component.getSymbols());
        assertNotSame(baseSymbols, script._symbols);
    }

    public void testWithSymbolsMapAndInformalParameters()
    {
        IScriptSource source = newScriptSource();
        MockScript script = new MockScript();

        PageRenderSupport support = newPageRenderSupport();
        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();
        Resource scriptLocation = newResource();
        IRender body = newRender();

        IComponent container = newComponent();

        Map baseSymbols = new HashMap();
        baseSymbols.put("fred", "flintstone");
        baseSymbols.put("flash", "gordon");

        IBinding informal = newBinding("mercury");

        String scriptPath = "MyScript.script";

        Script component = (Script) newInstance(Script.class, new Object[]
        { "specification", new ComponentSpecification(), "container", container, "scriptSource",
                source, "scriptPath", scriptPath, "baseSymbols", baseSymbols });
        component.setBinding("fred", informal);

        trainGetPageRenderSupport(cycle, support);

        trainGetScriptLocation(container, scriptPath, scriptLocation);

        trainGetScript(source, scriptLocation, script);

        body.render(writer, cycle);

        trainResponseBuilder(cycle, writer);
        
        replay();

        component.addBody(body);

        component.renderComponent(writer, cycle);

        verify();

        Map expectedSymbols = new HashMap(baseSymbols);
        expectedSymbols.put("fred", "mercury");

        assertEquals(expectedSymbols, script._symbols);
        assertSame(script._symbols, component.getSymbols());
    }

    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(true, false);
        IRender body = newRender();

        body.render(writer, cycle);

        trainResponseBuilder(cycle, writer);
        
        replay();

        Script component = (Script) newInstance(Script.class);

        component.addBody(body);

        component.renderComponent(writer, cycle);

        verify();
    }

    public void testMultiParamException() 
    {
    	IScriptSource source = newScriptSource();
        
        PageRenderSupport support = newPageRenderSupport();
        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();
        IRender body = newRender();
        
        IComponent container = newComponent();

        String scriptPath = "MyScript.script";
        
        IAsset scriptAsset = newAsset();
        
        Script component = (Script) newInstance(Script.class, new Object[]
        { "specification", new ComponentSpecification(), "container", container, "scriptSource",
                source, "scriptPath", scriptPath, "scriptAsset", scriptAsset });
        
        trainGetPageRenderSupport(cycle, support);
        
        replay();
        
        component.addBody(body);
        
        try {
        	component.renderComponent(writer, cycle);
        } catch (ApplicationRuntimeException ex) {
        	assertExceptionSubstring(ex, "Script component has both script IAsset");
        }
        
        verify();
    }
    
    public void testIAssetParamRender()
    {
        IScriptSource source = newScriptSource();
        IScript script = newScript();
        
        PageRenderSupport support = newPageRenderSupport();
        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();
        Resource scriptLocation = newResource();
        IRender body = newRender();
        
        IComponent container = newComponent();
        
        IAsset scriptAsset = newAsset();
        
        expect(scriptAsset.getResourceLocation()).andReturn(scriptLocation);
        
        Script component = newInstance(Script.class, new Object[]
        { "specification", new ComponentSpecification(), "container", container, "scriptSource",
                source, "scriptAsset", scriptAsset });
        
        trainGetPageRenderSupport(cycle, support);
        
        trainGetScript(source, scriptLocation, script);
        
        script.execute(cycle, support, new HashMap());
        
        body.render(writer, cycle);
        
        trainResponseBuilder(cycle, writer);
        
        replay();
        
        component.addBody(body);
        
        component.renderComponent(writer, cycle);
        
        verify();
    }
    
    protected IScript newScript()
    {
        return (IScript) newMock(IScript.class);
    }

    protected void trainGetScript(IScriptSource source, Resource scriptLocation, IScript script)
    {
        expect(source.getScript(scriptLocation)).andReturn(script);
    }

    protected IScriptSource newScriptSource()
    {
        return (IScriptSource) newMock(IScriptSource.class);
    }

    protected void trainGetScriptLocation(IComponent component, String scriptPath,
            Resource scriptLocation)
    {
        IComponentSpecification spec = newSpec();
        Resource resource = newResource();

        expect(component.getSpecification()).andReturn(spec);
        
        expect(spec.getSpecificationLocation()).andReturn(resource);

        expect(resource.getRelativeResource(scriptPath)).andReturn(scriptLocation);
    }
}
