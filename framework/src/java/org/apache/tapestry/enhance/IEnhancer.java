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

package org.apache.tapestry.enhance;

import org.apache.hivemind.service.ClassFab;

/**
 *  Defines an object which may work with a 
 *  {@link org.apache.tapestry.enhance.ComponentClassFactory}
 *  to create an enhancement to a class.  These enhancements are
 *  typically in the form of adding new fields and methods.
 *
 *  @author Howard Lewis Ship
 *  @since 3.0
 */

public interface IEnhancer
{
    public void performEnhancement(ClassFab classFab);
}
