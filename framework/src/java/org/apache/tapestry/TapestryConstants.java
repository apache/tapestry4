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

package org.apache.tapestry;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TapestryConstants
{
    /**
     * The name ("Home") of the default page presented when a user first accesses the application.
     * 
     * @see org.apache.tapestry.engine.HomeService
     * @deprecated To be removed in 3.2.
     */

    public static final String HOME_PAGE = "Home";

    /**
     * The name ("Exception") of the page used for reporting exceptions.
     * <p>
     * Such a page must have a writable JavaBeans property named 'exception' of type
     * <code>java.lang.Throwable</code>.
     * 
     * @deprecated To be removed in 3.2.
     */

    public static final String EXCEPTION_PAGE = "Exception";

    /**
     * The name ("StaleLink") of the page used for reporting stale links.
     * <p>
     * The page must implement a writeable JavaBeans proeprty named 'message' of type
     * <code>String</code>.
     */

    public static final String STALE_LINK_PAGE = "StaleLink";

    /**
     * The name ("StaleSession") of the page used for reporting state sessions.
     */

    public static final String STALE_SESSION_PAGE = "StaleSession";

    /**
     * Name of the cookie written to the client web browser to identify the locale.
     */

    public static final String LOCALE_COOKIE_NAME = "org.apache.tapestry.locale";

    /**
     * Name of the meta data property used as a default for page class names.
     */

    public static final String PAGE_CLASS_NAME = "org.apache.tapestry.default-page-class";

    private TapestryConstants()
    {
    }
}