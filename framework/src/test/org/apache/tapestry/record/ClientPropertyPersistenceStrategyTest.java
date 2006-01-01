// Copyright 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.record;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.web.WebRequest;

/**
 * Tests for {@link org.apache.tapestry.record.ClientPropertyPersistenceStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ClientPropertyPersistenceStrategyTest extends HiveMindTestCase
{
    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private PersistentPropertyDataEncoder newEncoder()
    {
        PersistentPropertyDataEncoderImpl encoder = new PersistentPropertyDataEncoderImpl();
        encoder.setClassResolver(getClassResolver());

        return encoder;
    }

    private IPage newPage()
    {
        return (IPage) newMock(IPage.class);
    }

    private WebRequest newRequest()
    {
        return (WebRequest) newMock(WebRequest.class);
    }

    private ClientPropertyPersistenceScope newScope()
    {
        return (ClientPropertyPersistenceScope) newMock(ClientPropertyPersistenceScope.class);
    }

    public void testAddParametersForPersistentProperties()
    {
        WebRequest request = newRequest();

        ServiceEncoding encoding = (ServiceEncoding) newMock(ServiceEncoding.class);

        trainGetParameterNames(request, new String[]
        { "bar", "appstate:MyPage" });

        trainGetParameterValue(request, "appstate:MyPage", "ENCODED");

        encoding.setParameterValue("appstate:MyPage", "ENCODED");

        replayControls();

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();
        strategy.setRequest(request);
        strategy.setScope(new AppClientPropertyPersistenceScope());
        strategy.setEncoder(newEncoder());

        strategy.initializeService();

        strategy.addParametersForPersistentProperties(encoding, false);

        verifyControls();
    }

    public void testGetChangesUnknownPage()
    {
        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();

        assertTrue(strategy.getStoredChanges("UnknownPage").isEmpty());
    }

    public void testInitialize()
    {
        WebRequest request = newRequest();
        ClientPropertyPersistenceScope scope = newScope();
        PersistentPropertyDataEncoder encoder = (PersistentPropertyDataEncoder) newMock(PersistentPropertyDataEncoder.class);

        trainGetParameterNames(request, new String[]
        { "foo", "state:MyPage" });

        trainIsParameterForScope(scope, "foo", false);
        trainIsParameterForScope(scope, "state:MyPage", true);

        trainExtractPageName(scope, "state:MyPage", "MyPage");

        trainGetParameterValue(request, "state:MyPage", "ENCODED");

        List changes = Collections.singletonList(new PropertyChangeImpl("foo", "bar", "baz"));

        trainDecodePageChanges(encoder, "ENCODED", changes);

        replayControls();

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();
        strategy.setRequest(request);
        strategy.setScope(scope);
        strategy.setEncoder(encoder);

        strategy.initializeService();

        assertSame(changes, strategy.getStoredChanges("MyPage"));

        verifyControls();
    }

    public void testPageScope()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        IPage page = newPage();

        ServiceEncoding encoding = (ServiceEncoding) newMock(ServiceEncoding.class);

        trainGetPage(cycle, page);

        trainGetPageName(page, "MyPage");

        trainGetPage(cycle, page);
        trainGetPageName(page, "MyPage");

        trainGetParameterNames(request, new String[]
        { "foo", "state:MyPage", "state:OtherPage" });

        trainGetParameterValue(request, "state:MyPage", "ENCODED1");
        trainGetParameterValue(request, "state:OtherPage", "ENCODED2");

        encoding.setParameterValue("state:MyPage", "ENCODED1");

        replayControls();

        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();
        scope.setRequestCycle(cycle);

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();
        strategy.setRequest(request);
        strategy.setScope(scope);
        strategy.setEncoder(newEncoder());

        strategy.initializeService();

        strategy.addParametersForPersistentProperties(encoding, false);

        verifyControls();

    }

    public void testStoreAndRetrieve()
    {
        PropertyChange pc = new PropertyChangeImpl("foo", "bar", "baz");

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();
        strategy.setEncoder(newEncoder());

        strategy.store("MyPage", "foo", "bar", "baz");

        assertEquals(Collections.singletonList(pc), strategy.getStoredChanges("MyPage"));

        strategy.discardStoredChanges("MyPage");

        assertEquals(Collections.EMPTY_LIST, strategy.getStoredChanges("MyPage"));
    }

    private void trainDecodePageChanges(PersistentPropertyDataEncoder encoder, String encoded,
            List changes)
    {
        encoder.decodePageChanges(encoded);
        setReturnValue(encoder, changes);
    }

    private void trainExtractPageName(ClientPropertyPersistenceScope scope, String parameterName,
            String pageName)
    {
        scope.extractPageName(parameterName);
        setReturnValue(scope, pageName);
    }

    private void trainGetPage(IRequestCycle cycle, IPage page)
    {
        cycle.getPage();
        setReturnValue(cycle, page);
    }

    private void trainGetPageName(IPage page, String pageName)
    {
        page.getPageName();
        setReturnValue(page, pageName);
    }

    private void trainGetParameterNames(WebRequest request, String[] names)
    {
        request.getParameterNames();
        setReturnValue(request, Arrays.asList(names));
    }

    private void trainGetParameterValue(WebRequest request, String parameterName, String value)
    {
        request.getParameterValue(parameterName);
        setReturnValue(request, value);
    }

    private void trainIsParameterForScope(ClientPropertyPersistenceScope scope,
            String parameterName, boolean result)
    {
        scope.isParameterForScope(parameterName);
        setReturnValue(scope, result);
    }
}