//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.AbstractService;
import org.apache.tapestry.engine.IEngineServiceView;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  A service for building URLs to and accessing {@link org.apache.tapestry.IAsset}s.
 *  Most of the work is deferred to the {@link org.apache.tapestry.IAsset} instance.
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
    /**
     *  Defaults MIME types, by extension, used when the servlet container
     *  doesn't provide MIME types.  ServletExec Debugger, for example,
     *  fails to do provide these.
     *
     **/

    private final static Map _mimeTypes;

    static {
        _mimeTypes = new HashMap(17);
        _mimeTypes.put("css", "text/css");
        _mimeTypes.put("gif", "image/gif");
        _mimeTypes.put("jpg", "image/jpeg");
        _mimeTypes.put("jpeg", "image/jpeg");
        _mimeTypes.put("htm", "text/html");
        _mimeTypes.put("html", "text/html");
    }

    private static final int BUFFER_SIZE = 10240;

    /**
     *  Builds a {@link ILink} for a {@link PrivateAsset}.
     *
     *  <p>A single parameter is expected, the resource path of the asset
     *  (which is expected to start with a leading slash).
     *
     **/

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 2)
            throw new ApplicationRuntimeException(
                Tapestry.format("service-incorrect-parameter-count", Tapestry.ASSET_SERVICE, new Integer(2)));

        // Service is stateless

        return constructLink(cycle, Tapestry.ASSET_SERVICE, null, parameters, false);
    }

    public String getName()
    {
        return Tapestry.ASSET_SERVICE;
    }

    private static String getMimeType(String path)
    {
        String key;
        String result;
        int dotx;

        dotx = path.lastIndexOf('.');
        key = path.substring(dotx + 1).toLowerCase();

        result = (String) _mimeTypes.get(key);

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
     *
     **/

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws IOException
    {
        Object[] parameters = getParameters(cycle);

        if (Tapestry.size(parameters) != 2)
            throw new ApplicationRuntimeException(
                Tapestry.format("service-incorrect-parameter-count", Tapestry.ASSET_SERVICE, new Integer(2)));

        String resourcePath = (String) parameters[0];
        String checksum = (String) parameters[1];

        URL resourceURL = engine.getResourceResolver().getResource(resourcePath);

        if (resourceURL == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("missing-resource", resourcePath));

        String actualChecksum = engine.getResourceChecksumSource().getChecksum(resourceURL);

        if (!actualChecksum.equals(checksum))
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("AssetService.checksum-failure", checksum, resourcePath));
        }

        URLConnection resourceConnection = resourceURL.openConnection();

        ServletContext servletContext = cycle.getRequestContext().getServlet().getServletContext();

        writeAssetContent(engine, cycle, output, resourcePath, resourceConnection, servletContext);
    }

    /**  @since 2.2 **/

    private void writeAssetContent(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output,
        String resourcePath,
        URLConnection resourceConnection,
        ServletContext servletContext)
    {
        // Getting the content type and length is very dependant
        // on support from the application server (represented
        // here by the servletContext).

        String contentType = servletContext.getMimeType(resourcePath);
        int contentLength = resourceConnection.getContentLength();

        try
        {
            if (contentLength > 0)
                cycle.getRequestContext().getResponse().setContentLength(contentLength);

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
        }
        catch (Throwable ex)
        {
            String title = Tapestry.format("AssetService.exception-report-title", resourcePath);

            engine.reportException(title, ex);
        }
    }
}