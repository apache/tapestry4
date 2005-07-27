package org.apache.tapestry.enhance;

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Base class for common utilities when testing enhancement workers.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class BaseEnhancementTestCase extends HiveMindTestCase
{
    public IComponentSpecification newSpec(Location location)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getLocation();
        control.setReturnValue(location);

        return spec;
    }

    protected IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }
}
