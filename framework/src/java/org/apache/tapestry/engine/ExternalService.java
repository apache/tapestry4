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

package org.apache.tapestry.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;

/**
 * The external service enables external applications to reference Tapestry pages via a URL. Pages
 * which can be referenced by the external service must implement the {@link IExternalPage}
 * interface. The external service enables the bookmarking of pages.
 * <p>
 * The external service may also be used by the Tapestry JSP taglibrary (
 * {@link org.apache.tapestry.jsp.ExternalURLTag}and {@link org.apache.tapestry.jsp.ExternalTag}).
 * <p>
 * You can try and second guess the URL format used by Tapestry. The default URL format for the
 * external service is: <blockquote>
 * <tt>http://localhost/app?service=external/<i>[Page Name]</i>&amp;sp=[Param 0]&amp;sp=[Param 1]...</tt>
 * </blockquote> For example to view the "ViewCustomer" page the service parameters 5056 (customer
 * ID) and 309 (company ID) the external service URL would be: <blockquote>
 * <tt>http://localhost/myapp?service=external&amp;context=<b>ViewCustomer</b>&amp;sp=<b>5056</b>&amp;sp=<b>302</b></tt>
 * </blockquote> In this example external service will get a "ViewCustomer" page and invoke the
 * {@link IExternalPage#activateExternalPage(Object[], IRequestCycle)}method with the parameters:
 * Object[] { new Integer(5056), new Integer(302) }.
 * <p>
 * Note service parameters (sp) need to be prefixed by valid
 * {@link org.apache.tapestry.util.io.DataSqueezerImpl}adaptor char. These adaptor chars are
 * automatically provided in URL's created by the <tt>buildGesture()</tt> method. However if you
 * hand coded an external service URL you will need to ensure valid prefix chars are present.
 * <p>
 * <table border="1" cellpadding="2">
 * <tr>
 * <th>Prefix char(s)</th>
 * <th>Mapped Java Type</th>
 * </tr>
 * <tr>
 * <td>&nbsp;TF</td>
 * <td>&nbsp;boolean</td>
 * </tr>
 * <tr>
 * <td>&nbsp;b</td>
 * <td>&nbsp;byte</td>
 * </tr>
 * <tr>
 * <td>&nbsp;c</td>
 * <td>&nbsp;char</td>
 * </tr>
 * <tr>
 * <td>&nbsp;d</td>
 * <td>&nbsp;double</td>
 * </tr>
 * <tr>
 * <td>&nbsp;-0123456789</td>
 * <td>&nbsp;integer</td>
 * </tr>
 * <tr>
 * <td>&nbsp;l</td>
 * <td>&nbsp;long</td>
 * </tr>
 * <tr>
 * <td>&nbsp;S</td>
 * <td>&nbsp;String</td>
 * </tr>
 * <tr>
 * <td>&nbsp;s</td>
 * <td>&nbsp;short</td>
 * </tr>
 * <tr>
 * <td>&nbsp;other chars</td>
 * <td>&nbsp; <tt>String</tt> without truncation of first char</td>
 * </tr>
 * <table>
 * <p>
 * <p>
 * A good rule of thumb is to keep the information encoded in the URL short and simple, and restrict
 * it to just Strings and Integers. Integers can be encoded as-is. Prefixing all Strings with the
 * letter 'S' will ensure that they are decoded properly. Again, this is only relevant if an
 * {@link org.apache.tapestry.IExternalPage}is being referenced from static HTML or JSP and the URL
 * must be assembled in user code ... when the URL is generated by Tapestry, it is automatically
 * created with the correct prefixes and encodings (as with any other service).
 * 
 * @see org.apache.tapestry.IExternalPage
 * @see org.apache.tapestry.jsp.ExternalTag
 * @see org.apache.tapestry.jsp.ExternalURLTag
 * @author Howard Lewis Ship
 * @author Malcolm Edgar
 * @since 2.2
 */

public class ExternalService implements IEngineService
{
    /** @since 4.0 */

    private ResponseRenderer _responseRenderer;

    /** @since 4.0 */
    private LinkFactory _linkFactory;

    public ILink getLink(boolean post, Object parameter)
    {
        Defense.isAssignable(parameter, ExternalServiceParameter.class, "parameter");

        ExternalServiceParameter esp = (ExternalServiceParameter) parameter;

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.PAGE, esp.getPageName());
        parameters.put(ServiceConstants.PARAMETER, esp.getServiceParameters());

        return _linkFactory.constructLink(this, post, parameters, true);
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        String pageName = cycle.getParameter(ServiceConstants.PAGE);
        IPage rawPage = cycle.getPage(pageName);

        IExternalPage page = null;

        try
        {
            page = (IExternalPage) rawPage;
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(EngineMessages.pageNotCompatible(
                    rawPage,
                    IExternalPage.class), rawPage, null, ex);
        }

        Object[] parameters = _linkFactory.extractListenerParameters(cycle);

        cycle.setListenerParameters(parameters);

        cycle.activate(page);

        page.activateExternalPage(parameters, cycle);

        _responseRenderer.renderResponse(cycle);
    }

    public String getName()
    {
        return Tapestry.EXTERNAL_SERVICE;
    }

    /** @since 4.0 */

    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    /** @since 4.0 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }
}