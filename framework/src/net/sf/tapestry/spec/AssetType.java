package net.sf.tapestry.spec;

import org.apache.commons.lang.enum.Enum;

/**
 *  Defines the types of assets.
 *
 *  @see AssetSpecification
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public final class AssetType extends Enum
{
    /**
     *  An external resource.
     *
     **/

    public static final AssetType EXTERNAL = new AssetType("EXTERNAL");

    /**
     *  A resource visible to the {@link javax.servlet.ServletContext}.
     *
     **/

    public static final AssetType CONTEXT = new AssetType("CONTEXT");

    /**
     *  An internal resource visible only on the classpath.  Typically,
     *  a resource package in a WAR or JAR file alongside the classes.
     *
     **/

    public static final AssetType PRIVATE = new AssetType("PRIVATE");

    private AssetType(String name)
    {
        super(name);
    }

}