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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.IOUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.link.StaticLink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * A service for building URLs to and accessing {@link org.apache.tapestry.IAsset}s. Most of the
 * work is deferred to the {@link org.apache.tapestry.IAsset}instance.
 * <p>
 * The retrieval part is directly linked to {@link PrivateAsset}. The service responds to a URL
 * that encodes the path of a resource within the classpath. The {@link #service(IRequestCycle)}
 * method reads the resource and streams it out.
 * <p>
 * TBD: Security issues. Should only be able to retrieve a resource that was previously registerred
 * in some way ... otherwise, hackers will be able to suck out the .class files of the application!
 * 
 * @author Howard Lewis Ship
 */

public class AssetService implements IEngineService
{

    /** @since 4.0 */
    private ClassResolver _classResolver;

    /** @since 4.0 */
    private AssetExternalizer _assetExternalizer;

    /** @since 4.0 */
    private LinkFactory _linkFactory;

    /** @since 4.0 */
    private WebContext _context;

    /** @since 4.0 */

    private WebRequest _request;

    /** @since 4.0 */
    private WebResponse _response;

    /** @since 4.0 */
    private ResourceDigestSource _digestSource;

    /**
     * Defaults MIME types, by extension, used when the servlet container doesn't provide MIME
     * types. ServletExec Debugger, for example, fails to provide these.
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

    /**
     * Startup time for this service; used to set the Last-Modified response header.
     * 
     * @since 4.0
     */

    private final long _startupTime = System.currentTimeMillis();

    /**
     * Time vended assets expire. Since a change in asset content is a change in asset URI, we want
     * them to not expire ... but a year will do.
     */

    private final long _expireTime = _startupTime + 365 * 24 * 60 * 60 * 1000;

    /** @since 4.0 */

    private RequestExceptionReporter _exceptionReporter;

    /**
     * Query parameter that stores the path to the resource (with a leading slash).
     * 
     * @since 4.0
     */

    public static final String PATH = "path";

    /**
     * Query parameter that stores the digest for the file; this is used to authenticate that the
     * client is allowed to access the file.
     * 
     * @since 4.0
     */

    public static final String DIGEST = "digest";

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

        String digest = _digestSource.getDigestForResource(path);

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        parameters.put(PATH, path);
        parameters.put(DIGEST, digest);

        // Service is stateless, which is the exception to the rule.

        return _linkFactory.constructLink(cycle, parameters, false);
    }

    public String getName()
    {
        return Tapestry.ASSET_SERVICE;
    }

    private String getMimeType(String path)
    {
        String result = _context.getMimeType(path);

        if (result == null)
        {
            int dotx = path.lastIndexOf('.');
            String key = path.substring(dotx + 1).toLowerCase();

            result = (String) _mimeTypes.get(key);

            if (result == null)
                result = "text/plain";
        }

        return result;
    }

    /**
     * Retrieves a resource from the classpath and returns it to the client in a binary output
     * stream.
     * <p>
     * TBD: Security issues. Hackers can download .class files.
     */

    public void service(IRequestCycle cycle) throws IOException
    {
        // If they were vended an asset in the past then it must be up-to date.
        // Asset URIs change if the underlying file is modified.

        if (_request.getHeader("If-Modified-Since") != null)
        {
            _response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        String path = cycle.getParameter(PATH);
        String md5 = cycle.getParameter(DIGEST);

        try
        {
            if (!_digestSource.getDigestForResource(path).equals(md5))
                throw new ApplicationRuntimeException(AssetMessages.md5Mismatch(path));

            URL resourceURL = _classResolver.getResource(path);

            if (resourceURL == null)
                throw new ApplicationRuntimeException(AssetMessages.noSuchResource(path));

            URLConnection resourceConnection = resourceURL.openConnection();

            writeAssetContent(cycle, path, resourceConnection);
        }
        catch (Throwable ex)
        {
            _exceptionReporter.reportRequestException(AssetMessages.exceptionReportTitle(path), ex);
        }

    }

    /** @since 2.2 */

    private void writeAssetContent(IRequestCycle cycle, String resourcePath,
            URLConnection resourceConnection) throws IOException
    {
        InputStream input = null;

        try
        {
            // Getting the content type and length is very dependant
            // on support from the application server (represented
            // here by the servletContext).

            String contentType = getMimeType(resourcePath);
            int contentLength = resourceConnection.getContentLength();

            if (contentLength > 0)
                _response.setContentLength(contentLength);

            _response.setDateHeader("Last-Modified", _startupTime);
            _response.setDateHeader("Expires", _expireTime);

            // Set the content type. If the servlet container doesn't
            // provide it, try and guess it by the extension.

            if (contentType == null || contentType.length() == 0)
                contentType = getMimeType(resourcePath);

            OutputStream output = _response.getOutputStream(new ContentType(contentType));

            input = new BufferedInputStream(resourceConnection.getInputStream());

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true)
            {
                int bytesRead = input.read(buffer);

                if (bytesRead < 0)
                    break;

                output.write(buffer, 0, bytesRead);
            }

            input.close();
            input = null;
        }
        finally
        {
            IOUtils.close(input);
        }
    }

    /** @since 4.0 */

    public void setExceptionReporter(RequestExceptionReporter exceptionReporter)
    {
        _exceptionReporter = exceptionReporter;
    }

    /** @since 4.0 */
    public void setAssetExternalizer(AssetExternalizer assetExternalizer)
    {
        _assetExternalizer = assetExternalizer;
    }

    /** @since 4.0 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    /** @since 4.0 */
    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    /** @since 4.0 */
    public void setContext(WebContext context)
    {
        _context = context;
    }

    /** @since 4.0 */
    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    /** @since 4.0 */
    public void setDigestSource(ResourceDigestSource md5Source)
    {
        _digestSource = md5Source;
    }

    /** @since 4.0 */
    public void setRequest(WebRequest request)
    {
        _request = request;
    }
}