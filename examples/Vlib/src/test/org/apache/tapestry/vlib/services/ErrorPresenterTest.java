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

package org.apache.tapestry.vlib.services;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.Visit;

/**
 * Tests for {@link org.apache.tapestry.vlib.services.ErrorPresenterImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ErrorPresenterTest extends HiveMindTestCase
{

    protected void trainGetPage(IRequestCycle cycle, String pageName, IPage page)
    {
        cycle.getPage(pageName);
        setReturnValue(cycle, page);
    }

    protected void trainGetVisit(IEngine engine, Object visit)
    {
        engine.getVisit();
        setReturnValue(engine, visit);
    }

    protected void trainGetEngine(IRequestCycle cycle, IEngine engine)
    {
        cycle.getEngine();
        setReturnValue(cycle, engine);
    }

    public void testLoggedOut()
    {
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine();
        IErrorProperty page = newPage();

        trainGetEngine(cycle, engine);

        trainGetVisit(engine, null);

        trainGetPage(cycle, "Home", page);

        page.setError("An error.");

        cycle.activate(page);

        replayControls();

        new ErrorPresenterImpl().presentError("An error.", cycle);

        verifyControls();
    }

    private IErrorProperty newPage()
    {
        return (IErrorProperty) newMock(IErrorProperty.class);
    }

    private IEngine newEngine()
    {
        return (IEngine) newMock(IEngine.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    public void testLoggedIn()
    {

        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine();
        IErrorProperty page = newPage();
        Visit visit = (Visit) newMock(Visit.class);

        trainGetEngine(cycle, engine);

        trainGetVisit(engine, visit);

        visit.isUserLoggedIn();
        setReturnValue(visit, true);

        trainGetPage(cycle, "MyLibrary", page);

        page.setError("An error.");

        cycle.activate(page);

        replayControls();

        new ErrorPresenterImpl().presentError("An error.", cycle);

        verifyControls();
    }
}
