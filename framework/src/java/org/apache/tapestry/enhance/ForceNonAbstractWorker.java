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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;

/**
 * A worker that must be last; its job is to detect abstract classes that have no other
 * enhancements, and force an enhancement (so that a non-abstract subclass is generated). This is to
 * answer to the common problem of declaring a page abstract and not defining any properties.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ForceNonAbstractWorker implements EnhancementWorker
{

    public void performEnhancement(EnhancementOperation op)
    {
        Class baseClass = op.getBaseClass();

        if (!Modifier.isAbstract(baseClass.getModifiers()))
            return;

        if (!op.hasEnhancements())
            op.forceEnhancement();
    }
}