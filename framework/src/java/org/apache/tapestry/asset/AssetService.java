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

package org.apache.tapestry.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.StaticLink;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.RequestExceptionReporter;
import org.apache.tapestry.services.ServiceConstants;

/**
 * A service for building URLs to and accessing {@link org.apache.tapestry.IAsset}s. Most of the
 * work is deferred to the {@link org.apache.tapestry.IAsset}instance.
 * <p>
 * The retrieval part is directly linked to {@link PrivateAsset}. The service responds to a URL
 * that encodes the path of a resource within the classpath. The
 * {@link #service(IRequestCycle, ResponseOutputStream)}method reads the resource and streams it
 * out.
 * <p>
 * TBD: Security issues. Should only be able to retrieve a resource that was previously registerred
 * in some way ... otherwise, hackers will be able to suck out the .class files of the application!
 * 
 * @author Howard Lewis Ship
 */

public class AssetService implements IEngineService
{
    /** @since 3.1 */
    private ClassResolver _classResolver;

    /** @since 3.1 */
    private AssetExternalizer _assetExternalizer;

    /** @since 3.1 */
    private LinkFactory _linkFactory;

    /** @since 3.1 */
    private ServletContext _servletContext;
    
    /** @since 3.1 */
    private HttpServletResponse _servletResponse;

    /**
     * Defaults MIME types, by extension, used when the servlet container doesn't provide MIME
     * types. ServletExec Debugger, for example, fails to do provide these.
     */

    private final static Map _mimeTypes;

    static
    {
        _mimeTypes = new HashMap(17);
        _mimeTypes.put("css", "text/css");
        _mimeTypes.put("gif", "image/gif");
        _mimeTypes.put("jpg", "image/jpeg");
        _mimeTypes.put("jpeg", "image/jpeg");
        _mimeTypes.put("htm", "text/html");
        _mimeTypes.put("html", "text/html");
    }

    private static final int BUFFER_SIZE = 10240;

    /** @since 3.1 */

    private RequestExceptionReporter _exceptionReporter;

    private static final String PATH = "path";

    /**
     * Builds a {@link ILink}for a {@link PrivateAsset}.
     * <p>
     * A single parameter is expected, the resource path of the asset (which is expected to start
     * with a leading slash).
     */

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        Defense.isAssignable(parameter, String.class, "parameter");

        String path = (String) parameter;

        String externalURL = _assetExternalizer.getURL(path);

        if (externalURL != null)
            return new StaticLink(externalURL);

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        parameters.put(PATH, path);

        // Service is stateless

        return _linkFactory.constructLink(cycle, parameters, false);
    }

    public String getName()
    {
        return Tapestry.ASSET_SERVICE;
    }

    private static String getMimeType(String path)
    {
        int dotx = path.lastIndexOf('.');
        String key = path.substring(dotx + 1).toLowerCase();

        String result = (String) _mimeTypes.get(key);

        if (result == null)
            result = "text/plain";

        return result;
    }

    /**
     * Retrieves a resource from the classpath and returns it to the client in a binary output
     * stream.
     * <p>
     * TBD: Security issues. Hackers can download .class files.
     */

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws ServletException,
            IOException
    {
        String path = cycle.getParameter(PATH);

        URL resourceURL = _classResolver.getResource(path);

        if (resourceURL == null)
            throw new ApplicationRuntimeException(Tapestry.format("missing-resource", path));

        URLConnection resourceConnection = resourceURL.openConnection();

        writeAssetContent(cycle, output, path, resourceConnection);
    }

    /** @since 2.2 * */

    private void writeAssetContent(IRequestCycle cycle, ResponseOutputStream output,
            String resourcePath, URLConnection resourceConnection)
    {
        // Getting the content type and length is very dependant
        // on support from the application server (represented
        // here by the servletContext).

        String contentType = _servletContext.getMimeType(resourcePath);
        int contentLength = resourceConnection.getContentLength();

        try
        {
            if (contentLength > 0)
                _servletResponse.setContentLength(contentLength);

            // Set the content type. If the servlet container doesn't
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

            _exceptionReporter.reportRequestException(title, ex);
        }
    }

    /** @since 3.1 */

    public void setExceptionReporter(RequestExceptionReporter exceptionReporter)
    {
        _exceptionReporter = exceptionReporter;
    }

    /** @since 3.1 */
    public void setAssetExternalizer(AssetExternalizer assetExternalizer)
    {
        _assetExternalizer = assetExternalizer;
    }

    /** @since 3.1 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    /** @since 3.1 */
    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    /** @since 3.1 */
    public void setServletContext(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }
    
    /** @since 3.1 */
    public void setServletResponse(HttpServletResponse servletResponse)
    {
        _servletResponse = servletResponse;
    }
}