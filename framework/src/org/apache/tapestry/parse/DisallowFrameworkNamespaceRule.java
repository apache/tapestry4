//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  Special purpose rule that simple validates that a library does
 *  not use the reserved framework namespace.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class DisallowFrameworkNamespaceRule extends AbstractSpecificationRule
{

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String id = getValue(attributes, "id");

        if (id.equals(INamespace.FRAMEWORK_NAMESPACE))
            throw new DocumentParseException(
                Tapestry.format(
                    "SpecificationParser.framework-library-id-is-reserved",
                    INamespace.FRAMEWORK_NAMESPACE),
                getResourceLocation());
    }

}
