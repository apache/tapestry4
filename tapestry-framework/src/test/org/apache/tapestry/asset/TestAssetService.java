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

import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TestBase;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.net.URLConnection;


/**
 * Tests functionality of {@link AssetService}. 
 *
 * @author jkuhnert
 */
@Test(sequential=true)
public class TestAssetService extends TestBase {
    
    public void test_Cached_Resource_Null_Modified()
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        
        AssetService service = new AssetService();
        service.setRequest(request);
        service.setLog(LogFactory.getLog("test"));

        expect(request.getDateHeader("If-Modified-Since")).andReturn(-1l);
        
        replay();

        assertFalse(service.cachedResource(null));
        
        verify();
    }
    
    public void test_Cached_Resource_Stale()
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        
        AssetService service = new AssetService();
        service.setRequest(request);
        service.setLog(LogFactory.getLog("test"));
        
        URLConnection url = org.easymock.classextension.EasyMock.createMock(URLConnection.class);

        long modifiedSince = System.currentTimeMillis() - 1000;

        expect(request.getDateHeader("If-Modified-Since")).andReturn(modifiedSince);
        expect(url.getLastModified()).andReturn(System.currentTimeMillis());
        
        replay();
        org.easymock.classextension.EasyMock.replay(url);
        
        assertFalse(service.cachedResource(url));
        
        verify();
        org.easymock.classextension.EasyMock.verify(url);
    }
    
    public void test_Cached_Resource_Good()
    throws Exception
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        WebResponse response = newMock(WebResponse.class);
        
        AssetService service = new AssetService();
        service.setRequest(request);
        service.setResponse(response);
        service.setLog(LogFactory.getLog("test"));
        
        URLConnection url = org.easymock.classextension.EasyMock.createMock(URLConnection.class);

        long lastModified = System.currentTimeMillis() - 4000;
        long modifiedSince = System.currentTimeMillis();

        expect(request.getDateHeader("If-Modified-Since")).andReturn(modifiedSince);
        expect(url.getLastModified()).andReturn(lastModified);
        
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        
        replay();
        org.easymock.classextension.EasyMock.replay(url);
        
        assertTrue(service.cachedResource(url));
        
        verify();
        org.easymock.classextension.EasyMock.verify(url);
    }

    public void test_ETag_Header_Response()
            throws Exception
    {
        String requestedResource = "/org/apache/tapestry/asset/tapestry-in-action.png";
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        WebResponse response = newMock(WebResponse.class);
        WebContext context = newMock(WebContext.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        ResourceMatcher matcher = newMock(ResourceMatcher.class);
        
        ClassResolver resolver = new DefaultClassResolver();
        URLConnection url = resolver.getResource(requestedResource).openConnection();

        AssetService service = new AssetService();
        service.setRequest(request);
        service.setResponse(response);
        service.setLog(LogFactory.getLog("test"));
        service.setUnprotectedMatcher(matcher);
        service.setClassResolver(resolver);
        service.setContext(context);

        expect(cycle.getParameter("path")).andReturn(requestedResource);
        expect(cycle.getParameter("digest")).andReturn(null);

        expect(matcher.containsResource(requestedResource)).andReturn(true);

        expect(request.getDateHeader("If-Modified-Since")).andReturn(-1L);
        expect(context.getMimeType(requestedResource)).andReturn("image/png");

        response.setDateHeader("Last-Modified", url.getLastModified());
        response.setDateHeader("Expires", service._expireTime);
        response.setHeader("Cache-Control", "public, max-age=" + (AssetService.MONTH_SECONDS * 3));

        expect(request.getHeader("User-Agent")).andReturn("Mozilla").anyTimes();

        response.setHeader("ETag", "W/\"" + url.getContentLength() + "-" + url.getLastModified() + "\"");
        response.setContentLength(url.getContentLength());

        expect(response.getOutputStream(new ContentType("image/png"))).andReturn(new ByteArrayOutputStream());

        replay();

        service.service(cycle);

        verify();        
    }

    public void test_Gzip_Response() throws Exception
    {
        String requestedResource = "/org/apache/tapestry/pages/Exception.css";
        WebResponse response = newMock(WebResponse.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        ClassResolver resolver = new DefaultClassResolver();
        URLConnection url = resolver.getResource(requestedResource).openConnection();

        AssetService service = newAssetServiceAllowingGzip(requestedResource, url, cycle, response, resolver);

        response.setHeader("Content-Encoding", "gzip");

        final int originalLength = url.getContentLength();
        response.setHeader(EasyMock.matches("ETag"),
                EasyMock.matches("W/\".*-" + url.getLastModified() + "\""));
        response.setContentLength(EasyMock.lt(originalLength));

        expect(response.getOutputStream(new ContentType("text/css"))).andReturn(new ByteArrayOutputStream());

        replay();

        service.service(cycle);

        verify();
    }

    public void test_Gzip_Disabled_Response() throws Exception
    {
        String requestedResource = "/org/apache/tapestry/pages/Exception.css";
        WebResponse response = newMock(WebResponse.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        ClassResolver resolver = new DefaultClassResolver();
        URLConnection url = resolver.getResource(requestedResource).openConnection();

        AssetService service = newAssetServiceAllowingGzip(requestedResource, url, cycle, response, resolver);
        service.setNeverGzip(true);

        final int originalLength = url.getContentLength();
        response.setHeader("ETag", "W/\"" + originalLength + "-" + url.getLastModified() + "\"");
        response.setContentLength(originalLength);

        expect(response.getOutputStream(new ContentType("text/css"))).andReturn(new ByteArrayOutputStream());

        replay();

        service.service(cycle);

        verify();
    }

    public void test_Invalid_Resource()
            throws Exception
    {
        String requestedResource = "/org/apache/tapestry/asset/tapestry-in-action-missing.png";
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        WebResponse response = newMock(WebResponse.class);
        WebContext context = newMock(WebContext.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        ResourceMatcher matcher = newMock(ResourceMatcher.class);
        ResourceDigestSource digestSource = newMock(ResourceDigestSource.class);
        RequestExceptionReporter exceptionReporter = newMock(RequestExceptionReporter.class);

        // digester throws exception for invalid resources
        expect(digestSource.getDigestForResource(requestedResource))
            .andThrow(new ApplicationRuntimeException("error"))
            .anyTimes();
        // in which case the exception reporter has to show them
        exceptionReporter.reportRequestException((String)EasyMock.anyObject(), (Throwable)EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();

        ClassResolver resolver = new DefaultClassResolver();

        AssetService service = new AssetService();
        service.setRequest(request);
        service.setResponse(response);
        service.setLog(LogFactory.getLog("test"));
        service.setUnprotectedMatcher(matcher);
        service.setClassResolver(resolver);
        service.setContext(context);
        service.setDigestSource(digestSource);
        service.setExceptionReporter(exceptionReporter);

        expect(cycle.getParameter("path")).andReturn(requestedResource);
        expect(cycle.getParameter("digest")).andReturn(null);

        expect(matcher.containsResource(requestedResource)).andReturn(false);

        // make sure that a 404 is sent - instead of an exception thrown
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        replay();

        service.service(cycle);

        verify();
    }

    private AssetService newAssetServiceAllowingGzip(String requestedResource, URLConnection url,
                                         IRequestCycle cycle, WebResponse response, ClassResolver resolver)
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        WebContext context = newMock(WebContext.class);
        ResourceMatcher matcher = newMock(ResourceMatcher.class);

        AssetService service = new AssetService();
        service.setRequest(request);
        service.setResponse(response);
        service.setLog(LogFactory.getLog("test"));
        service.setUnprotectedMatcher(matcher);
        service.setClassResolver(resolver);
        service.setContext(context);

        expect(cycle.getParameter("path")).andReturn(requestedResource);
        expect(cycle.getParameter("digest")).andReturn(null);

        expect(matcher.containsResource(requestedResource)).andReturn(true);

        expect(request.getDateHeader("If-Modified-Since")).andReturn(-1L);
        expect(context.getMimeType(requestedResource)).andReturn("text/css");

        response.setDateHeader("Last-Modified", url.getLastModified());
        response.setDateHeader("Expires", service._expireTime);
        response.setHeader("Cache-Control", "public, max-age=" + (AssetService.MONTH_SECONDS * 3));

        expect(request.getHeader("User-Agent")).andReturn("Mozilla").anyTimes();
        expect(request.getHeader("Accept-Encoding")).andReturn("gzip").anyTimes();
        return service;
    }
}
