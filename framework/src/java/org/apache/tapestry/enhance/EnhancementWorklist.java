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
 *  This interface represents a class to be enhanced, and a list of
 *  enhancements to be performed.
 * 
 *  @author Mindbridge
 *  @since 3.0
 */
public interface EnhancementWorklist
{
    /**
     * Returns the name of the class being enhanced.
     */
    public String getClassName();

    /**
     * Returns true if there is a need for enhancement. In some cases, a class
     * can be used as-is, without enhancement.
     */
    public boolean hasModifications();

    /**
     * Adds another enhancement.
     */

    public void addEnhancer(IEnhancer enhancer);

    /**
     * Creates the enhanced class.
     */
    public Class createEnhancedSubclass();
}
