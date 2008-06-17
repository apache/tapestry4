package ${packageName}.application.aso;

import org.apache.tapestry.engine.state.StateObjectFactory;

import ${packageName}.application.Global;
import ${packageName}.util.Utilities;

/**
 * This is a factory for creating application-scoped ASOs and is configured in hivemodule.xml. This is
 * needed if your ASO can't be instantiated using a simple constructor. In this case we want to
 * inject a Spring managed bean into the ASO.
 */
public class GlobalASOFactory implements StateObjectFactory
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
        Global v = new Global();
        v.setUtilities(getUtilities());
        return v;
    }
}
