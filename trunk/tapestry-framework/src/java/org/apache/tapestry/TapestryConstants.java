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
 * @since 4.0
 */
public final class TapestryConstants
{
    /**
     * Name of the cookie written to the client web browser to identify the locale.
     */

    public static final String LOCALE_COOKIE_NAME = "org.apache.tapestry.locale";

    /**
     * Name of the meta data property used as a default for page class names.
     */

    public static final String PAGE_CLASS_NAME = "org.apache.tapestry.default-page-class";

    /**
     * Name of meta-data property used to determine the default binding prefix. The meta default for
     * this is "ognl".
     */

    public static final String DEFAULT_BINDING_PREFIX_NAME = "org.apache.tapestry.default-binding-prefix";

    private TapestryConstants()
    {
    }
}
