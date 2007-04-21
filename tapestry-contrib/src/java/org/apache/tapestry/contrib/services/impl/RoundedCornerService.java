package org.apache.tapestry.contrib.services.impl;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides generated rounded corner images in a similar use / fashion as
 * outlined here: <a href="http://xach.livejournal.com/95656.html">google's own cornershop</a>.
 */
public class RoundedCornerService implements IEngineService {

    public static final String SERVICE_NAME = "rounded";

    public static final String PARM_COLOR = "c";
    public static final String PARM_BACKGROUND_COLOR = "bc";
    public static final String PARM_WIDTH = "w";
    public static final String PARM_HEIGHT = "h";
    public static final String PARM_ANGLE = "a";

    private static final long MONTH_SECONDS = 60 * 60 * 24 * 30;

    private static final long EXPIRES = System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L;

    private RequestExceptionReporter _exceptionReporter;

    private LinkFactory _linkFactory;

    private WebResponse _response;

    private RoundedCornerGenerator _generator = new RoundedCornerGenerator();
    
    public ILink getLink(boolean post, Object parameter)
    {
        Defense.notNull(parameter, "parameter");
        Defense.isAssignable(parameter, Object[].class, "parameter");
        
        Object[] parms = (Object[]) parameter;
        
        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, getName());
        parameters.put(ServiceConstants.PARAMETER, parms);
        
        return _linkFactory.constructLink(this, post, parameters, false);
    }

    public void service(IRequestCycle cycle)
            throws IOException
    {
        String color = cycle.getParameter(PARM_COLOR);
        String bgColor = cycle.getParameter(PARM_BACKGROUND_COLOR);
        int width = getIntParam(cycle.getParameter(PARM_WIDTH));
        int height = getIntParam(cycle.getParameter(PARM_HEIGHT));
        String angle = cycle.getParameter(PARM_ANGLE);

        OutputStream os = null;

        try {

            byte[] data = _generator.buildCorner(color, bgColor, width, height, angle);

            _response.setDateHeader("Expires", EXPIRES);
            _response.setHeader("Cache-Control", "public, max-age=" + (MONTH_SECONDS * 3));
            _response.setContentLength(data.length);
            
            os = _response.getOutputStream(new ContentType("image/gif"));

            os.write(data);
            
        } catch (Throwable ex) {

            ex.printStackTrace();
            _exceptionReporter.reportRequestException("Error creating image.", ex);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (Throwable t) {
                // ignore
            }

        }
    }

    private int getIntParam(String value)
    {
        if (value == null)
            return 0;
        
        return Integer.valueOf(value).intValue();
    }

    public String getName()
    {
        return SERVICE_NAME;
    }

    /* Injected */
    public void setExceptionReporter(RequestExceptionReporter exceptionReporter)
    {
        _exceptionReporter = exceptionReporter;
    }

    /* Injected */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    /* Injected */
    public void setResponse(WebResponse response)
    {
        _response = response;
    }
}
