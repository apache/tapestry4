// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import org.apache.hivemind.service.ClassFab;
import org.apache.tapestry.Defense;
import org.apache.tapestry.Tapestry;

/**
 * Adds a transient or persistent property to a class.
 * 
 * @author Mindbridge
 * @since 3.0
 */
public class CreatePropertyEnhancer implements IEnhancer
{
    private String _propertyName;

    private Class _propertyType;

    private boolean _persistent;

    private String _readMethodName;

    public CreatePropertyEnhancer(String propertyName, Class propertyType)
    {
        this(propertyName, propertyType, CreateAccessorUtils.buildMethodName("get", propertyName),
                false);
    }

    public CreatePropertyEnhancer(String propertyName, Class propertyType, String readMethodName,
            boolean persistent)
    {
        Defense.notNull(propertyName, "propertyName");
        Defense.notNull(propertyType, "propertyType");
        Defense.notNull(readMethodName, "readMethodName");

        _propertyName = propertyName;
        _propertyType = propertyType;
        _readMethodName = readMethodName;
        _persistent = persistent;
    }

    public void performEnhancement(ClassFab classFab)
    {
        String fieldName = "_$" + _propertyName;

        classFab.addField(fieldName, _propertyType);

        CreateAccessorUtils.createPropertyAccessor(
                classFab,
                _propertyType,
                fieldName,
                _propertyName,
                _readMethodName);

        CreateAccessorUtils.createPropertyMutator(
                classFab,
                _propertyType,
                fieldName,
                _propertyName,
                _persistent);
    }
}