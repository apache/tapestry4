package net.sf.tapestry.vlib;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.valid.ValidationDelegate;

/**
 *  Implementation of {@link net.sf.tapestry.valid.IValidationDelegate} 
 *  which uses the
 *  correct CSS class when rendering errors.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class SimpleValidationDelegate extends ValidationDelegate
{
    public void writeLabelPrefix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
    {
        if (isInError(component))
        {
            writer.begin("span");
            writer.attribute("class", "clsInvalidField");
        }
    }

    public void writeLabelSuffix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
    {
        if (isInError(component))
            writer.end();
    }

    public void writePrefix(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (isInError())
        {
            writer.begin("span");
            writer.attribute("class", "error");
        }
    }

    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (isInError())
            writer.end(); // <span>
    }

}