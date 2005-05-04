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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.describe.RenderStrategy;
import org.apache.tapestry.error.ErrorMessages;
import org.apache.tapestry.error.ExceptionPresenter;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.util.exception.ExceptionDescription;
import org.apache.tapestry.util.exception.ExceptionProperty;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Service used to present a runtime exception to the user. This is very tricky in the Portlet world
 * because of the split between the action and render requests (much more likely to get an error
 * during the action request than during the render request, but both are possible).
 * <p>
 * During an action request, this code will render the HTML markup for the exception into a buffer
 * that is stored as persistent attribute in the portal session.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletExceptionPresenter implements ExceptionPresenter
{
    private PortletRequestGlobals _globals;

    private RenderStrategy _renderStrategy;

    private WebRequest _request;

    private RequestExceptionReporter _requestExceptionReporter;

    private WebResponse _response;

    public void presentException(IRequestCycle cycle, Throwable cause)
    {
        try
        {
            if (_globals.isRenderRequest())
                reportRenderRequestException(cycle, cause);
            else
                reportActionRequestException(cycle, cause);
        }
        catch (Exception ex)
        {
            // Worst case scenario. The exception page itself is broken, leaving
            // us with no option but to write the cause to the output.

            // Also, write the exception thrown when redendering the exception
            // page, so that can get fixed as well.

            _requestExceptionReporter.reportRequestException(PortletMessages
                    .errorReportingException(ex), ex);

            // And throw the exception.

            throw new ApplicationRuntimeException(ex.getMessage(), ex);
        }

        _requestExceptionReporter.reportRequestException(ErrorMessages
                .unableToProcessClientRequest(cause), cause);
    }

    private void reportActionRequestException(IRequestCycle cycle, Throwable cause)
    {
        CharArrayWriter caw = new CharArrayWriter();
        PrintWriter pw = new PrintWriter(caw);

        IMarkupWriter writer = new MarkupWriterImpl("text/html", pw, new AsciiMarkupFilter());

        writeException(writer, cycle, cause);

        writer.close();

        String markup = caw.toString();

        _request.getSession(true).setAttribute(
                PortletConstants.PORTLET_EXCEPTION_MARKUP_ATTRIBUTE,
                markup);

        ActionResponse response = _globals.getActionResponse();

        response.setRenderParameter(ServiceConstants.SERVICE, PortletConstants.EXCEPTION_SERVICE);
    }

    private void reportRenderRequestException(IRequestCycle cycle, Throwable cause)
            throws IOException
    {
        PrintWriter pw = _response.getPrintWriter(new ContentType("text/html"));

        IMarkupWriter writer = new MarkupWriterImpl("text/html", pw, new AsciiMarkupFilter());

        writeException(writer, cycle, cause);
    }

    public void setGlobals(PortletRequestGlobals globals)
    {
        _globals = globals;
    }

    public void setRenderStrategy(RenderStrategy renderStrategy)
    {
        _renderStrategy = renderStrategy;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    public void setRequestExceptionReporter(RequestExceptionReporter requestExceptionReporter)
    {
        _requestExceptionReporter = requestExceptionReporter;
    }

    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    private void writeException(IMarkupWriter writer, IRequestCycle cycle,
            ExceptionDescription exception, boolean showStackTrace)
    {
        writer.begin("div");
        writer.attribute("class", "portlet-section-header");
        writer.print(exception.getExceptionClassName());
        writer.end();
        writer.println();

        writer.begin("div");
        writer.attribute("class", "portlet-msg-error");
        writer.print(exception.getMessage());
        writer.end();
        writer.println();

        ExceptionProperty[] properties = exception.getProperties();

        if (properties.length > 0)
        {

            writer.begin("table");
            writer.attribute("class", "portlet-section-subheader");

            for (int i = 0; i < properties.length; i++)
            {
                writer.begin("tr");

                writer.attribute("class", i % 2 == 0 ? "portlet-section-body"
                        : "portlet-section-alternate");

                writer.begin("th");
                writer.print(properties[i].getName());
                writer.end();
                writer.println();

                writer.begin("td");

                _renderStrategy.renderObject(properties[i].getValue(), writer, cycle);
                writer.end("tr");
                writer.println();
            }

            writer.end();
            writer.println();
        }

        if (!showStackTrace)
            return;

        writer.begin("ul");

        String[] trace = exception.getStackTrace();

        for (int i = 0; i < trace.length; i++)
        {
            writer.begin("li");
            writer.print(trace[i]);
            writer.end();
            writer.println();
        }

        writer.end();
        writer.println();

    }

    private void writeException(IMarkupWriter writer, IRequestCycle cycle, Throwable cause)
    {
        ExceptionDescription[] exceptions = new ExceptionAnalyzer().analyze(cause);

        for (int i = 0; i < exceptions.length; i++)
            writeException(writer, cycle, exceptions[i], i + 1 == exceptions.length);
    }

}