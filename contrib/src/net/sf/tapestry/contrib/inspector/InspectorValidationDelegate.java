package net.sf.tapestry.contrib.inspector;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.valid.ValidationDelegate;

/**
 * Customized version of {@link ValidationDelegate} that changes some
 * output behavior. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class InspectorValidationDelegate extends ValidationDelegate
{
    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError())
        {
            writer.printRaw("&nbsp;");
            writer.begin("span");
            writer.attribute("class", "error");
            writer.print("**");
            writer.end();
        }
    }

}