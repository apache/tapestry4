package net.sf.tapestry.form;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implementation of {@link IPropertySelectionRenderer} that
 *  produces a &lt;select&gt; element (containing &lt;option&gt; elements).
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class SelectPropertySelectionRenderer
    implements IPropertySelectionRenderer
{
    /**
     *  Writes the &lt;select&gt; element.  If the
     *  {@link PropertySelection} is {@link PropertySelection#isDisabled() disabled}
     *  then a <code>disabled</code> attribute is written into the tag
     *  (though Navigator 4 will ignore this).
     *
     **/

    public void beginRender(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.begin("select");
        writer.attribute("name", component.getName());

        if (component.isDisabled())
            writer.attribute("disabled");

        writer.println();
    }

    /**
     *  Closes the &lt;select&gt; element.
     *
     **/

    public void endRender(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.end(); // <select>
    }

    /**
     *  Writes an &lt;option&gt; element.
     *
     **/

    public void renderOption(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected)
        throws RequestCycleException
    {
        writer.beginEmpty("option");
        writer.attribute("value", model.getValue(index));

        if (selected)
            writer.attribute("selected");

        writer.print(model.getLabel(index));

        writer.end();
        
        writer.println();
    }
}