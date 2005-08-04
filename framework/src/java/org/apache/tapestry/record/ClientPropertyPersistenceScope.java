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

package org.apache.tapestry.record;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;

/**
 * Service tapestry.persist.ClientPropertyPersistenceScope. Determines whether a particular property
 * needs to be persisted or not.
 * 
 * @author Mindbridge
 * @since 4.0
 * @see org.apache.tapestry.record.ClientPropertyPersistenceStrategy
 */
public interface ClientPropertyPersistenceScope
{
    /**
     * Determines whether state should be encoded for the request.
     * 
     * @param encoding
     *            identifies the service, URL and base set of parameters
     * @param cycle
     *            current request
     * @param pageName
     *            the page for which data is potentially to be encoded
     * @param data
     * @return true if state should be encoded into the encoding, false otherwise
     */

    public boolean shouldEncodeState(ServiceEncoding encoding, IRequestCycle cycle,
            String pageName, PersistentPropertyData data);

    /**
     * Constructs a parameter name for a particular page name. The parameter name can be recognized
     * (in a later request) by the {@link #isParameterForScope(String)} method.
     * 
     * @param pageName
     *            the name of the page for which a corresponding parameter name should be generated.
     * @returns a query parameter name that identifies the page and this client persistence scope.
     */

    public String constructParameterName(String pageName);

    /**
     * Checks a parameter to see if it was the result of {@link #constructParameterName(String)} for
     * this persistence scope.
     * 
     * @param parameterName
     *            a query parameter name
     * @return true if the parameterName was genereted (i.e., is properly prefixed) by this scope,
     *         false otherwise.
     */

    public boolean isParameterForScope(String parameterName);

    /**
     * Extracts a page name from a query parameter name.
     * 
     * @param parameterName
     *            the paramter name, for which {@link #isParameterForScope(String) must return true
     * @return the name of the page
     */
    public String extractPageName(String parameterName);
}
