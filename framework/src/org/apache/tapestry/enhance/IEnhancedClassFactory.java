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

/**
 *  An interface defining the factory for creation of new objects representing 
 *  an enhanced class. This object is used essentially as a singleton -- there is
 *  typically only one instance of it in the system. Common functionality, such as
 *  caches, can be stored here.
 * 
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public interface IEnhancedClassFactory
{
    void reset();
    IEnhancedClass createEnhancedClass(String className, Class parentClass);
}
