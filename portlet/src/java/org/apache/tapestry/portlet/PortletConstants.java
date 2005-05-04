// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.portlet;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletConstants
{
    /**
     * Name of the render service, whose job is to render a page. The page to render is identified
     * in the {@link org.apache.tapestry.services.ServiceConstants#PAGE}&nbsp;parameter.
     */
    public static final String RENDER_SERVICE = "render";

    /**
     * Name of the exception service, which renders stored exception markup.
     */

    public static final String EXCEPTION_SERVICE = "exception";

    /**
     * Session attribute key used to store markup generated during an action request, so that it can
     * be displayed during a later render request.
     */

    public static final String PORTLET_EXCEPTION_MARKUP_ATTRIBUTE = "org.apache.tapestry.portlet.PortletExceptionMarkup";

    private PortletConstants()
    {
        // Prevent instantiation
    }
}