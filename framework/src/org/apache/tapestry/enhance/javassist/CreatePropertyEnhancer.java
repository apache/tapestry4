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

package org.apache.tapestry.enhance.javassist;

import javassist.CtClass;

import org.apache.tapestry.enhance.IEnhancedClass;
import org.apache.tapestry.enhance.IEnhancer;

/**
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class CreatePropertyEnhancer implements IEnhancer
{
    private String _propertyName;
    private CtClass _propertyType;
    private boolean _persistent;
    private String _readMethodName;

    public CreatePropertyEnhancer(String propertyName, CtClass propertyType)
    {
        this(propertyName, propertyType, null, false);
    }

    public CreatePropertyEnhancer(
        String propertyName,
        CtClass propertyType,
        String readMethodName,
        boolean persistent)
    {
        _propertyName = propertyName;
        _propertyType = propertyType;
        _readMethodName = readMethodName;
        _persistent = persistent;
    }

    public void performEnhancement(IEnhancedClass enhancedClass)
    {
        String fieldName = "_$" + _propertyName;

        EnhancedClass jaEnhancedClass = (EnhancedClass) enhancedClass;
        ClassFabricator classFabricator = jaEnhancedClass.getClassFabricator(); 

        classFabricator.createField(_propertyType, fieldName);
        classFabricator.createPropertyAccessor(_propertyType, fieldName, _propertyName, _readMethodName);
        classFabricator.createPropertyMutator(_propertyType, fieldName, _propertyName, _persistent);
    }

}
