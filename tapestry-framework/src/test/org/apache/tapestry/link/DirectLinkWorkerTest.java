// Copyright Oct 5, 2006 The Apache Software Foundation
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
package org.apache.tapestry.link;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.Map;

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.engine.IScriptSource;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link DirectLinkWorker}.
 * 
 * @author jkuhnert
 */
@Test
public class DirectLinkWorkerTest extends BaseComponentTestCase
{

    public void test_Render()
    {
        DirectLink link = newInstance(DirectLink.class, new Object[] { 
            "disabled", Boolean.FALSE,
            "async", Boolean.TRUE,
            "json", Boolean.FALSE
            });
        
        IScriptSource scriptSource = newMock(IScriptSource.class);
        IRequestCycle cycle = newCycle(false);
        
        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);
        
        IScript script = newMock(IScript.class);
        
        expect(scriptSource.getScript(isA(Resource.class))).andReturn(script);
        
        script.execute(eq(link), eq(cycle), eq(prs), isA(Map.class));
        
        DirectLinkWorker w = new DirectLinkWorker();
        w.setScript("/org/apache/tapestry/link/DirectLinkWorker.script");
        w.setScriptSource(scriptSource);
        w.setClassResolver(new DefaultClassResolver());
        w.initialize();
        
        replay();
        
        w.renderComponent(cycle, link);
        
        verify();
    }
    
    public void test_Render_Disabled()
    {
        DirectLink link = newInstance(DirectLink.class, new Object[] { 
            "disabled", Boolean.TRUE,
            "async", Boolean.TRUE,
            "json", Boolean.FALSE
            });
        
        IScriptSource scriptSource = newMock(IScriptSource.class);
        IRequestCycle cycle = newCycle(false);
        
        DirectLinkWorker w = new DirectLinkWorker();
        w.setScript("/org/apache/tapestry/link/DirectLinkWorker.script");
        w.setScriptSource(scriptSource);
        w.setClassResolver(new DefaultClassResolver());
        w.initialize();
        
        replay();
        
        w.renderComponent(cycle, link);
        
        verify();
    }
}
