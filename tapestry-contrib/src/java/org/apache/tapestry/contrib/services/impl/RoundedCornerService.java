package org.apache.tapestry.contrib.services.impl;

import org.apache.commons.logging.Log;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

    public static final String PARM_SHADOW_WIDTH ="sw";
    public static final String PARM_SHADOW_OPACITY ="o";
    public static final String PARM_SHADOW_SIDE = "s";

    public static final String PARM_WHOLE_SHADOW = "shadow";
    public static final String PARM_ARC_HEIGHT = "ah";
    public static final String PARM_ARC_WIDTH = "aw";

    private static final long MONTH_SECONDS = 60 * 60 * 24 * 30;

    private static final long EXPIRES = System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L;

    private RequestExceptionReporter _exceptionReporter;

    private LinkFactory _linkFactory;

    private WebRequest _request;

    private WebResponse _response;

    private RoundedCornerGenerator _generator = new RoundedCornerGenerator();

    // holds pre-built binaries for previously generated colors
    private Map _imageCache = new HashMap();

    private Log _log;

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
        if (_request.getHeader("If-Modified-Since") != null)
        {
            _response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        String color = cycle.getParameter(PARM_COLOR);
        String bgColor = cycle.getParameter(PARM_BACKGROUND_COLOR);
        int width = getIntParam(cycle.getParameter(PARM_WIDTH));
        int height = getIntParam(cycle.getParameter(PARM_HEIGHT));
        String angle = cycle.getParameter(PARM_ANGLE);
        
        int shadowWidth = getIntParam(cycle.getParameter(PARM_SHADOW_WIDTH));
        float shadowOpacity = getFloatParam(cycle.getParameter(PARM_SHADOW_OPACITY));
        String side = cycle.getParameter(PARM_SHADOW_SIDE);

        boolean wholeShadow = Boolean.valueOf(cycle.getParameter(PARM_WHOLE_SHADOW)).booleanValue();
        float arcWidth = getFloatParam(cycle.getParameter(PARM_ARC_WIDTH));
        float arcHeight = getFloatParam(cycle.getParameter(PARM_ARC_HEIGHT));

        String hashKey = color + bgColor + width + height + angle + shadowWidth + shadowOpacity + side + wholeShadow;

        ByteArrayOutputStream bo = null;
        
        try {
            
            String type = (bgColor != null) ? "gif" : "png";

            byte[] data = (byte[])_imageCache.get(hashKey);
            if (data != null) {

                writeImageResponse(data, type);
                return;
            }

            BufferedImage image = null;

            if (wholeShadow) {

                image = _generator.buildShadow(bgColor, width, height, arcWidth, arcHeight, shadowWidth, shadowOpacity);
            } else if (side != null) {

                image = _generator.buildSideShadow(side, shadowWidth, shadowOpacity);
            } else {

                image = _generator.buildCorner(color, bgColor, width, height, angle, shadowWidth, shadowOpacity);
            }

            bo = new ByteArrayOutputStream();

            ImageIO.write(image, type, bo);

            data = bo.toByteArray();

            if (data == null || data.length < 1)
            {
                _log.error("Image generated had zero length byte array from parameters of:\n"
                           + "[color:" + color + ", bgColor:" + bgColor
                           + ", width:" + width + ", height:" + height
                           + ", angle:" + angle + ", shadowWidth:" + shadowWidth
                           + ", shadowOpacity:" + shadowOpacity + ", side:" + side
                           + ", wholeShadow: " + wholeShadow + ", arcWidth: " + arcWidth
                           + ", arcHeight:" + arcHeight + "\n image: " + image);

                _response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            _imageCache.put(hashKey, data);

            writeImageResponse(data, type);
            
        } catch (Throwable ex) {

            ex.printStackTrace();
            _exceptionReporter.reportRequestException("Error creating image.", ex);
        } finally {
            try {
                if (bo != null) {
                    bo.close();
                }
            } catch (Throwable t) {
                // ignore
            }

        }
    }

    void writeImageResponse(byte[] data, String type)
    throws Exception
    {
        OutputStream os = null;

        try {
            _response.setDateHeader("Expires", EXPIRES);
            _response.setHeader("Cache-Control", "public, max-age=" + (MONTH_SECONDS * 3));
            _response.setContentLength(data.length);

            os = _response.getOutputStream(new ContentType("image/" + type));

            os.write(data);

        }  finally {
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
            return -1;
        
        return Integer.valueOf(value).intValue();
    }

    private float getFloatParam(String value)
    {
        if (value == null)
            return -1f;
        
        return Float.valueOf(value).floatValue();
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
    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    /* Injected */
    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    public void setLog(Log log)
    {
        _log = log;
    }
}
