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

package org.apache.tapestry.link;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ExternalServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * A component for creating a link to {@link org.apache.tapestry.IExternalPage}using the
 * {@link org.apache.tapestry.engine.ExternalService}. [ <a
 * href="../../../../../ComponentReference/ExternalLink.html">Component Reference </a>]
 * 
 * @see org.apache.tapestry.IExternalPage
 * @see org.apache.tapestry.engine.ExternalService
 * @author Malcolm Edgar
 */

public abstract class ExternalLink extends AbstractLinkComponent
{
    public abstract IEngineService getExternalService();

    public ILink getLink(IRequestCycle cycle)
    {
        Object[] serviceParameters = DirectLink.constructServiceParameters(getParameters());

        ExternalServiceParameter esp = new ExternalServiceParameter(getTargetPage(),
                serviceParameters);

        return getExternalService().getLink(cycle, esp);
    }

    public abstract Object getParameters();

    public abstract String getTargetPage();
}