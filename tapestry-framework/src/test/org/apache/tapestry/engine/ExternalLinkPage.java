// Copyright Jul 24, 2006 The Apache Software Foundation
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
package org.apache.tapestry.engine;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;


/**
 * Test class used by {@link ExternalServiceTest}.
 * 
 * @author jkuhnert
 */
public abstract class ExternalLinkPage extends BasePage implements IExternalPage
{
    
    /** 
     * {@inheritDoc}
     */
    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    {
    }

    public void validate(IRequestCycle cycle)
    {
        super.validate(cycle);
    }
}
