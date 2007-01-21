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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.io.GzipUtil;
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
    
    static final DateFormat CACHED_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    
    /**
     * Defaults MIME types, by extension, used when the servlet container doesn't provide MIME
     * types. ServletExec Debugger, for example, fails to provide these.
     */

    private static final Map _mimeTypes;

    static
    {
        _mimeTypes = new HashMap(17);
        _mimeTypes.put("css", "text/css");
        _mimeTypes.put("gif", "image/gif");
        _mimeTypes.put("jpg", "image/jpeg");
        _mimeTypes.put("jpeg", "image/jpeg");
        _mimeTypes.put("png", "image/png");
        _mimeTypes.put("htm", "text/html");
        _mimeTypes.put("html", "text/html");
    }
    
    /** Represents a month of time in seconds. */
    private static final long MONTH_SECONDS = 60 * 60 * 24 * 30;
    
    private Log _log;
    
    /** @since 4.0 */
    private ClassResolver _classResolver;

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

    /** @since 4.1 */
    private ResourceMatcher _unprotectedMatcher;
    
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

    private final long _expireTime = _startupTime + 365 * 24 * 60 * 60 * 1000L;

    /** @since 4.0 */

    private RequestExceptionReporter _exceptionReporter;
    
    private Map _cache = new HashMap();
    
    /**
     * Builds a {@link ILink}for a {@link PrivateAsset}.
     * <p>
     * A single parameter is expected, the resource path of the asset (which is expected to start
     * with a leading slash).
     */

    public ILink getLink(boolean post, Object parameter)
    {
        Defense.isAssignable(parameter, String.class, "parameter");

        String path = (String) parameter;
        
        String digest = null;
        
        if(!_unprotectedMatcher.containsResource(path))
            digest = _digestSource.getDigestForResource(path);
        
        Map parameters = new TreeMap(new AssetComparator());
        
        parameters.put(ServiceConstants.SERVICE, getName());
        parameters.put(PATH, path);
        
        if (digest != null)
            parameters.put(DIGEST, digest);
        
        // Service is stateless, which is the exception to the rule.
        
        return _linkFactory.constructLink(this, post, parameters, false);
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
            if (dotx > -1) {
                String key = path.substring(dotx + 1).toLowerCase();
                result = (String) _mimeTypes.get(key);
            }
            
            if (result == null)
                result = "text/plain";
        }

        return result;
    }
    
    /**
     * Retrieves a resource from the classpath and returns it to the client in a binary output
     * stream.
     */

    public void service(IRequestCycle cycle) throws IOException
    {
        String path = cycle.getParameter(PATH);
        String md5Digest = cycle.getParameter(DIGEST);
        boolean checkDigest = !_unprotectedMatcher.containsResource(path);
        
        URLConnection resourceConnection = null;
        
        try
        {
            if (checkDigest
                    && !_digestSource.getDigestForResource(path).equals(md5Digest))
            {
                _response.sendError(HttpServletResponse.SC_FORBIDDEN, AssetMessages
                        .md5Mismatch(path));
                return;
            }
            
            // If they were vended an asset in the past then it must be up-to date.
            // Asset URIs change if the underlying file is modified. (unless unprotected)
            
            if (checkDigest && _request.getHeader("If-Modified-Since") != null)
            {
                _response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
            
            URL resourceURL = _classResolver.getResource(translatePath(path));
            
            if (resourceURL == null) {
                _response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                _log.warn(AssetMessages.noSuchResource(path));
                return;
            }
            
            resourceConnection = resourceURL.openConnection();
            
            // check caching for unprotected resources
            
            if (!checkDigest && cachedResource(resourceConnection))
                return;
            
            writeAssetContent(cycle, path, resourceConnection);
        }
        catch (Throwable ex)
        {
            _exceptionReporter.reportRequestException(AssetMessages.exceptionReportTitle(path), ex);
        } finally {
            resourceConnection = null;
        }

    }

    /**
     * Utility that helps to resolve css file relative resources included
     * in a css temlpate via "url('../images/foo.gif')" or fix paths containing 
     * relative resource ".." style notation.
     * 
     * @param path The incoming path to check for relativity.
     * @return The path unchanged if not containing a css relative path, otherwise
     *          returns the path without the css filename in it so the resource is resolvable
     *          directly from the path.
     */
    String translatePath(String path)
    {
        if (path == null) 
            return null;
        
        String ret = FilenameUtils.normalize(path);
        ret = FilenameUtils.separatorsToUnix(ret);
        
        return ret;
    }
    
    /**
     * Checks if the resource contained within the specified URL 
     * has a modified time greater than the request header value
     * of <code>If-Modified-Since</code>. If it doesn't then the 
     * response status is set to {@link HttpServletResponse#SC_NOT_MODIFIED}.
     * 
     * @param resourceURL Resource being checked
     * @return True if resource should be cached and response header was set.
     * @since 4.1
     */
    
    boolean cachedResource(URLConnection resourceURL)
    {
        // even if it doesn't exist in header the value will be -1, 
        // which means we need to write out the contents of the resource
        
        String header = _request.getHeader("If-Modified-Since");
        long modify = -1;
        
        try {
            if (header != null)
                modify = CACHED_FORMAT.parse(header).getTime();
        } catch (ParseException e) { e.printStackTrace(); }
        
        if (resourceURL.getLastModified() > modify)
            return false;
        
        _response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        
        return true;
    }
    
    /** @since 2.2 */

    private void writeAssetContent(IRequestCycle cycle, String resourcePath, URLConnection resourceConnection) 
    throws IOException
    {
        // Getting the content type and length is very dependant
        // on support from the application server (represented
        // here by the servletContext).
        
        String contentType = getMimeType(resourcePath);

        long lastModified = resourceConnection.getLastModified();
        if (lastModified <= 0)
            lastModified = _startupTime;
        
        _response.setDateHeader("Last-Modified", lastModified);
        
        // write out expiration/cache info

        _response.setDateHeader("Expires", _expireTime);
        _response.setHeader("Cache-Control", "public, max-age=" + (MONTH_SECONDS * 3));
        
        // ie won't cache javascript with etag attached
        if (_request.getHeader("User-Agent") != null 
                && _request.getHeader("User-Agent").indexOf("MSIE") < 0 
                || contentType.indexOf("javascript") < 0)
            _response.setHeader("ETag", String.valueOf(resourcePath.hashCode()));
            
        
        // Set the content type. If the servlet container doesn't
        // provide it, try and guess it by the extension.
        
        if (contentType == null || contentType.length() == 0)
            contentType = getMimeType(resourcePath);
        
        byte[] data = getAssetData(cycle, resourcePath, resourceConnection, contentType);
        
        // force image(or other) caching when detected, esp helps with ie related things
        // see http://mir.aculo.us/2005/08/28/internet-explorer-and-ajax-image-caching-woes
        
        _response.setContentLength(data.length);
        
        OutputStream output = _response.getOutputStream(new ContentType(contentType));
        
        output.write(data);
    }
    
    byte[] getAssetData(IRequestCycle cycle, String resourcePath,
            URLConnection resourceConnection, String contentType) 
    throws IOException
    {
        InputStream input = null;

        try {
            
            CachedAsset cache = null;
            byte[] data = null;
            
            // check cache first
            
            if (_cache.get(resourcePath) != null) {
                
                cache = (CachedAsset)_cache.get(resourcePath);
                
                if (cache.getLastModified() < resourceConnection.getLastModified())
                    cache.clear(resourceConnection.getLastModified());
                
                data = cache.getData();
            } else {
                
                cache = new CachedAsset(resourcePath, resourceConnection.getLastModified(), null, null);
                
                _cache.put(resourcePath, cache);
            }
            
            if (data == null) {
                
                input = resourceConnection.getInputStream();
                data = IOUtils.toByteArray(input);
                
                cache.setData(data);
            }
            
            // compress javascript responses when possible
            
            if (GzipUtil.shouldCompressContentType(contentType) && GzipUtil.isGzipCapable(_request)) {
                
                if (cache.getGzipData() == null) {
                    
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    GZIPOutputStream gzip = new GZIPOutputStream(bo);
                    
                    gzip.write(data);
                    gzip.close();
                    
                    data = bo.toByteArray();
                    cache.setGzipData(data);
                } else {
                    
                    data = cache.getGzipData();
                }
                
                _response.setHeader("Content-Encoding", "gzip");
            }
            
            return data;
            
        } finally {
            
            if (input != null) {
                IOUtils.closeQuietly(input);
                input = null;
            }
        }
    }
    
    /** @since 4.0 */

    public void setExceptionReporter(RequestExceptionReporter exceptionReporter)
    {
        _exceptionReporter = exceptionReporter;
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
    
    /** @since 4.1 */
    public void setUnprotectedMatcher(ResourceMatcher matcher)
    {
        _unprotectedMatcher = matcher;
    }
    
    public void setLog(Log log)
    {
        _log = log;
    }
}
