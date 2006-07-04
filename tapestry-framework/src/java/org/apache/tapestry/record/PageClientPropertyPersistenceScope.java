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

package org.apache.tapestry.record;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;

/**
 * Defines the 'page' scope for persisting client properties. Persist the properties only if the
 * current page name is the same as that of the property.
 * 
 * @author Mindbridge
 * @since 4.0
 * @see org.apache.tapestry.record.ClientPropertyPersistenceScope
 */
public class PageClientPropertyPersistenceScope extends
        AbstractPrefixedClientPropertyPersistenceScope
{
    private IRequestCycle _requestCycle;

    public PageClientPropertyPersistenceScope()
    {
        super("state:");
    }

    /**
     * Returns true if the active page name matches the page for this property. This means that
     * <em>after a new page has been activated</em>, the state is discarded.
     */

    public boolean shouldEncodeState(ServiceEncoding encoding, String pageName,
            PersistentPropertyData data)
    {
        IPage page = _requestCycle.getPage();

        // TAPESTRY-701: if you try to generate a link using, say, page or external service,
        // from inside PageValidateListener.pageValidate(), then there may not be an active
        // page yet. Seems like the right thing to do is hold onto any properties until
        // we know what the active page is.  I know this one is going to cause a fight
        // since its not clear whether keeping or discarding is the right way to go.
        
        if (page == null)
            return true;

        return pageName.equals(page.getPageName());
    }

    public void setRequestCycle(IRequestCycle requestCycle)
    {
        _requestCycle = requestCycle;
    }

}
