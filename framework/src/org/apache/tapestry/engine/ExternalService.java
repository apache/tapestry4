/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 * The external service enables external applications
 * to reference Tapestry pages via a URL. Pages which can be referenced
 * by the external service must implement the {@link IExternalPage}
 * interface. The external service enables the bookmarking of pages.
 * 
 * <p>
 * The external service may also be used by the Tapestry JSP taglibrary
 * ({@link org.apache.tapestry.jsp.ExternalURLTag} and {@link org.apache.tapestry.jsp.ExternalTag}).
 * 
 * <p> 
 * You can try and second guess the URL format used by Tapestry. 
 * The default URL format for the external service is:
 * <blockquote>
 * <tt>http://localhost/app?service=external/<i>[Page Name]</i>&amp;sp=[Param 0]&amp;sp=[Param 1]...</tt>
 * </blockquote>
 * For example to view the "ViewCustomer" page the service parameters 5056 (customer ID) and
 * 309 (company ID) the external service URL would be:
 * <blockquote>
 * <tt>http://localhost/myapp?service=external&amp;context=<b>ViewCustomer</b>&amp;sp=<b>5056</b>&amp;sp=<b>302</b></tt>
 * </blockquote>
 * In this example external service will get a "ViewCustomer" page and invoke the 
 * {@link IExternalPage#activateExternalPage(Object[], IRequestCycle)} method with the parameters:  
 * Object[] { new Integer(5056), new Integer(302) }.
 * <p>
 * Note service parameters (sp) need to be prefixed by valid
 * {@link DataSqueezer} adaptor char. These adaptor chars are automatically provided in 
 * URL's created by the <tt>buildGesture()</tt> method. However if you hand coded an external 
 * service URL you will need to ensure valid prefix chars are present.
 * <p>
 * <table border="1" cellpadding="2">
 *  <tr>
 *   <th>Prefix char(s)</th><th>Mapped Java Type</th>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;TF</td><td>&nbsp;boolean</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;b</td><td>&nbsp;byte</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;c</td><td>&nbsp;char</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;d</td><td>&nbsp;double</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;-0123456789</td><td>&nbsp;integer</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;l</td><td>&nbsp;long</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;S</td><td>&nbsp;String</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;s</td><td>&nbsp;short</td>
 *  </tr>
 *  <tr>
 *   <td>&nbsp;other chars</td>
 *   <td>&nbsp;<tt>String</tt> without truncation of first char</td>
 *  </tr>
 * <table>
 *  <p>
 *  <p>
 *  A good rule of thumb is to keep the information encoded in the URL short and simple, and restrict it
 *  to just Strings and Integers.  Integers can be encoded as-is.  Prefixing all Strings with the letter 'S'
 *  will ensure that they are decoded properly.  Again, this is only relevant if an 
 *  {@link org.apache.tapestry.IExternalPage} is being referenced from static HTML or JSP and the
 *  URL must be assembled in user code ... when the URL is generated by Tapestry, it is automatically
 *  created with the correct prefixes and encodings (as with any other service).
 * 
 * @see org.apache.tapestry.IExternalPage
 * @see org.apache.tapestry.jsp.ExternalTag
 * @see org.apache.tapestry.jsp.ExternalURLTag
 *
 * @author Howard Lewis Ship
 * @author Malcolm Edgar
 * @since 2.2
 *  
 **/

public class ExternalService extends AbstractService
{

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (parameters == null || parameters.length == 0)
            throw new ApplicationRuntimeException(
                Tapestry.format("service-requires-parameters", Tapestry.EXTERNAL_SERVICE));

        String pageName = (String) parameters[0];
        String[] context = new String[] { pageName };

        Object[] pageParameters = new Object[parameters.length - 1];
        System.arraycopy(parameters, 1, pageParameters, 0, parameters.length - 1);

        return constructLink(cycle, Tapestry.EXTERNAL_SERVICE, context, pageParameters, true);
    }

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
    {
        IExternalPage page = null;

        String[] context = getServiceContext(cycle.getRequestContext());

        if (context == null || context.length != 1)
            throw new ApplicationRuntimeException(
                Tapestry.format("service-single-context-parameter", Tapestry.EXTERNAL_SERVICE));

        String pageName = context[0];

        try
        {
            page = (IExternalPage) cycle.getPage(pageName);
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ExternalService.page-not-compatible", pageName),
                ex);
        }

        Object[] parameters = getParameters(cycle);

        cycle.setServiceParameters(parameters);

        cycle.activate(page);

        page.activateExternalPage(parameters, cycle);

        // Render the response.
        engine.renderResponse(cycle, output);
    }

    public String getName()
    {
        return Tapestry.EXTERNAL_SERVICE;
    }
}