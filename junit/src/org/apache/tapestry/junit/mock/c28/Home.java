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

package org.apache.tapestry.junit.mock.c28;

import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

public class Home extends BasePage
{
    public void changeLocale(IRequestCycle cycle)
    {
        Object[] params = cycle.getServiceParameters();
        String pageName = (String) params[0];
        String localeName = (String) params[1];
        
        String[] localeParts = new String[] { "", "", "" };
        StringTokenizer tokenizer = new StringTokenizer(localeName, "_");
        for (int i = 0; i < 3 && tokenizer.hasMoreTokens(); i++) {
            localeParts[i] = tokenizer.nextToken();
        }
        
        Locale locale = new Locale(localeParts[0], localeParts[1], localeParts[2]);
        cycle.getEngine().setLocale(locale);
        cycle.activate(pageName);
    }
}
