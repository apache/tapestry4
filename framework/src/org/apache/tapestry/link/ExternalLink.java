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

package org.apache.tapestry.link;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;

/**
 *  A component for creating a link to {@link org.apache.tapestry.IExternalPage} using the
 * {@link org.apache.tapestry.engine.ExternalService}.
 *
 *  [<a href="../../../../../ComponentReference/ExternalLink.html">Component Reference</a>]
 *
 * @see org.apache.tapestry.IExternalPage
 * @see org.apache.tapestry.engine.ExternalService
 *
 * @author Malcolm Edgar
 * @version $Id$
 *
 **/

public abstract class ExternalLink extends AbstractLinkComponent
{
    public ILink getLink(IRequestCycle cycle)
    {
        return getLink(cycle, Tapestry.EXTERNAL_SERVICE, getServiceParameters());
    }

    private Object[] getServiceParameters()
    {
        Object[] pageParameters = DirectLink.constructServiceParameters(getParameters());
        String targetPage = getTargetPage();

        if (pageParameters == null)
            return new Object[] { targetPage };

        Object[] parameters = new Object[pageParameters.length + 1];

        parameters[0] = targetPage;

        System.arraycopy(pageParameters, 0, parameters, 1, pageParameters.length);

        return parameters;
    }

    public abstract Object getParameters();

    public abstract String getTargetPage();
}
