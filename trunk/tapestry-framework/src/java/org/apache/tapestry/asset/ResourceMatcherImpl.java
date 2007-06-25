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
package org.apache.tapestry.asset;

import java.util.List;

import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.util.RegexpMatcher;


/**
 * Implementation of {@link ResourceMatcher}.
 * 
 */
public class ResourceMatcherImpl implements ResetEventListener, ResourceMatcher {
    
    /** regexp matcher engine. */
    protected RegexpMatcher _matcher;
    /** Resource match configuration regexp strings. */
    protected List _contributions;
    
    /** no args constructor. */
    public ResourceMatcherImpl() { }
    
    /**
     * Invoked by hivemind by default to initialize
     * service.
     */
    public void initializeService() 
    {
        _matcher = new RegexpMatcher();
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized void resetEventDidOccur()
    {
        _matcher.clear();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean containsResource(String path)
    {
        if (_contributions == null || _contributions.size() < 1 || path == null)
            return false;
        
        for (int i = 0; i < _contributions.size(); i++) {
            String pattern = (String)_contributions.get(i);
            
            if (_matcher.contains(pattern, path))
                return true;
        }
        
        return false;
    }
    
    /**
     * The set of contributed regexp strings that will positively
     * match incoming path strings for this matcher.
     * @param contributions
     */
    public void setContributions(List contributions)
    {
        this._contributions = contributions;
    }
}
