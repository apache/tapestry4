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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;
import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.URLResource;
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

import com.javaforge.tapestry.testng.TestBase;

/**
 * Base class for testing components, or testing classes that operate on components. Simplifies
 * creating much of the infrastructure around the components.
 * <p>
 * This class may eventually be part of the Tapestry distribution.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BaseComponentTestCase extends TestBase
{
    private Creator _creator;

    protected Creator getCreator()
    {
        if (_creator == null)
            _creator = new Creator();
        
        return _creator;
    }

    protected ClassResolver getClassResolver()
    {
        return new DefaultClassResolver();
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

    protected void assertExceptionSubstring(Throwable t, String msg)
    {
        assertTrue(t.getMessage().contains(msg));
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

    protected IRequestCycle newCycle()
    {
        return newMock(IRequestCycle.class);
    }

    protected IRequestCycle newCycle(IMarkupWriter writer)
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        
        trainResponseBuilder(cycle, writer);
        
        return cycle;
    }
    
    protected IRequestCycle newCycle(boolean rewinding)
    {
        return newCycle(rewinding, null);
    }
    
    protected IRequestCycle newCycle(boolean rewinding, boolean trainWriter)
    {
        IRequestCycle cycle = newRequestCycle();
        
        trainIsRewinding(cycle, rewinding);
        
        if (trainWriter)
            trainResponseBuilder(cycle, null);
        
        return cycle;
    }
    
    protected IRequestCycle newCycle(boolean rewinding, IMarkupWriter writer)
    {
        IRequestCycle cycle = newRequestCycle();

        trainIsRewinding(cycle, rewinding);
        
        if (writer != null)
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
        IRequestCycle cycle = newRequestCycle();

        expect(cycle.getPage(pageName)).andReturn(page);

        return cycle;
    }

    protected IRequestCycle newCycleGetUniqueId(String id, String uniqueId)
    {
        IRequestCycle cycle = newRequestCycle();

        expect(cycle.getUniqueId(id)).andReturn(uniqueId);
        return cycle;
    }

    protected IRequestCycle newCycleGetParameter(String name, String value)
    {
        IRequestCycle cycle = newRequestCycle();

        expect(cycle.getParameter(name)).andReturn(value);
        return cycle;
    }

    protected IMarkupWriter newWriter()
    {
        return newMock(IMarkupWriter.class);
    }

    protected IBinding newBinding(Object value)
    {
        IBinding binding = newMock(IBinding.class);
        checkOrder(binding, false);
        
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
        IComponent component = newMock(IComponent.class);

        expect(component.getExtendedId()).andReturn(extendedId);
        expect(component.getLocation()).andReturn(location);
        return component;
    }

    protected IComponentSpecification newSpec(String parameterName, IParameterSpecification pspec)
    {
        IComponentSpecification spec = newMock(IComponentSpecification.class);

        expect(spec.getParameter(parameterName)).andReturn(pspec);
        return spec;
    }

    protected IRender newRender()
    {
        return newMock(IRender.class);
    }

    protected IPage newPage()
    {
        return newMock(IPage.class);
    }

    protected IPage newPage(String name)
    {
        return newPage(name, 1);
    }

    protected IPage newPage(String name, int count)
    {
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        expect(page.getPageName()).andReturn(name).times(count);
        
        return page;
    }

    protected IForm newForm()
    {
        return newMock(IForm.class);
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
        return newMock(PageRenderSupport.class);
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
        return newMock(IAsset.class);
    }

    protected IEngine newEngine(ClassResolver resolver)
    {
        IEngine engine = newMock(IEngine.class);
        
        return engine;
    }

    protected void trainGetEngine(IPage page, IEngine engine)
    {
        expect(page.getEngine()).andReturn(engine);
    }

    protected IComponent newComponent()
    {
        return newMock(IComponent.class);
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
        return newMock(IBinding.class);
    }

    protected void trainGetComponent(IComponent container, String componentId, IComponent containee)
    {
        expect(container.getComponent(componentId)).andReturn(containee);
    }

    protected IEngineService newEngineService()
    {
        return newMock(IEngineService.class);
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
        return newMock(IComponentSpecification.class);
    }

    protected Resource newResource()
    {
        return newMock(Resource.class);
    }

    protected WebRequest newRequest()
    {
        return newMock(WebRequest.class);
    }

    protected Location newLocation()
    {
        return newMock(Location.class);
    }
    
    protected Location fabricateLocation(int line)
    {
        Location location = newLocation();
        checkOrder(location, false);
        
        expect(location.getLineNumber()).andReturn(line).anyTimes();
        
        return location;
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
        return newMock(NestedMarkupWriter.class);
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
        return newMock(ILink.class);
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
        return newMock(Log.class);
    }

    protected void trainGetId(IComponent component, String id)
    {
        expect(component.getId()).andReturn(id);
    }
    
    protected void trainExtractBrowserEvent(IRequestCycle cycle)
    {
        expect(cycle.getParameter(BrowserEvent.NAME)).andReturn("onClick").anyTimes();
        
        expect(cycle.getParameter(BrowserEvent.TYPE)).andReturn("click");
        expect(cycle.getParameters(BrowserEvent.KEYS)).andReturn(null);
        expect(cycle.getParameter(BrowserEvent.CHAR_CODE)).andReturn(null);
        expect(cycle.getParameter(BrowserEvent.PAGE_X)).andReturn("123");
        expect(cycle.getParameter(BrowserEvent.PAGE_Y)).andReturn("1243");
        expect(cycle.getParameter(BrowserEvent.LAYER_X)).andReturn(null);
        expect(cycle.getParameter(BrowserEvent.LAYER_Y)).andReturn(null);
        
        expect(cycle.getParameter(BrowserEvent.TARGET + "." + BrowserEvent.TARGET_ATTR_ID))
        .andReturn("element1");
    }
    
    /**
     * Convienience method for invoking {@link #buildFrameworkRegistry(String[])} with only a single
     * file.
     */
    protected Registry buildFrameworkRegistry(String file) throws Exception
    {
        return buildFrameworkRegistry(new String[]
        { file });
    }
    
    /**
     * Builds a minimal registry, containing only the specified files, plus the master module
     * descriptor (i.e., those visible on the classpath). Files are resolved using
     * {@link HiveMindTestCase#getResource(String)}.
     */
    protected Registry buildFrameworkRegistry(String[] files) throws Exception
    {
        ClassResolver resolver = getClassResolver();

        List descriptorResources = new ArrayList();
        for (int i = 0; i < files.length; i++)
        {
            Resource resource = getResource(files[i]);

            descriptorResources.add(resource);
        }

        ModuleDescriptorProvider provider = new XmlModuleDescriptorProvider(resolver,
                descriptorResources);

        return buildFrameworkRegistry(provider);
    }
    
    /**
     * Builds a registry, containing only the modules delivered by the specified
     * {@link org.apache.hivemind.ModuleDescriptorProvider}, plus the master module descriptor
     * (i.e., those visible on the classpath).
     */
    protected Registry buildFrameworkRegistry(ModuleDescriptorProvider customProvider)
    {
        ClassResolver resolver = getClassResolver();

        RegistryBuilder builder = new RegistryBuilder();

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(resolver));
        builder.addModuleDescriptorProvider(customProvider);

        return builder.constructRegistry(Locale.getDefault());
    }

    /**
     * Builds a registry from exactly the provided resource; this registry will not include the
     * <code>hivemind</code> module.
     */
    protected Registry buildMinimalRegistry(Resource l) throws Exception
    {
        RegistryBuilder builder = new RegistryBuilder();

        return builder.constructRegistry(Locale.getDefault());
    }
    
    /**
     * Returns the given file as a {@link Resource} from the classpath. Typically, this is to find
     * files in the same folder as the invoking class.
     */
    protected Resource getResource(String file)
    {
        URL url = getClass().getResource(file);

        if (url == null)
            throw new NullPointerException("No resource named '" + file + "'.");

        return new URLResource(url);
    }
}