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

import org.apache.hivemind.util.Defense;

/**
 * Base implementation of {@link org.apache.tapestry.record.ClientPropertyPersistenceScope} wherein
 * the scopes are recognized via a prefix on the page name to form the query parameter name.
 * Capiche?
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class AbstractPrefixedClientPropertyPersistenceScope implements
        ClientPropertyPersistenceScope
{
    private final String _prefix;

    public AbstractPrefixedClientPropertyPersistenceScope(String prefix)
    {
        Defense.notNull(prefix, "prefix");

        _prefix = prefix;
    }

    public String constructParameterName(String pageName)
    {
        return _prefix + pageName;
    }

    public boolean isParameterForScope(String parameterName)
    {
        return parameterName.startsWith(_prefix);
    }

    public String extractPageName(String parameterName)
    {
        return parameterName.substring(_prefix.length());
    }

}
