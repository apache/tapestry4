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

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.spec.IExtensionSpecification;

/**
 * Used to hold data about a &lt;configure&gt; element while it is being parsed.
 *
 * @author Howard Lewis Ship
 */
class ExtensionConfigurationSetter extends BaseLocatable
{
    private IExtensionSpecification _extension;
    private String _propertyName;
    private ConfigureValueConverter _converter;
    private String _value;

    ExtensionConfigurationSetter(
        IExtensionSpecification extension,
        String propertyName,
        ConfigureValueConverter converter,
        String value)
    {
        _extension = extension;
        _propertyName = propertyName;
        _converter = converter;
        _value = value;
    }

    String getValue()
    {
        return _value;
    }

    void apply(String value)
    {
        Object objectValue = _converter.convert(value, getLocation());

        _extension.addConfiguration(_propertyName, objectValue);
    }

}
