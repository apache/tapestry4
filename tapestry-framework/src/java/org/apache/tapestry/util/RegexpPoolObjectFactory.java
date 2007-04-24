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
package org.apache.tapestry.util;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.hivemind.util.Defense;


/**
 * Implementation of {@link BaseKeyedPoolableObjectFactory} for regexp patterns
 * that compiles incoming String regexp patterns into compiled {@link java.util.regex.Pattern}
 * objects.
 */
public class RegexpPoolObjectFactory extends BaseKeyedPoolableObjectFactory
{
    /**
     * {@inheritDoc}
     */
    public Object makeObject(Object pattern)
        throws Exception
    {
        Defense.notNull(pattern, "Regexp pattern");
        Defense.isAssignable(pattern, String.class, "Regexp pattern");
        
        String regexp = (String)pattern;

        return java.util.regex.Pattern.compile(regexp, java.util.regex.Pattern.COMMENTS | java.util.regex.Pattern.DOTALL);
    }

}
