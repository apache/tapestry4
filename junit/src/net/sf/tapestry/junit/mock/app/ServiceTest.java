package net.sf.tapestry.junit.mock.app;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IExternalPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.junit.mock.lib.Dumper;

public class ServiceTest extends BasePage implements IExternalPage
{
    public Object[] getServiceParameters()
    {
        return new Object[] { "Sopranos", new Integer(20705), new Double(22./ 7.)};
    }

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle) throws RequestCycleException
    {
        Dumper dumper = (Dumper) cycle.getPage("lib:Dumper");

        dumper.setObjects(parameters);

        cycle.setPage(dumper);
    }

}
