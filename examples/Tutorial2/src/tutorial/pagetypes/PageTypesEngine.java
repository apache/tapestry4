/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
