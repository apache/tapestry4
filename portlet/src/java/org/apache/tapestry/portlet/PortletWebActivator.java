package org.apache.tapestry.portlet;

import java.util.List;

import javax.portlet.PortletConfig;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.describe.DescriptionReceiver;
import org.apache.tapestry.web.WebActivator;
import org.apache.tapestry.web.WebUtils;

/**
 * Adapts a {@link javax.portlet.PortletConfig}&nbsp; as {@link org.apache.tapestry.web.WebActivator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletWebActivator implements WebActivator
{
    private final PortletConfig _config;

    public PortletWebActivator(PortletConfig config)
    {
        Defense.notNull(config, "config");

        _config = config;
    }

    public String getActivatorName()
    {
        return _config.getPortletName();
    }

    public List getInitParameterNames()
    {
        return WebUtils.toSortedList(_config.getInitParameterNames());
    }

    public String getInitParameterValue(String name)
    {
        return _config.getInitParameter(name);
    }

    public void describeTo(DescriptionReceiver receiver)
    {
        receiver.describeAlternate(_config);
    }

}