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
package org.apache.tapestry.util.io;

import org.apache.tapestry.TestBase;
import org.apache.tapestry.web.WebRequest;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests functionality of {@link GzipUtil} .
 *
 * @author jkuhnert
 */
@Test
public class TestGzipUtil extends TestBase {
    
    // for more see http://en.wikipedia.org/wiki/User_agent
    static final String MSIE_5_SUNOS = "Mozilla/4.0 (compatible; MSIE 5.0; SunOS 5.9 sun4u; X11)";
    static final String MSIE_5_5 = "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)";
    static final String MSIE_6 = "Mozilla/4.0 (compatible; MSIE 6.0; MSN 2.5; Windows 98)";
    static final String MSIE_6_SP2 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";
    static final String MSIE_7_BETA_1 = "Mozilla/4.0 (compatible; MSIE 7.0b; Win32)";
    
    static final String MOZ_1_5 = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.0.2) Gecko/20060308 Firefox/1.5.0.2";
    static final String MOZ_2_WIN98 = "Mozilla/5.0 (Windows; U; Win98; en-US; rv:1.8.1) Gecko/20061010 Firefox/2.0";
    
    static final String OPERA_7 = "Opera/7.23 (Windows 98; U) [en]";
    
    static final String SAFARI_125 = "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko)";
    static final String SAFARI_204 = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3";
    
    public void test_Gzip_Capable()
    {
        WebRequest request = newMock(WebRequest.class);
        checkOrder(request, false);
        
        expect(request.getHeader("Accept-Encoding")).andReturn("gzip").anyTimes();
        
        expect(request.getHeader("User-Agent")).andReturn(MSIE_5_SUNOS);
        expect(request.getHeader("User-Agent")).andReturn(MSIE_5_5);
        expect(request.getHeader("User-Agent")).andReturn(MSIE_6);
        
        expect(request.getHeader("User-Agent")).andReturn(MSIE_6_SP2);
        expect(request.getHeader("User-Agent")).andReturn(MSIE_7_BETA_1);
        
        expect(request.getHeader("User-Agent")).andReturn(MOZ_1_5);
        expect(request.getHeader("User-Agent")).andReturn(MOZ_2_WIN98);
        expect(request.getHeader("User-Agent")).andReturn(OPERA_7);
        expect(request.getHeader("User-Agent")).andReturn(SAFARI_125);
        expect(request.getHeader("User-Agent")).andReturn(SAFARI_204);
        
        replay();
        
        assertFalse(GzipUtil.isGzipCapable(request));
        assertFalse(GzipUtil.isGzipCapable(request));
        assertFalse(GzipUtil.isGzipCapable(request));
        
        assertTrue(GzipUtil.isGzipCapable(request));
        assertTrue(GzipUtil.isGzipCapable(request));
        assertTrue(GzipUtil.isGzipCapable(request));
        assertTrue(GzipUtil.isGzipCapable(request));
        assertTrue(GzipUtil.isGzipCapable(request));
        assertTrue(GzipUtil.isGzipCapable(request));
        assertTrue(GzipUtil.isGzipCapable(request));
        
        verify();
    }
    
    public void test_Compress_Content_Type()
    {
        assertFalse(GzipUtil.shouldCompressContentType(null));
        assertTrue(GzipUtil.shouldCompressContentType("javascript"));
        assertTrue(GzipUtil.shouldCompressContentType("html"));
        assertTrue(GzipUtil.shouldCompressContentType("css"));
        
        assertFalse(GzipUtil.shouldCompressContentType("jpeg"));
        assertFalse(GzipUtil.shouldCompressContentType("image"));
        assertFalse(GzipUtil.shouldCompressContentType("png"));
    }
}
