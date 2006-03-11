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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * Tests for {@link org.apache.tapestry.error.ExceptionPresenterImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestExceptionPresenter extends BaseErrorTestCase
{
    public abstract static class ExceptionFixture extends BasePage
    {
        public abstract void setException(Throwable exception);
    }

    public void testSuccess() throws Exception
    {
        Throwable cause = new IllegalArgumentException();
        IPage page = newPage();

        IRequestCycle cycle = newCycle("Exception", page);
        ResponseRenderer renderer = newRenderer(cycle, null);

        cycle.activate(page);

        replayControls();

        ExceptionPresenterImpl ep = new ExceptionPresenterImpl();
        ep.setExceptionPageName("Exception");
        ep.setResponseRenderer(renderer);

        ep.presentException(cycle, cause);

        verifyControls();

        assertSame(cause, page.getProperty("exception"));
    }

    public void testFailure() throws Exception
    {
        Throwable cause = new IllegalArgumentException();
        Throwable renderCause = new ApplicationRuntimeException("Some failure.");

        IPage page = newPage();

        IRequestCycle cycle = newCycle("Exception", page);
        ResponseRenderer renderer = newRenderer(cycle, renderCause);
        RequestExceptionReporter reporter = newReporter();

        cycle.activate(page);

        reporter.reportRequestException(ErrorMessages.unableToProcessClientRequest(cause), cause);
        reporter.reportRequestException(
                ErrorMessages.unableToPresentExceptionPage(renderCause),
                renderCause);

        replayControls();

        ExceptionPresenterImpl ep = new ExceptionPresenterImpl();
        ep.setExceptionPageName("Exception");
        ep.setResponseRenderer(renderer);
        ep.setRequestExceptionReporter(reporter);

        try
        {
            ep.presentException(cycle, cause);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertSame(renderCause, ex.getRootCause());
        }

        verifyControls();

        assertSame(cause, page.getProperty("exception"));
    }
}