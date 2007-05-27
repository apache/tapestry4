package org.apache.tapestry.binding;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests functionality of {@link ClientIdListBinding}. 
 */
@Test
public class TestClientIdListBinding extends BindingTestCase {

    public void test_Single_Component()
    {
        IComponent nested = newComponent();
        IComponent component = newMock(IComponent.class);
        ValueConverter vc = newValueConverter();

        Map comps = new HashMap();
        comps.put("foo", nested);
        
        expect(component.getComponents()).andReturn(comps);
        expect(component.getComponent("foo")).andReturn(nested);
        expect(nested.getClientId()).andReturn("fooClientId");

        List clientIds = Arrays.asList("fooClientId");
        
        expect(vc.coerceValue(clientIds, List.class)).andReturn(clientIds);

        Location l = newLocation();

        replay();

        ClientIdListBinding b = new ClientIdListBinding("param", vc, l, component, new String[]{"foo"});

        assert component == b.getComponent();
        assertEquals(b.getObject(List.class), Arrays.asList("fooClientId"));
        
        verify();
    }

    public void test_Multiple_Components()
    {
        IComponent compA = newComponent();
        IComponent compB = newComponent();
        ValueConverter vc = newValueConverter();
        
        IComponent component = newMock(IComponent.class);
        checkOrder(component, false);
        
        Map comps = new HashMap();
        comps.put("fred", compA);
        comps.put("barney", compB);

        expect(component.getComponents()).andReturn(comps).anyTimes();
        expect(component.getComponent("fred")).andReturn(compA);
        expect(compA.getClientId()).andReturn("comp_1");

        expect(component.getComponent("barney")).andReturn(compB);
        expect(compB.getClientId()).andReturn("comp_0");

        List clientIds = Arrays.asList("comp_1", "comp_0");

        expect(vc.coerceValue(clientIds, List.class)).andReturn(clientIds);

        Location l = newLocation();

        replay();

        ClientIdListBinding b = new ClientIdListBinding("param", vc, l, component, new String[]{"fred", "barney"});

        assert component == b.getComponent();
        assertEquals(b.getObject(List.class), Arrays.asList("comp_1", "comp_0"));

        verify();
    }

    public void test_Get_Object_Failure()
    {
        IComponent component = newMock(IComponent.class);
        Map comps = new HashMap();
        
        expect(component.getComponents()).andReturn(comps);
        expect(component.getPage()).andReturn(null);

        ValueConverter vc = newValueConverter();

        Location l = newLocation();

        replay();

        ClientIdListBinding b = new ClientIdListBinding("param", vc, l, component, new String[]{"foo"});

        try
        {
            b.getObject();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assert ex.getMessage().indexOf("id foo not found in container") > -1;
            assertSame(l, ex.getLocation());
        }

        verify();
    }
}
