package net.sf.tapestry.contrib.mckoi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jboss.system.ServiceMBeanSupport;
import com.mckoi.runtime.BootMain;

/**
 *  An MBean used to start and stop an embedded instance of
 *  <a href="http://www.mckoi.com/database">McKoi Database</a>.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public class McKoiDB extends ServiceMBeanSupport implements McKoiDBMBean
{
    private String rootPath;
    private String configPath;
    
    public String getRootPath()
    {
        return rootPath;
    }
    
    public void setRootPath(String path)
    {
        log.debug("Root path set to: " + path);
        rootPath = path;
    }
    
    public String getConfigPath()
    {
        return configPath;
    }
    
    public void setConfigPath(String path)
    {
        log.debug("Config path set to: " + path);
        configPath = path;
    }
    
    public ObjectName preRegister(MBeanServer server, ObjectName name)
        throws Exception
    {
        if (name != null)
            return name;
        return new ObjectName(":service=McKoiDB");
    }
    
    public String getName()
    {
        return "McKoiDB";
    }
    
    public void startService() throws Exception
    {
        if (rootPath == null)
            throw new NullPointerException("McKoiDB: rootPath not specified.");
        if (configPath == null)
            throw new NullPointerException("McKoiDB: configPath not specified.");
        log.debug("Root path: " + rootPath);
        log.debug("Config path: " + configPath);
        File file = new File(rootPath);
        InputStream stream = new FileInputStream(configPath);
        ResourceBundle bundle = new PropertyResourceBundle(stream);
        BootMain.boot(file, bundle);
    }
    
    public void stopService()
    {
        BootMain.shutdown();
    }
}