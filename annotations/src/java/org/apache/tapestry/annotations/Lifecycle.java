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

package org.apache.tapestry.annotations;

import org.apache.tapestry.spec.BeanLifecycle;

/**
 * Enum version of {@link org.apache.tapestry.spec.BeanLifecycle}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public enum Lifecycle {

    // Preferred this enum as a member of the Bean interface, but Clover couldn't handle that.

    NONE(BeanLifecycle.NONE), REQUEST(BeanLifecycle.REQUEST), PAGE(BeanLifecycle.PAGE), RENDER(
            BeanLifecycle.RENDER);

    private final BeanLifecycle _beanLifecycle;

    Lifecycle(BeanLifecycle beanLifecycle)
    {
        _beanLifecycle = beanLifecycle;
    }

    public BeanLifecycle getBeanLifecycle()
    {
        return _beanLifecycle;
    }

}
