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

import org.apache.commons.beanutils.MethodUtils;
import org.xml.sax.Attributes;

/**
 *  Connects a child object to a parent object using a named method.  The method
 *  takes two parameters: the name of the child object and the child object itself.
 *  The child object name is taken from an attribute.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ConnectChildRule extends AbstractSpecificationRule
{
    private String _methodName;
    private String _attributeName;

    private String _attributeValue;

    public ConnectChildRule(String methodName, String attributeName)
    {
        _methodName = methodName;
        _attributeName = attributeName;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        _attributeValue = getValue(attributes, _attributeName);

        // Check for null?
    }

    /**
     *  Performs the add.  This is done in <code>end()</code> to ensure
     *  that the child object (on top of the stack) is fully initialized.
     * 
     **/

    public void end(String namespace, String name) throws Exception
    {
        Object child = digester.peek();
        Object parent = digester.peek(1);

        MethodUtils.invokeMethod(parent, _methodName, new Object[] { _attributeValue, child });

        _attributeValue = null;
    }

}
