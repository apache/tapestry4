package net.sf.tapestry.contrib.mckoi;

import org.jboss.system.ServiceMBean;

/**
 *  MBean interface for {@link net.sf.tapestry.contrib.mckoi.McKoiDB}.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public interface McKoiDBMBean extends ServiceMBean
{
    public String getRootPath();
    
    /**
     *  Sets the root directory name, which is where database files are stored.
     * 
     **/
    
    public void setRootPath(String path);
    
    public String getConfigPath();
    
    /**
     *  Sets the config path, the path to a properties file that sets database
     *  parameters.
     * 
     **/
    
    public void setConfigPath(String path);
}