// Copyright 2005, 2006 The Apache Software Foundation
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
import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class SpecMessages
{

    private static final Messages MESSAGES = new MessageFormatter(SpecMessages.class);

    /** @since 4.1 */
    private SpecMessages()
    {
    }

    static String claimedProperty(String propertyName, Object existing)
    {
        return MESSAGES.format("claimed-property", propertyName, HiveMind.getLocationString(existing));
    }

    static String duplicateAsset(String name, IAssetSpecification previousAsset)
    {
        return MESSAGES.format("duplicate-asset", name, HiveMind.getLocationString(previousAsset));
    }

    static String duplicateParameter(String name, IParameterSpecification existing)
    {
        return MESSAGES.format("duplicate-parameter", name, HiveMind.getLocationString(existing));
    }

    static String duplicateBean(String name, IBeanSpecification spec)
    {
        return MESSAGES.format("duplicate-bean", name, HiveMind.getLocationString(spec));
    }

    static String duplicateProperty(String name, IPropertySpecification existing)
    {
        return MESSAGES.format("duplicate-property", name, HiveMind.getLocationString(existing));
    }

    static String duplicateComponent(String name, IContainedComponent existing)
    {
        return MESSAGES.format("duplicate-component", name, HiveMind.getLocationString(existing));
    }
}
