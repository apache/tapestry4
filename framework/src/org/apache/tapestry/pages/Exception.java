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

package org.apache.tapestry.pages;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.util.exception.ExceptionDescription;

/**
 *  Default exception reporting page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Exception extends BasePage
{
    private ExceptionDescription[] _exceptions;

    public void detach()
    {
        _exceptions = null;

        super.detach();
    }

    public ExceptionDescription[] getExceptions()
    {
        return _exceptions;
    }

    public void setException(Throwable value)
    {
        ExceptionAnalyzer analyzer;

        analyzer = new ExceptionAnalyzer();

        _exceptions = analyzer.analyze(value);
    }

}