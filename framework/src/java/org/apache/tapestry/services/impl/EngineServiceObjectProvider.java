package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.service.ObjectProvider;
import org.apache.tapestry.services.ServiceMap;

/**
 * Provides the implementation of the "engine-service:" prefix, were the locator is simply the name
 * of an engine service.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class EngineServiceObjectProvider implements ObjectProvider
{
    private ServiceMap _serviceMap;

    public void setServiceMap(ServiceMap serviceMap)
    {
        _serviceMap = serviceMap;
    }

    public Object provideObject(Module contributingModule, Class propertyType, String locator,
            Location location)
    {
        return _serviceMap.getService(locator);
    }
}