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

package org.apache.tapestry.parse;

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.bean.BindingBeanInitializer;
import org.apache.tapestry.spec.IBeanSpecification;

/**
 * Holds the data from a &lt;set-property&gt; element while the body is being parsed.
 * 
 * @author Howard Lewis Ship
 */
class BeanSetPropertySetter extends BaseLocatable
{
    private IBeanSpecification _beanSpecification;

    private BindingBeanInitializer _initializer;

    private String _bindingReference;

    private String _prefix;

    BeanSetPropertySetter(IBeanSpecification beanSpecification, BindingBeanInitializer initializer,
            String prefix, String expression)
    {
        _beanSpecification = beanSpecification;
        _initializer = initializer;
        _prefix = prefix;
        _bindingReference = expression;
    }

    void applyBindingReference(String bindingReference)
    {
        String fullBindingReference = _prefix == null ? bindingReference : _prefix
                + bindingReference;

        _initializer.setBindingReference(fullBindingReference);

        _beanSpecification.setLocation(getLocation());
        _beanSpecification.addInitializer(_initializer);
    }

    public String getBindingReference()
    {
        return _bindingReference;
    }

}