//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Category;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IExternalPage;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.engine.AbstractService;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 * The <tt>ExternalService</tt> enables external applications
 * to reference Tapestry pages via a URL. Pages which can be referenced
 * by the external service must implement the {@link IExternalPage}
 * interface. The external service enables the bookmarking of pages.
 * <p> 
 * The URL format for the external service is:
 * <blockquote>
 * <tt>http://localhost/app?service=external&amp;sp=[Page Name]&amp;sp=[Param 1]&amp;sp=[Param 2]</tt>
 * </blockquote>
 * For example to view the "ViewCustomer" page the service parameters 5056 (customer ID) and
 * 309 (company ID) the external service URL would be:
 * <blockquote>
 * <tt>http://localhost/myapp?service=external&amp;sp=<b>ViewCustomer</b>&amp;sp=<b>5056</b>&amp;sp=<b>302</b></tt>
 * <blockquote>
 * In this example external service will get a "ViewCustomer" page and invoke the 
 * {@link IExternalPage#setup(Object[], IRequestCycle)} method with the parameters:  
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
 * <table><br>
 * <p>
 * Note the {@link IPage#validate(IRequestCycle)} method is not invoked on 
 * {@link IExternalPage}s as external applications need to do be able to reference a specific page
 * without going through validation.
 *
 * @see net.sf.tapestry.IExternalPage
 *
 * @author Howard Lewis Ship
 * @author Malcolm Edgar
 * @since 2.2
 **/
public class ExternalService extends AbstractService
{
	/** The name of the service. */
	public static final String SERVICE_NAME = "external";

	/**
	 * @see IEngineService#buildGesture(IRequestCycle, IComponent, Object[])
	 */
	public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
	{
		if (parameters == null || parameters.length == 0)
		{
			throw new ApplicationRuntimeException
                ("external service requires at least one parameter.");
		}
		return assembleGesture(cycle, SERVICE_NAME, null, parameters, true);
	}

	/**
	 * @see IEngineService#service(IEngineServiceView, IRequestCycle, ResponseOutputStream)
	 */
	public boolean service(IEngineServiceView engine, IRequestCycle cycle, 
        ResponseOutputStream output) throws RequestCycleException, ServletException, IOException
	{
		IExternalPage page = null;

		Object[] parameters = getParameters(cycle);

		if (parameters == null || parameters.length == 0)
		{
			throw new ApplicationRuntimeException
                ("external service requires at least one parameter.");
		}

		String pageName = parameters[0].toString();

		Object[] pageParameters = new Object[parameters.length - 1];
		for (int i = 0, j = 1; i < pageParameters.length; i++, j++)
		{
			pageParameters[i] = parameters[j];
		}

		try
		{
			page = (IExternalPage) cycle.getPage(pageName);
		}
		catch (ClassCastException e)
		{
			throw new ApplicationRuntimeException
                ("Page " + pageName + " may not be used with the " + SERVICE_NAME + " service.");
		}

		cycle.setPage(page);

		page.setup(pageParameters, cycle);

		// Render the response.
		engine.renderResponse(cycle, output);

		return true;
	}

	/**
	 * @see IEngineService#getName()
	 */
	public String getName()
	{
		return SERVICE_NAME;
	}
}
