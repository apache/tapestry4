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

package org.apache.tapestry.enhance;

import org.apache.tapestry.spec.InjectSpecification;

/**
 * A kind of enhancement worker dedicated to injection, based on the &lt;inject&gt; element of the
 * specification. There are different types of injection which match up to different implementations
 * of this interface (i.e., the Strategy pattern).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.spec.InjectSpecification
 * @see org.apache.tapestry.enhance.DispatchToInjectWorker
 */
public interface InjectEnhancementWorker
{
    /**
     * Perform the enhancement defined by the {@link org.apache.tapestry.spec.InjectSpecification}.
     * Thrown runtime exceptions are caught and reported by the invoker.
     */

    public void performEnhancement(EnhancementOperation op, InjectSpecification spec);

}
