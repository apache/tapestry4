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

package org.apache.tapestry.services;

import java.util.Locale;

/**
 * Used to determine what is the client-specified locale, if any, for the current request. This may
 * be stored in the request as an HTTP Cookie, or may be interpolated from request headers.
 * Additionally, the "raw" value provided by the client may be filtered down.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface RequestLocaleManager
{
    /**
     * Determines the locale to use for processing the current request. The value is returned, but
     * also stored as {@link org.apache.tapestry.services.RequestGlobals#setIncomingLocale(Locale)}.
     */

    public Locale extractLocaleForCurrentRequest();
    
    /**
     * Stores the locale as a cookie for later use.
     */
    
    public void persistLocale(Locale locale);
}