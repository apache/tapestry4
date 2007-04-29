package ${packageName}.application.aso;
import ${packageName}.util.* ;
import ${packageName}.application.* ;
import org.apache.tapestry.engine.state.StateObjectFactory;


public class VisitASOFactory implements StateObjectFactory
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
       Visit v = new Visit() ;
        v.setUtilities(getUtilities());
        return v;
    }
}