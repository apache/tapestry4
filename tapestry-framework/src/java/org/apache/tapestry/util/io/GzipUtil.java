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

import org.apache.tapestry.web.WebRequest;


/**
 * Encapsulates logic related to various gzip compression schemes and their rules
 * as they apply to different browsers.
 *
 * @author jkuhnert
 */
public final class GzipUtil
{
    private static final float MIN_IE_VERSION = 7.0f;

    private static final String MSIE_6_COMPATIBLE_STRING = "SV1";

    /* defeat instantiation */
    private GzipUtil() { }

    /**
     * Determines if gzip compression is appropriate/possible based on the User Agent and 
     * other limiting factors. IE versions &lt; 6.1 are known to not work with gzip compression reliably. 
     *
     * @return True, if this request can be served in gzip format. False otherwise.
     */
    public static boolean isGzipCapable(WebRequest request)
    {
        String encoding = request.getHeader("Accept-Encoding");
        if (encoding == null || encoding.indexOf("gzip") < 0)
            return false;

        // Handle IE specific hacks

        String userAgent = request.getHeader("User-Agent");
        int ieIndex = (userAgent != null) ? userAgent.indexOf("MSIE") : -1;
        if (ieIndex > -1) {

            float version = -1;

            try {
                version = Float.parseFloat(userAgent.substring(ieIndex + 4, ieIndex + 8));
            } catch (NumberFormatException nf) {nf.printStackTrace();}

            if (version >= MIN_IE_VERSION)
                return true;

            if (userAgent.indexOf(MSIE_6_COMPATIBLE_STRING) > -1)
                return true;

            // else false

            return false;
        }

        return true;
    }

    /**
     * Based on the given type of content, determines if compression is appropriate. The biggest
     * thing it does is make sure that image content isn't compressed as that kind of content
     * is already compressed fairly well.
     *
     * @param contentType
     *          The content type to check. (ie "text/javascript","text/html", etc..)
     *
     * @return True if compression is appropriate for the content specified, false otherwise.
     */
    public static boolean shouldCompressContentType(String contentType)
    {
        if (contentType == null)
            return false;

        return contentType.indexOf("javascript") > -1
               || contentType.indexOf("css") > -1
               || contentType.indexOf("html") > -1
               || contentType.indexOf("text") > -1;
    }
}
