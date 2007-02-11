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

import static org.easymock.EasyMock.*;

import java.net.URLConnection;
import java.text.DateFormat;

import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.testng.annotations.Test;

import com.javaforge.tapestry.testng.TestBase;

import javax.servlet.http.HttpServletResponse;


/**
 * Tests functionality of {@link AssetService}. 
 *
 * @author jkuhnert
 */
@Test(sequential=true)
public class TestAssetService extends TestBase
{   
    
    public void test_Cached_Resource_Null_Modified()
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        
        AssetService service = new AssetService();
        service.setRequest(request);
        service.setLog(LogFactory.getLog("test"));
        
        URLConnection url = org.easymock.classextension.EasyMock.createMock(URLConnection.class);
        
        expect(request.getHeader("If-Modified-Since")).andReturn(null);
        expect(url.getLastModified()).andReturn(System.currentTimeMillis());
        
        replay();
        org.easymock.classextension.EasyMock.replay(url);
        
        assertFalse(service.cachedResource(url));
        
        verify();
        org.easymock.classextension.EasyMock.verify(url);
    }
    
    public void test_Cached_Resource_Malformed_Modified()
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        
        AssetService service = new AssetService();
        service.setRequest(request);
        service.setLog(LogFactory.getLog("test"));
        
        URLConnection url = org.easymock.classextension.EasyMock.createMock(URLConnection.class);
        
        expect(request.getHeader("If-Modified-Since")).andReturn("Woopedy woopedy");
        expect(url.getLastModified()).andReturn(System.currentTimeMillis());
        
        replay();
        org.easymock.classextension.EasyMock.replay(url);
        
        assertFalse(service.cachedResource(url));
        
        verify();
        org.easymock.classextension.EasyMock.verify(url);
    }
    
    public void test_Cached_Resource_Stale()
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        
        AssetService service = new AssetService();
        service.setRequest(request);
        service.setLog(LogFactory.getLog("test"));
        
        URLConnection url = org.easymock.classextension.EasyMock.createMock(URLConnection.class);
        
        expect(request.getHeader("If-Modified-Since")).andReturn("Sat, 29 Oct 1994 19:43:31 GMT");
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
        
        DateFormat format = null;
        
        try {
            
            format = (DateFormat) AssetService.CACHED_FORMAT_POOL.borrowObject();
            
            expect(request.getHeader("If-Modified-Since")).andReturn("Sat, 29 Oct 1994 19:43:31 GMT");
            expect(url.getLastModified()).andReturn(format.parse("Sat, 1 Dec 1991 19:43:31 GMT").getTime());
            
        } finally {
            if (format != null) {
                try { AssetService.CACHED_FORMAT_POOL.returnObject(format); } catch (Throwable t) {}
            }
        }
        
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        
        replay();
        org.easymock.classextension.EasyMock.replay(url);
        
        assertTrue(service.cachedResource(url));
        
        verify();
        org.easymock.classextension.EasyMock.verify(url);
    }
}
