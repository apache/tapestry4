package org.apache.tapestry.services.impl;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.ServiceMap;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineServiceObjectProvider}.
 * 
 * @author Howard M. Lewis Ship
 */
public class TestEngineServiceObjectProvider extends HiveMindTestCase
{
    public void testProvideObject()
    {
        MockControl mapControl = newControl(ServiceMap.class);
        ServiceMap map = (ServiceMap) mapControl.getMock();

        IEngineService service = (IEngineService) newMock(IEngineService.class);

        map.getService("page");
        mapControl.setReturnValue(service);

        replayControls();

        EngineServiceObjectProvider p = new EngineServiceObjectProvider();

        p.setServiceMap(map);

        assertSame(service, p.provideObject(null, null, "page", null));

        verifyControls();
    }
}