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

package org.apache.tapestry;

import java.util.Locale;

import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.WebRequestServicer;

/**
 * Defines the core, session-persistant object used to run a Tapestry application for a single
 * client (each client will have its own instance of the engine).
 * <p>
 * The engine exists to provide core services to the pages and components that make up the
 * application. The engine is a delegate to the {@link ApplicationServlet}.
 * <p>
 * Starting in release 4.0, the engine is kept around only for compatibility (with release 3.0).
 * It's functions have been moved over into a collection of HiveMind services (or are in the process
 * of doing so).
 * 
 * @author Howard Lewis Ship
 */

public interface IEngine extends WebRequestServicer
{
    /**
     * Returns the locale for the engine. This locale is used when selecting templates and assets.
     */

    Locale getLocale();

    /**
     * Changes the engine's locale. Any subsequently loaded pages will be in the new locale (though
     * pages already loaded stay in the old locale). Generally, you should render a new page after
     * changing the locale, to show that the locale has changed.
     */

    void setLocale(Locale value);

    /**
     * Returns the encoding to be used to generate the responses and accept the requests.
     * 
     * @since 3.0
     */

    String getOutputEncoding();

    /**
     * Returns the {@link org.apache.tapestry.services.Infrastructure}&nbsp;object, a central
     * registry of key HiveMind services used by Tapestry.
     * 
     * @since 4.0
     */

    Infrastructure getInfrastructure();
}
