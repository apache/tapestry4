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

package tutorial.pagetypes;

import net.sf.tapestry.*;
import net.sf.tapestry.engine.SimpleEngine;
import net.sf.tapestry.util.prop.OgnlUtils;

import javax.servlet.ServletException;

/**
 * Provides the ability to handle an exception in a different manner
 */
public class PageTypesEngine extends SimpleEngine {
    private static final String NEW_EXCEPTION_PAGE = "NewException";

    protected void activateExceptionPage(IRequestCycle cycle, ResponseOutputStream output, Throwable cause)
            throws ServletException {
        RequestCycleException cycleE = (RequestCycleException)cause;
        if (cycleE.getRootCause() instanceof MyException) {
            handleNewException(cycle, output, cycleE.getRootCause());
        } else {
            super.activateExceptionPage(cycle, output, cause);
        }
    }

    /**
     * This standard exception handling block is what is found in
     * the SimpleEngine. This is not normal. Usually, if you are overriding
     * the Exception pae, you just have to provide a new page - not override the engine
     * to do it. This is done only because I want to show both the standard and new
     * exception pages in the same application.
     */
    protected void handleNewException(IRequestCycle cycle, ResponseOutputStream output, Throwable cause) throws ServletException {
        try {
            IPage exceptionPage = cycle.getPage(NEW_EXCEPTION_PAGE);
            OgnlUtils.set("exception", getResourceResolver(), exceptionPage, cause);
            cycle.setPage(exceptionPage);
            renderResponse(cycle, output);
        } catch (Throwable ex) {
            // Worst case scenario.  The exception page itself is broken, leaving
            // us with no option but to write the cause to the output.
            reportException(Tapestry.getString("AbstractEngine.unable-to-process-client-request"), cause);

            // Also, write the exception thrown when redendering the exception
            // page, so that can get fixed as well.
            reportException(Tapestry.getString("AbstractEngine.unable-to-present-exception-page"), ex);
            // And throw the exception.
            throw new ServletException(ex.getMessage(), ex);
        }
    }
}
