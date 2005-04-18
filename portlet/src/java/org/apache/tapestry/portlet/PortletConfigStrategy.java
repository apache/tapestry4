package org.apache.tapestry.portlet;

import java.util.Iterator;

import javax.portlet.PortletConfig;

import org.apache.tapestry.describe.DescribableStrategy;
import org.apache.tapestry.describe.DescriptionReceiver;
import org.apache.tapestry.web.WebUtils;

/**
 * Adapts {@link javax.portlet.PortletConfig}&nbsp;for describing.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletConfigStrategy implements DescribableStrategy
{

    public void describeObject(Object object, DescriptionReceiver receiver)
    {
        PortletConfig pc = (PortletConfig) object;

        receiver.title("Portlet Config");

        receiver.property("portletName", pc.getPortletName());

        receiver.section("Init Parameters");

        Iterator i = WebUtils.toSortedList(pc.getInitParameterNames()).iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            receiver.property(name, pc.getInitParameter(name));
        }
    }
}