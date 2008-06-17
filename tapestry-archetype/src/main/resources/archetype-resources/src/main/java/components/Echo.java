package ${packageName}.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.Parameter;

/**
 * @author Joshua Long
 */
abstract public class Echo extends BaseComponent
{

    @Parameter
    public abstract String getValue();

}
