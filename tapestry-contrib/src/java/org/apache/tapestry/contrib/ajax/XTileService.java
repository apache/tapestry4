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

package org.apache.tapestry.contrib.ajax;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author mindbridge
 * @author Paul Green
 * @since 4.0
 */
public class XTileService implements IEngineService
{
    public static final String SERVICE_NAME = "xtile";

    private RequestExceptionReporter _exceptionReporter;

    private WebResponse _response;

    private LinkFactory _linkFactory;

    public String getName()
    {
        return SERVICE_NAME;
    }

    public ILink getLink(boolean post, Object parameter)
    {
        Defense.isAssignable(parameter, IComponent.class, "parameter");

        IComponent component = (IComponent) parameter;

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.PAGE, component.getPage().getPageName());
        parameters.put(ServiceConstants.COMPONENT, component.getIdPath());

        return _linkFactory.constructLink(this, false, parameters, true);
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        String pageName = cycle.getParameter(ServiceConstants.PAGE);
        String componentId = cycle.getParameter(ServiceConstants.COMPONENT);

        IPage componentPage = cycle.getPage(pageName);
        IComponent component = componentPage.getNestedComponent(componentId);

        if (!(component instanceof IXTile))
            throw new ApplicationRuntimeException("Incorrect component type: was "
                    + component.getClass() + " but must be " + IXTile.class, component, null, null);

        IXTile xtile = (IXTile) component;

        String[] params = cycle.getParameters(ServiceConstants.PARAMETER);
        cycle.setListenerParameters(params);

        xtile.trigger(cycle);

        // do not squeeze on output either
        Object[] args = cycle.getListenerParameters();
        String strArgs = generateOutputString(args);
        if (strArgs != null)
        {
            OutputStream output = _response.getOutputStream(new ContentType("text/xml"));
            output.write(strArgs.getBytes("utf-8"));
        }
    }

    protected String generateOutputString(Object[] args)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Node rootNode = doc.createElement("data");
            doc.appendChild(rootNode);

            if (args != null)
            {
                for (int i = 0; i < args.length; i++)
                {
                    Object value = args[i];

                    Node spNode = doc.createElement("sp");
                    rootNode.appendChild(spNode);

                    Node valueNode = doc.createTextNode(value.toString());
                    spNode.appendChild(valueNode);
                }
            }

            TransformerFactory trf = TransformerFactory.newInstance();
            Transformer tr = trf.newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource domSrc = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult res = new StreamResult(writer);
            tr.transform(domSrc, res);
            writer.close();

            return writer.toString();
        }
        catch (Exception e)
        {
            _exceptionReporter.reportRequestException("Cannot generate XML", e);
            return null;
        }
    }

    public void setExceptionReporter(RequestExceptionReporter exceptionReporter)
    {
        _exceptionReporter = exceptionReporter;
    }

    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

}
