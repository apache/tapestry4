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

package org.apache.tapestry.pages;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.util.exception.ExceptionDescription;

/**
 * Default exception reporting page.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Exception extends BasePage implements PageDetachListener
{
    /** @since 4.1.4 */
    public abstract String getPagePackages();
    /** @since 4.1.4 */
    public abstract String getComponentPackages();

    /** Transient property. */
    public abstract void setExceptions(ExceptionDescription[] exceptions);

    public void setException(Throwable value)
    {
        ExceptionAnalyzer analyzer = new ExceptionAnalyzer();

        ExceptionDescription[] exceptions = analyzer.analyze(value);

        setExceptions(exceptions);
    }

    public boolean isDynamic()
    {
        return getRequestCycle().getResponseBuilder().isDynamic();
    }
    
    public String[] getPackages() {
        INamespace namespace = getRequestCycle().getInfrastructure().getSpecificationSource().getApplicationNamespace();
        String pages = namespace.getPropertyValue(getPagePackages());
        String comps = namespace.getPropertyValue(getComponentPackages());
        StringBuffer sb = new StringBuffer();
        if (pages!=null) 
            sb.append(pages);
        if (comps!=null)
            sb.append(",").append(comps);
        return TapestryUtils.split(sb.toString(), ',');
    }
}
