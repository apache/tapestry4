package net.sf.tapestry.contrib.informal;

import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 * 
 *  A version of the Any component that inherits the informal attributes of its parent
 * 
 *  @version $Id$
 *  @author mindbridge
 *  @since 2.2
 * 
 **/

public class InheritInformalAny extends AbstractComponent
{
    // Bindings
    private IBinding m_objElementBinding;

    public IBinding getElementBinding()
    {
        return m_objElementBinding;
    }

    public void setElementBinding(IBinding objElementBinding)
    {
        m_objElementBinding = objElementBinding;
    }

    protected void generateParentAttributes(IMarkupWriter writer, IRequestCycle cycle)
    {
        String attribute;

        IComponent objParent = getContainer();
        if (objParent == null)
            return;

        ComponentSpecification specification = objParent.getSpecification();
        Map bindings = objParent.getBindings();
        if (bindings == null)
            return;

        Iterator i = bindings.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();

            // Skip over formal parameters stored in the bindings
            // Map.  We're just interested in informal parameters.

            if (specification.getParameter(name) != null)
                continue;

            IBinding binding = (IBinding) entry.getValue();

            Object value = binding.getObject();
            if (value == null)
                continue;

            if (value instanceof IAsset)
            {
                IAsset asset = (IAsset) value;

                // Get the URL of the asset and insert that.
                attribute = asset.buildURL(cycle);
            }
            else
                attribute = value.toString();

            writer.attribute(name, attribute);
        }

    }

    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        String strElement = m_objElementBinding.getObject().toString();

        writer.begin(strElement);
        generateParentAttributes(writer, cycle);
        generateAttributes(writer, cycle);

        renderBody(writer, cycle);

        writer.end();
    }

}
