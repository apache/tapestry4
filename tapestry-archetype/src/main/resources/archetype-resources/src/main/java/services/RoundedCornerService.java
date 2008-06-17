package ${packageName}.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

import ${packageName}.util.Utilities;

public class RoundedCornerService implements IEngineService
{

    enum RoundedCorner
    {
        NW, NE, SW, SE
    }

    /**
     * This renders a rounded corner that has the specified size, dimensions, etc.
     * 
     * @param bg
     * @param fg
     * @param width
     * @param height
     * @param corner
     * @return
     * @throws Throwable
     */

    private BufferedImage renderRoundedCorner(String bg, String fg, int width, int height, RoundedCorner corner)
        throws Throwable
    {

        width = width * 2;
        height = height * 2;
        int visibleH = height / 2;
        int visibleW = width / 2;

        BufferedImage img = new BufferedImage(visibleW, visibleH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = img.createGraphics();

        int x = 0, y = 0;
        switch(corner) {
        case NW:
            // everythings rendered this way by default
            break;
        case SW:
            x = 0;
            y = (visibleH * 3) * -1;
            ;
            break;

        case NE:
            x = (visibleW * 3) * -1;
            y = 0;// visibleH * -1 ;

            break;

        case SE:
            x = (visibleW * 3) * -1;
            y = (visibleH * 3) * -1;

            break;
        }

        RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, width * 2, height * 2, width, height);

        Color bgColor = Color.decode(bg);

        Color fgColor = Color.decode(fg);
        Rectangle r = new Rectangle(width, height);
        g2d.setColor(bgColor);
        g2d.fill(r);
        g2d.setPaint(fgColor);
        g2d.setBackground(bgColor);
        g2d.setColor(fgColor);
        g2d.fill(rect);

        g2d.dispose();

        return img;
    }

    public static String SERVICE_NAME = "RoundedCornerService";

    public void service(IRequestCycle cycle)
        throws IOException
    {

        // so you hit the service
        // by the time were actually rendering stuff, we will always
        // have a map of <String,String > that translates well.

        String corner = cycle.getParameter("corner");
        String wS = cycle.getParameter("w"), hS = cycle.getParameter("h"), fg = cycle.getParameter("fg"), bg = cycle
                .getParameter("bg");

        RoundedCorner crnr = RoundedCorner.valueOf(corner);

        int h = Integer.parseInt(hS);
        int w = Integer.parseInt(wS);

        try {
            OutputStream output = response.getOutputStream(new ContentType("image/jpeg"));

            BufferedImage rc = renderRoundedCorner(bg, fg, w, h, crnr);

            ImageIO.write(rc, "jpeg", output);

        } catch (Throwable th) {
            getUtilities().log(th);
        }

    }

    public Utilities getUtilities()
    {
        return utilities;
    }

    public void setUtilities(Utilities utilities)
    {
        this.utilities = utilities;
    }

    private Utilities utilities;

    public ILink getLink(boolean isPost, Object parameter)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corner", RoundedCorner.NE.toString());
        params.put("bg", "#FFFFFF");
        params.put("w", "8");
        params.put("h", "8");
        params.put("fg", "#000000");

        if (null != parameter) {
            // grab the overridden defaults from the params if they exist
            Map<String, String> p = (Map<String, String>) parameter;

            Set<String> keys = params.keySet();

            for(String key : keys) {
                if (p.containsKey(key)) {

                    Object value = p.get(key);

                    if (value != null) params.put(key, value);

                }
            }

        }

        return linkFactory.constructLink(this, false, params, true);
    }

    private LinkFactory linkFactory;
    private WebResponse response;

    public LinkFactory getLinkFactory()
    {
        return linkFactory;
    }

    public void setLinkFactory(LinkFactory linkFactory)
    {
        this.linkFactory = linkFactory;
    }

    public WebResponse getResponse()
    {
        return response;
    }

    public void setResponse(WebResponse response)
    {
        this.response = response;
    }

    public String getName()
    {
        return SERVICE_NAME;
    }

}
