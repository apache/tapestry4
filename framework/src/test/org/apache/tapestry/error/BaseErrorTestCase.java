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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.error.TestExceptionPresenter.ExceptionFixture;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.test.Creator;

/**
 * Base class for tests of the various error reporting service implementations.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class BaseErrorTestCase extends BaseComponentTestCase
{

    protected IPage newPage()
    {
        Creator c = new Creator();
    
        return (IPage) c.newInstance(ExceptionFixture.class);
    }

    protected IRequestCycle newCycle(String pageName, IPage page)
    {
        IRequestCycle cycle = newCycle();
        
        expect(cycle.getPage(pageName)).andReturn(page);
    
        return cycle;
    }

    protected ResponseRenderer newRenderer(IRequestCycle cycle, Throwable throwable) throws Exception
    {
        ResponseRenderer renderer = newMock(ResponseRenderer.class);
    
        renderer.renderResponse(cycle);
    
        if (throwable != null)
            expectLastCall().andThrow(throwable);
    
        return renderer;
    }

    protected RequestExceptionReporter newReporter()
    {
        return newMock(RequestExceptionReporter.class);
    }

}
