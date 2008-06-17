package ${packageName}.application.aso;

import org.apache.tapestry.engine.state.StateObjectFactory;

import ${packageName}.application.Visit;
import ${packageName}.util.Utilities;

/**
 * This is a factory for creating session-scoped ASOs and is configured in hivemodule.xml. This is
 * needed if your ASO can't be instantiated using a simple constructor. In this case we want to
 * inject a Spring managed bean into the ASO.
 */
public class VisitASOFactory implements StateObjectFactory
{

    private Utilities utilities;

    public Utilities getUtilities()
    {
        return utilities;
    }

    public void setUtilities(Utilities utilities)
    {
        this.utilities = utilities;
    }

    public Object createStateObject()
    {
        Visit v = new Visit();
        v.setUtilities(getUtilities());
        return v;
    }
}
