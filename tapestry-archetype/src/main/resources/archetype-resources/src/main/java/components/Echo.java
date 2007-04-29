package ${packageName}.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;

/**
 * @author Joshua Long
 *
 */
abstract public class Echo extends BaseComponent
{
    

    @Parameter abstract public String getValue() ;
    

}