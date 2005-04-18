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

package org.apache.tapestry.error;

import org.apache.tapestry.IRequestCycle;

/**
 * Invoked by the {@link org.apache.tapestry.IEngine}&nbsp;if there's an uncaught exception
 * (checked or runtime) processing a request. The ExceptionPresenter is responsible for presenting a
 * exception message (or description) to the user. The default implementation activates the
 * "Exception" page, but it is common to override this to do something application specific
 * (typically, return to the Home page and display an error message there). This service also
 * provides a good hook for creating a server-side log of exceptions.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see RequestExceptionReporter
 */
public interface ExceptionPresenter
{
    /**
     * Report the exception and provide some response to the user in lieu of the expected result
     * page.
     * 
     * @param cycle
     *            the current request cycle
     * @param cause
     *            the exception that was caught
     */

    public void presentException(IRequestCycle cycle, Throwable cause);
}