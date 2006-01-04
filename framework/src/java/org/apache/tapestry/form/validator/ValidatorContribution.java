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

package org.apache.tapestry.form.validator;

/**
 * Contains information contributed to the tapestry.form.validator.Validators configuration point.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ValidatorContribution
{
    private boolean _configurable = true;

    private String _name;

    private Class _validatorClass;

    public String getName()
    {
        return _name;
    }

    public Class getValidatorClass()
    {
        return _validatorClass;
    }

    public boolean isConfigurable()
    {
        return _configurable;
    }

    public void setConfigurable(boolean configurable)
    {
        _configurable = configurable;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setValidatorClass(Class validatorClass)
    {
        _validatorClass = validatorClass;
    }
}
