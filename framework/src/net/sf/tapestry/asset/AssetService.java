package net.sf.tapestry.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.engine.AbstractService;

/**
 *  A service for building URLs to and accessing {@link IAsset}s.
 *  Most of the work is deferred to the {@link IAsset} instance.
 *
 *  <p>The retrieval part is directly linked to {@link PrivateAsset}.
 *  The service responds to a URL that encodes the path of a resource
 *  within the classpath.  The 
 *  {@link #service(IEngineServiceView, IRequestCycle, ResponseOutputStream)} 
 *  method reads the resource and streams it out.
 *
 *  <p>TBD: Security issues.  Should only be able to retrieve a
 *  resource that was previously registerred in some way
 *  ... otherwise, hackers will be able to suck out the .class files
 *  of the application!
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class AssetService extends AbstractService
{
    private String _servletPath;

    /**
     *  Defaults MIME types, by extension, used when the servlet container
     *  doesn't provide MIME types.  ServletExec Debugger, for example,
     *  fails to do provide these.
     *
     **/

    private final static Map _mimeTypes;

    static {
        _mimeTypes = new HashMap(17);
        _mimeTypes.put("css", "text/css");
        _mimeTypes.put("gif", "image/gif");
        _mimeTypes.put("jpg", "image/jpeg");
        _mimeTypes.put("jpeg", "image/jpeg");
        _mimeTypes.put("htm", "text/html");
        _mimeTypes.put("html", "text/html");
    }

    private static final int BUFFER_SIZE = 10240;

    /**
     *  Builds a {@link Gesture} for a {@link PrivateAsset}.
     *
     *  <p>A single parameter is expected, the resource path of the asset
     *  (which is expected to start with a leading slash).
     *
     **/

    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 1)
            throw new ApplicationRuntimeException(Tapestry.getString("service-single-parameter", ASSET_SERVICE));

        // Service is stateless

        return assembleGesture(cycle, ASSET_SERVICE, null, parameters, false);
    }

    public String getName()
    {
        return ASSET_SERVICE;
    }

    private static String getMimeType(String path)
    {
        String key;
        String result;
        int dotx;

        dotx = path.lastIndexOf('.');
        key = path.substring(dotx + 1).toLowerCase();

        result = (String) _mimeTypes.get(key);

        if (result == null)
            result = "text/plain";

        return result;
    }

    /**
     *  Retrieves a resource from the classpath and returns it to the
     *  client in a binary output stream.
     *
     *  <p>TBD: Security issues.  Hackers can download .class files.
     *
     *
     **/

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws ServletException, IOException, RequestCycleException
    {
        Object[] parameters = getParameters(cycle);

        String resourcePath =  (String)parameters[0];
        
        URL resourceURL = cycle.getEngine().getResourceResolver().getResource(resourcePath);

        if (resourceURL == null)
            throw new ApplicationRuntimeException(Tapestry.getString("missing-resource", resourcePath));

        URLConnection resourceConnection = resourceURL.openConnection();

        ServletContext servletContext = cycle.getRequestContext().getServlet().getServletContext();

        writeAssetContent(engine, cycle, output, resourcePath, resourceConnection, servletContext);

        // Return false, to indicate that no server side state could have changed.

        return false;
    }

    /**  @since 2.2 **/
    
    private void writeAssetContent(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output,
        String resourcePath,
        URLConnection resourceConnection,
        ServletContext servletContext)
    {
        // Getting the content type and length is very dependant
        // on support from the application server (represented
        // here by the servletContext).

        String contentType = servletContext.getMimeType(resourcePath);
        int contentLength = resourceConnection.getContentLength();

        try
        {
            if (contentLength > 0)
                cycle.getRequestContext().getResponse().setContentLength(contentLength);

            // Set the content type.  If the servlet container doesn't
            // provide it, try and guess it by the extension.

            if (contentType == null || contentType.length() == 0)
                contentType = getMimeType(resourcePath);

            output.setContentType(contentType);

            // Disable any further buffering inside the ResponseOutputStream

            output.forceFlush();

            InputStream input = resourceConnection.getInputStream();

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true)
            {
                int bytesRead = input.read(buffer);

                if (bytesRead < 0)
                    break;

                output.write(buffer, 0, bytesRead);
            }

            input.close();
        }
        catch (Throwable ex)
        {
            String title = Tapestry.getString("AssetService.exception-report-title", resourcePath);

            engine.reportException(title, ex);
        }
    }
}