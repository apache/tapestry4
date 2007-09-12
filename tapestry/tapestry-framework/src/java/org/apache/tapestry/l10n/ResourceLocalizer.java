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

package org.apache.tapestry.l10n;

import java.util.Locale;

import org.apache.hivemind.Resource;

/**
 * An interface that defines how to localize a particular resource. A default implementation will
 * use {@link org.apache.hivemind.Resource#getLocalization(java.util.Locale)}, but this interface
 * (and associated chain of command configuration point) allows for alternative approaches to
 * localizing a resource.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ResourceLocalizer
{
    /**
     * Finds and returns a localization of a root resource for the indicated locale.
     * 
     * @param rootResource
     *            the base resource for which a localization is needed
     * @param locale
     *            the locale to find a localization for
     * @return the localized version of the resource, or null if not found
     */
    Resource findLocalization(Resource rootResource, Locale locale);
}
