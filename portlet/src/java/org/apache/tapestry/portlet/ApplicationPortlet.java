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

package org.apache.tapestry.portlet;

import java.io.IOException;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;

/**
 * Portlet implementation for Tapestry Portlet applilcations. It's job is to create and manage the
 * HiveMind registry, to use the <code>tapestry.portlet.PortletApplicationInitializer</code>
 * service to initialize HiveMind, and the delegate requests to the
 * <code>tapestry.portlet.ActionRequestServicer</code> and
 * <code>tapestry.portlet.RenderRequestServicer</code> services.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ApplicationPortlet implements Portlet
{
    Registry _registry;

    ActionRequestServicer _actionRequestServicer;

    RenderRequestServicer _renderRequestServicer;

    public void destroy()
    {
        try
        {
            _registry.shutdown();
        }
        finally
        {
            _registry = null;
            _actionRequestServicer = null;
            _renderRequestServicer = null;
        }
    }

    public void init(PortletConfig config) throws PortletException
    {
        _registry = constructRegistry(config);

        PortletApplicationInitializer initializer = (PortletApplicationInitializer) _registry
                .getService(
                        "tapestry.portlet.PortletApplicationInitializer",
                        PortletApplicationInitializer.class);

        initializer.initialize(config);

        _actionRequestServicer = (ActionRequestServicer) _registry.getService(
                "tapestry.portlet.ActionRequestServicer",
                ActionRequestServicer.class);

        _renderRequestServicer = (RenderRequestServicer) _registry.getService(
                "tapestry.portlet.RenderRequestServicer",
                RenderRequestServicer.class);
    }

    /**
     * Constructs the Registry. The Registry is constructed from the classpath, plus two optional
     * files:
     * <ul>
     * <li>WEB-INF/ <em>name</em> /hivemodule.xml</li>
     * <li>WEB-INF/hivemodule.xml</li>
     * </ul>.
     * <p>
     * Where <em>name</em> is the name of the portlet.
     */

    protected Registry constructRegistry(PortletConfig config)
    {
        RegistryBuilder builder = new RegistryBuilder();

        ClassResolver resolver = new DefaultClassResolver();

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(resolver));

        String name = config.getPortletName();
        WebContext context = new PortletWebContext(config.getPortletContext());

        addModuleIfExists(builder, resolver, context, "/WEB-INF/" + name + "/hivemodule.xml");
        addModuleIfExists(builder, resolver, context, "/WEB-INF/hivemodule.xml");

        return builder.constructRegistry(Locale.getDefault());
    }

    /**
     * Looks for a file in the context; if it exists, it is expected to be a HiveMind module
     * descriptor, and is added to the builder.
     * 
     * @since 3.1
     */

    protected void addModuleIfExists(RegistryBuilder builder, ClassResolver resolver,
            WebContext context, String path)
    {
        Resource r = new WebContextResource(context, path);

        if (r.getResourceURL() == null)
            return;

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(resolver, r));
    }

    public void processAction(ActionRequest request, ActionResponse response)
            throws PortletException, IOException
    {
        try
        {
            _actionRequestServicer.service(request, response);
        }
        catch (RuntimeException ex)
        {
            throw new PortletException(PortletMessages.errorProcessingAction(ex), ex);
        }
        finally
        {
            _registry.cleanupThread();
        }
    }

    public void render(RenderRequest request, RenderResponse response) throws PortletException,
            IOException
    {
        try
        {
            _renderRequestServicer.service(request, response);
        }
        catch (RuntimeException ex)
        {
            throw new PortletException(PortletMessages.errorProcessingRender(ex), ex);
        }
        finally
        {
            _registry.cleanupThread();
        }
    }
}