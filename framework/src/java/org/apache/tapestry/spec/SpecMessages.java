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

package org.apache.tapestry.spec;

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class SpecMessages
{
    private static MessageFormatter _formatter = new MessageFormatter(SpecMessages.class,
            "SpecStrings");

    static String claimedProperty(String propertyName, Object existing)
    {
        return _formatter.format("claimed-property", propertyName, HiveMind
                .getLocationString(existing));
    }

    static String duplicateAsset(String name, IAssetSpecification previousAsset)
    {
        return _formatter
                .format("duplicate-asset", name, HiveMind.getLocationString(previousAsset));
    }

    static String duplicateParameter(String name, IParameterSpecification existing)
    {
        return _formatter.format("duplicate-parameter", name, HiveMind.getLocationString(existing));
    }

    static String duplicateBean(String name, IBeanSpecification spec)
    {
        return _formatter.format("duplicate-bean", name, HiveMind.getLocationString(spec));
    }

    static String duplicateProperty(String name, IPropertySpecification existing)
    {
        return _formatter.format("duplicate-property", name, HiveMind.getLocationString(existing));
    }

    static String duplicateComponent(String name, IContainedComponent existing)
    {
        return _formatter.format("duplicate-component", name, HiveMind.getLocationString(existing));
    }
}
