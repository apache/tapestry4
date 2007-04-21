package org.apache.tapestry.engine.encoders;

import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Encoder for mapping service names as simple paths such as "/service" . 
 */
public class PathEncoder implements ServiceEncoder {

    private String _path;

    private String _service;

    public void setPath(String path)
    {
        _path = path;
    }

    public void setService(String service)
    {
        _service = service;
    }

    public void encode(ServiceEncoding encoding)
    {
        if (!encoding.getParameterValue(ServiceConstants.SERVICE).equals(_service))
            return;
           
        encoding.setServletPath(_path);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
    }

    public void decode(ServiceEncoding encoding)
    {
        if (!encoding.getServletPath().equals(_path))
            return;
        
        encoding.setParameterValue(ServiceConstants.SERVICE, _service);
    }
}
