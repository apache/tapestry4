package tutorial.workbench;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.valid.ValidationDelegate;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 **/

public class WorkbenchValidationDelegate extends ValidationDelegate
{
    public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (isInError())
            writer.attribute("class", "field-error");
    }

    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (isInError())
        {
            writer.print(" ");
            writer.beginEmpty("img");
            writer.attribute("src", "images/Warning-small.gif");
            writer.attribute("height", 20);
            writer.attribute("width", 20);
        }
    }

    public void writeLabelPrefix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError(component))
        {
            writer.begin("span");
            writer.attribute("class", "label-error");
        }
    }

    public void writeLabelSuffix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError(component))
            writer.end(); // <span>
    }
}