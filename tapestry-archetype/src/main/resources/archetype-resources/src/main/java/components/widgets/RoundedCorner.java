package ${packageName}.components.widgets;

import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.BaseComponent;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

import java.util.HashMap;
import java.util.Map;


/**
 * The rounded corner component provides a corner image, rendered with Java2D. Naturally, the inspiration was
 * the much vaunted google groups image generator URL. That URL is exemplary of the value of an engine service in Tapestry:
 * you parameterize it, and it generates useful output, even if the output itself is never going to be a destination for some user.
 * <p/>
 * It takes as it's parameters the background color, the foreground color, the desired
 * height and width, and the desired corner.
 * <p/>
 * Note that the component itself is really very simple. It just marshals the parameters
 * and converts them into useful input for our engine-service. The engine service does the
 * hard work of rendering the image using Java2D. All values have useful defaults, so getting it working can be as simple as
 * <p/>
 * <code>
 * <img src ="#" jwcid = "@widgets/RoundedCorner" corner = "SE"/>
 * </code>
 */
abstract public class RoundedCorner extends BaseComponent {


    @InjectObject("engine-service:RoundedCornerService")
    abstract public IEngineService getRoundedCornersService();


    public String getCornerImageURL() {
        IEngineService service = getRoundedCornersService();

        ILink link = service.getLink(false, getServiceParameters());

        return link.getURL();
    }

    public Map<String, String> getServiceParameters() {
        Map<String, String> ps = new HashMap<String, String>();
        ps.put("h", getHeight());
        ps.put("w", getWidth());
        ps.put("fg", getForegroundColor());
        ps.put("bg", getBackgroundColor());
        ps.put("corner", getCorner());
        return ps;
    }

    @Parameter(defaultValue = "literal:10")
    abstract public String getHeight();

    abstract public void setHeight(String height);

    @Parameter(defaultValue = "literal:#FFFFFF")
    abstract public String getBackgroundColor();

    abstract public void setBackgroundColor(String backgroundColor);

    @Parameter(defaultValue = "literal:#000000")
    abstract public String getForegroundColor();

    abstract public void setForegroundColor(String foregroundColor);

    @Parameter(defaultValue = "literal:NE")
    abstract public String getCorner();

    abstract public void setCorner(String corner);

    @Parameter(defaultValue = "literal:10")
    abstract public String getWidth();

    abstract public void setWidth(String width);
}
