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

package net.sf.tapestry.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.engine.AbstractService;

/**
 *  A service for building URLs to and accessing {@link IAsset}s.
 *  Most of the work is deferred to the {@link IAsset} instance.
 *
 *  <p>The retrieval part is directly linked to {@link PrivateAsset}.
 *  The service responds to a URL that encodes the path of a resource
 *  within the classpath.  The 
 *  {@link #service(IEngineServiceView, IRequestCycle, ResponseOutputStream)} 
 *  method reads the resource and streams it out.
 *
 *  <p>TBD: Security issues.  Should only be able to retrieve a
 *  resource that was previously registerred in some way
 *  ... otherwise, hackers will be able to suck out the .class files
 *  of the application!
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class AssetService extends AbstractService
{
    private String _servletPath;

    /**
     *  Defaults MIME types, by extension, used when the servlet container
     *  doesn't provide MIME types.  ServletExec Debugger, for example,
     *  fails to do provide these.
     *
     **/

    private final static Map mimeTypes;

    static {
        mimeTypes = new HashMap(17);
        mimeTypes.put("css", "text/css");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("htm", "text/html");
        mimeTypes.put("html", "text/html");
    }

    private static final int BUFFER_SIZE = 10240;

    /**
     *  Builds a {@link Gesture} for a {@link PrivateAsset}.
     *
     *  <p>A single parameter is expected, the resource path of the asset
     *  (which is expected to start with a leading slash).
     *
     **/

    public Gesture buildGesture(
        IRequestCycle cycle,
        IComponent component,
        String[] parameters)
    {
        if (parameters == null || parameters.length != 1)
            throw new ApplicationRuntimeException(
                Tapestry.getString("service-single-parameter", ASSET_SERVICE));

        // Service is stateless

        return assembleGesture(cycle, ASSET_SERVICE, parameters, null, false);
    }

    public String getName()
    {
        return ASSET_SERVICE;
    }

    private static String getMimeType(String path)
    {
        String key;
        String result;
        int dotx;

        dotx = path.lastIndexOf('.');
        key = path.substring(dotx + 1).toLowerCase();

        result = (String) mimeTypes.get(key);

        if (result == null)
            result = "text/plain";

        return result;
    }

    /**
     *  Retrieves a resource from the classpath and returns it to the
     *  client in a binary output stream.
     *
     *  <p>TBD: Security issues.  Hackers can download .class files.
     *
     *  <p>TBD: Error handling.  What to do if an IOException is
     *  thrown, or the resource doesn't exist?  Typically, we're
     *  downloading an image file and there's no way to communicate
     *  the error back to the client web browser.
     *
     **/

    public boolean service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException, RequestCycleException
    {
        RequestContext context = cycle.getRequestContext();
        String resourcePath = context.getParameter(CONTEXT_QUERY_PARMETER_NAME);

        URL resourceURL =
            cycle.getEngine().getResourceResolver().getResource(resourcePath);

        if (resourceURL == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("missing-resource", resourcePath));

        URLConnection resourceConnection = resourceURL.openConnection();

        ServletContext servletContext =
            cycle.getRequestContext().getServlet().getServletContext();

        // Getting the content type and length is very dependant
        // on support from the application server (represented
        // here by the servletContext).

        String contentType = servletContext.getMimeType(resourcePath);
        int contentLength = resourceConnection.getContentLength();

        try
        {
            if (contentLength > 0)
                cycle.getRequestContext().getResponse().setContentLength(
                    contentLength);

            // Set the content type.  If the servlet container doesn't
            // provide it, try and guess it by the extension.

            if (contentType == null || contentType.length() == 0)
                contentType = getMimeType(resourcePath);

            output.setContentType(contentType);

            // Disable any further buffering inside the ResponseOutputStream

            output.forceFlush();

            InputStream input = resourceConnection.getInputStream();

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true)
            {
                int bytesRead = input.read(buffer);

                if (bytesRead < 0)
                    break;

                output.write(buffer, 0, bytesRead);
            }

            input.close();

            // Return false, to indicate that no server side state could have changed.

            return false;
        }

        catch (Throwable ex)
        {
            String title =
                Tapestry.getString(
                    "AssetService.exception-report-title",
                    resourcePath);

            engine.reportException(title, ex);

            return false;
        }
    }
}