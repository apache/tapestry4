package net.sf.tapestry.contrib.form;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  Implementation of {@link IMultiplePropertySelectionRenderer} that
 *  produces a table of checkbox (&lt;input type=checkbox&gt;) elements.
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 **/

public class CheckBoxMultiplePropertySelectionRenderer
    implements IMultiplePropertySelectionRenderer
{

    /**
     *  Writes the &lt;table&gt; element.
     *
     **/

    public void beginRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
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

    public void endRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.end(); // <table>
    }

    /**
     *  Writes a row of the table.  The table contains two cells; the first is the checkbox,
     *  the second is the label for the checkbox.
     *
     **/

    public void renderOption(
        MultiplePropertySelection component,
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
        writer.attribute("type", "checkbox");
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