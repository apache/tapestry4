package net.sf.tapestry.form;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implementation of {@link IPropertySelectionRenderer} that
 *  produces a table of radio (&lt;input type=radio&gt;) elements.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class RadioPropertySelectionRenderer implements IPropertySelectionRenderer
{

    /**
     *  Writes the &lt;table&gt; element.
     *
     **/

    public void beginRender(PropertySelection component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.begin("table");
        writer.attribute("border", 0);
        writer.attribute("cellpadding", 0);
        writer.attribute("cellspacing", 2);
    }

    /**
     *  Closes the &lt;table&gt; element.
     *
     **/

    public void endRender(PropertySelection component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.end(); // <table>
    }

    /**
     *  Writes a row of the table.  The table contains two cells; the first is the radio
     *  button, the second is the label for the radio button.
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
        writer.begin("tr");
        writer.begin("td");

        writer.beginEmpty("input");
        writer.attribute("type", "radio");
        writer.attribute("name", component.getName());
        writer.attribute("value", model.getValue(index));

        if (component.isDisabled())
            writer.attribute("disabled");

        if (selected)
            writer.attribute("checked");

        writer.end(); // <td>

        writer.println();

        writer.begin("td");
        writer.print(model.getLabel(index));
        writer.end(); // <td>
        writer.end(); // <tr>	

        writer.println();
    }
}