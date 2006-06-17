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

package org.apache.tapestry;

import static org.easymock.EasyMock.*;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.apache.tapestry.markup.JSONWriterImpl;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.impl.DefaultResponseBuilder;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Base class for testing components, or testing classes that operate on components. Simplifies
 * creating much of the infrastructure around the components.
 * <p>
 * This class may eventually be part of the Tapestry distribution.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class BaseComponentTestCase extends HiveMindTestCase
{
    private Creator _creator;

    protected Creator getCreator()
    {
        if (_creator == null)
            _creator = new Creator();

        return _creator;
    }

    protected CharArrayWriter _charArrayWriter;

    protected IMarkupWriter newBufferWriter()
    {
        _charArrayWriter = new CharArrayWriter();
        PrintWriter pw = new PrintWriter(_charArrayWriter);

        return new MarkupWriterImpl("text/html", pw, new AsciiMarkupFilter());
    }
    
    protected IJSONWriter newBufferJSONWriter()
    {
        _charArrayWriter = new CharArrayWriter();
        PrintWriter pw = new PrintWriter(_charArrayWriter);
        
        return new JSONWriterImpl(pw);
    }
    
    protected void assertBuffer(String expected)
    {
        String actual = _charArrayWriter.toString();

        assertEquals("Buffered markup writer content.", expected, actual);

        _charArrayWriter.reset();
    }

    protected Object newInstance(Class componentClass)
    {
        return newInstance(componentClass, null);
    }

    protected Object newInstance(Class componentClass, String propertyName, Object propertyValue)
    {
        return getCreator().newInstance(componentClass, new Object[]
        { propertyName, propertyValue });
    }

    protected Object newInstance(Class componentClass, Object[] properties)
    {
        return getCreator().newInstance(componentClass, properties);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle)newMock(IRequestCycle.class);
    }

    protected IRequestCycle newCycle(IMarkupWriter writer)
    {
        IRequestCycle cycle = (IRequestCycle) newMock(IRequestCycle.class);
        
        trainResponseBuilder(cycle, writer);
        
        return cycle;
    }
    
    protected IRequestCycle newCycle(boolean rewinding)
    {
        return newCycle(rewinding, null);
    }
    
    protected IRequestCycle newCycle(boolean rewinding, boolean trainWriter)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        trainIsRewinding(cycle, rewinding);
        
        if (trainWriter)
            trainResponseBuilder(cycle, null);
        
        return cycle;
    }
    
    protected IRequestCycle newCycle(boolean rewinding, IMarkupWriter writer)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        trainIsRewinding(cycle, rewinding);

        trainResponseBuilder(cycle, writer);
        
        return cycle;
    }
    
    protected void trainResponseBuilder(IRequestCycle cycle, IMarkupWriter writer)
    {
        ResponseBuilder builder = 
            new DefaultResponseBuilder(writer == null ? NullWriter.getSharedInstance() : writer);
        
        expect(cycle.getResponseBuilder()).andReturn(builder);
    }
    
    protected void trainIsRewinding(IRequestCycle cycle, boolean rewinding)
    {
        expect(cycle.isRewinding()).andReturn(rewinding);
    }

    protected IRequestCycle newCycleGetPage(String pageName, IPage page)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        expect(cycle.getPage(pageName)).andReturn(page);

        return cycle;
    }

    protected IRequestCycle newCycleGetUniqueId(String id, String uniqueId)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        expect(cycle.getUniqueId(id)).andReturn(uniqueId);
        return cycle;
    }

    protected IRequestCycle newCycleGetParameter(String name, String value)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        expect(cycle.getParameter(name)).andReturn(value);
        return cycle;
    }

    protected IMarkupWriter newWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    protected IBinding newBinding(Object value)
    {
        MockControl control = newControl(IBinding.class);
        IBinding binding = (IBinding) control.getMock();

        expect(binding.getObject()).andReturn(value);
        return binding;
    }

    protected IBinding newBinding(Location location)
    {
        IBinding binding = newBinding();

        trainGetLocation(binding, location);

        return binding;
    }

    protected IComponent newComponent(String extendedId, Location location)
    {
        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();

        expect(component.getExtendedId()).andReturn(extendedId);
        expect(component.getLocation()).andReturn(location);
        return component;
    }

    protected IComponentSpecification newSpec(String parameterName, IParameterSpecification pspec)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        expect(spec.getParameter(parameterName)).andReturn(pspec);
        return spec;
    }

    protected IRender newRender()
    {
        return (IRender) newMock(IRender.class);
    }

    protected IPage newPage()
    {
        return (IPage) newMock(IPage.class);
    }

    protected IPage newPage(String name)
    {
        return newPage(name, 1);
    }

    protected IPage newPage(String name, int count)
    {
        IPage page = (IPage)newMock(IPage.class);
        
        expect(page.getPageName()).andReturn(name).times(count);
        
        return page;
    }

    protected IForm newForm()
    {
        return (IForm) newMock(IForm.class);
    }

    protected IRender newBody()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                writer.print("BODY");
            }
        };
    }

    protected PageRenderSupport newPageRenderSupport()
    {
        return (PageRenderSupport) newMock(PageRenderSupport.class);
    }

    protected void trainGetSupport(IRequestCycle cycle, PageRenderSupport support)
    {
        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, support);
    }

    protected void trainGetAttribute(IRequestCycle cycle, String attributeName, Object attribute)
    {
        expect(cycle.getAttribute(attributeName)).andReturn(attribute);
    }

    protected void trainGetUniqueId(IRequestCycle cycle, String id, String uniqueId)
    {
        expect(cycle.getUniqueId(id)).andReturn(uniqueId);
    }

    protected void trainGetIdPath(IComponent component, String idPath)
    {
        expect(component.getIdPath()).andReturn(idPath);
    }

    protected void trainGetParameter(IRequestCycle cycle, String name, String value)
    {
        expect(cycle.getParameter(name)).andReturn(value);
    }

    protected void trainGetPageName(IPage page, String pageName)
    {
        expect(page.getPageName()).andReturn(pageName);
    }

    protected void trainBuildURL(IAsset asset, IRequestCycle cycle, String URL)
    {
        expect(asset.buildURL()).andReturn(URL);
    }

    protected IAsset newAsset()
    {
        return (IAsset) newMock(IAsset.class);
    }

    protected IEngine newEngine(ClassResolver resolver)
    {
        IEngine engine = (IEngine) newMock(IEngine.class);
        
        return engine;
    }

    protected void trainGetEngine(IPage page, IEngine engine)
    {
        expect(page.getEngine()).andReturn(engine);
    }

    protected IComponent newComponent()
    {
        return (IComponent) newMock(IComponent.class);
    }

    protected void trainGetPage(IComponent component, IPage page)
    {
        expect(component.getPage()).andReturn(page);
    }

    protected void trainGetExtendedId(IComponent component, String extendedId)
    {
        expect(component.getExtendedId()).andReturn(extendedId);
    }

    protected void trainGetLocation(Locatable locatable, Location location)
    {
        expect(locatable.getLocation()).andReturn(location);
    }

    protected IBinding newBinding()
    {
        return (IBinding) newMock(IBinding.class);
    }

    protected void trainGetComponent(IComponent container, String componentId, IComponent containee)
    {
        expect(container.getComponent(componentId)).andReturn(containee);
    }

    protected IEngineService newEngineService()
    {
        return (IEngineService) newMock(IEngineService.class);
    }

    protected void trainGetLink(IEngineService service, IRequestCycle cycle, boolean post,
            Object parameter, ILink link)
    {
        expect(service.getLink(post, parameter)).andReturn(link);
    }

    protected void trainGetLinkCheckIgnoreParameter(IEngineService service, IRequestCycle cycle,
            boolean post, Object parameter, ILink link)
    {
        expect(service.getLink(eq(post), anyObject())).andReturn(link);
    }

    protected void trainGetURL(ILink link, String URL)
    {
        expect(link.getURL()).andReturn(URL);
    }

    protected void trainGetPageRenderSupport(IRequestCycle cycle, PageRenderSupport support)
    {
        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, support);
    }

    protected IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    protected Resource newResource()
    {
        return (Resource) newMock(Resource.class);
    }

    protected WebRequest newRequest()
    {
        return (WebRequest) newMock(WebRequest.class);
    }

    protected void trainEncodeURL(IRequestCycle rc, String URL, String encodedURL)
    {
        expect(rc.encodeURL(URL)).andReturn(encodedURL);
    }

    protected void trainGetServerPort(WebRequest request, int port)
    {
        expect(request.getServerPort()).andReturn(port);
    }

    protected void trainGetServerName(WebRequest request, String serverName)
    {
        expect(request.getServerName()).andReturn(serverName);
    }

    protected void trainGetScheme(WebRequest request, String scheme)
    {
        expect(request.getScheme()).andReturn(scheme);
    }

    protected NestedMarkupWriter newNestedWriter()
    {
        return (NestedMarkupWriter) newMock(NestedMarkupWriter.class);
    }

    protected void trainGetNestedWriter(IMarkupWriter writer, NestedMarkupWriter nested)
    {
        expect(writer.getNestedWriter()).andReturn(nested);
    }
    
    protected void trainGetURL(ILink link, String scheme, String anchor, String URL)
    {
        trainGetURL(link, scheme, anchor, URL, 0);
    }
    
    protected void trainGetURL(ILink link, String scheme, String anchor, String URL, int port)
    {
        expect(link.getURL(scheme, null, port, anchor, true)).andReturn(URL);
    }

    protected ILink newLink()
    {
        return (ILink) newMock(ILink.class);
    }

    protected void trainGetLink(ILinkComponent component, IRequestCycle cycle, ILink link)
    {
        expect(component.getLink(cycle)).andReturn(link);
    }

    protected void trainGetEngine(IRequestCycle cycle, IEngine engine)
    {
        expect(cycle.getEngine()).andReturn(engine);
    }

    protected void trainGetParameterValues(ILink link, String parameterName, String[] values)
    {
        expect(link.getParameterValues(parameterName)).andReturn(values);
    }

    protected void trainGetParameterNames(ILink link, String[] names)
    {
        expect(link.getParameterNames()).andReturn(names);
    }

    protected void trainGetSpecification(IComponent component, IComponentSpecification spec)
    {
        expect(component.getSpecification()).andReturn(spec);
    }

    protected void trainGetBinding(IComponent component, String name, IBinding binding)
    {
        expect(component.getBinding(name)).andReturn(binding);
    }

    protected Log newLog()
    {
        return (Log) newMock(Log.class);
    }

    protected void trainGetId(IComponent component, String id)
    {
        expect(component.getId()).andReturn(id);
    }
    
    protected void trainExtractBrowserEvent(IRequestCycle cycle)
    {
        expect(cycle.getParameter(BrowserEvent.NAME)).andReturn("onClick");
        
        cycle.getParameter(BrowserEvent.TYPE);
        setReturnValue(cycle, "click");
        cycle.getParameters(BrowserEvent.KEYS);
        setReturnValue(cycle, null);
        cycle.getParameter(BrowserEvent.CHAR_CODE);
        setReturnValue(cycle, null);
        cycle.getParameter(BrowserEvent.PAGE_X);
        setReturnValue(cycle, "123");
        cycle.getParameter(BrowserEvent.PAGE_Y);
        setReturnValue(cycle, "1243");
        cycle.getParameter(BrowserEvent.LAYER_X);
        setReturnValue(cycle, null);
        cycle.getParameter(BrowserEvent.LAYER_Y);
        setReturnValue(cycle, null);
        
        cycle.getParameter(BrowserEvent.TARGET + "." + BrowserEvent.TARGET_ATTR_ID);
        setReturnValue(cycle, "element1");
    }
}