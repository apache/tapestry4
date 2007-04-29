package ${packageName}.application.aso;
import ${packageName}.util.* ;
import ${packageName}.application.* ;
import org.apache.tapestry.engine.state.StateObjectFactory;


public class GlobalASOFactory implements StateObjectFactory
{
     private Utilities utilities ;

    public Utilities getUtilities() {
        return utilities;
    }

    public void setUtilities(Utilities utilities) {
        this.utilities = utilities;
    }

    public Object createStateObject()
    {
       Global v = new Global() ;
        v.setUtilities(getUtilities());
        return v;
    }
}