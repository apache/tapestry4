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

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 *
 */
public abstract class BaseEnhancedClass implements IEnhancedClass
{

    /**
     *  List of {@link IEnhancer}.
     * 
     **/
    private List _enhancers;

    protected List getEnhancers()
    {
        return _enhancers;
    }

    public void addEnhancer(IEnhancer enhancer)
    {
        if (_enhancers == null)
            _enhancers = new ArrayList();

        _enhancers.add(enhancer);
    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClass#hasModifications()
     */
    public boolean hasModifications()
    {
        return _enhancers != null && !_enhancers.isEmpty();
    }

    public void performEnhancement()
    {
        List enhancers = getEnhancers();

        if (enhancers == null)
            return;

        int count = enhancers.size();

        for (int i = 0; i < count; i++)
        {
            IEnhancer enhancer = (IEnhancer) enhancers.get(i);

            enhancer.performEnhancement(this);
        }
    }

}
