package net.sf.tapestry.spec;

import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  Defines an internal, external or private asset.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class AssetSpecification extends BasePropertyHolder
{
    private AssetType type;
    protected String path;

    public AssetSpecification(AssetType type, String path)
    {
        this.type = type;
        this.path = path;
    }

    /**
     *  Returns the base path for the asset.  This may be interpreted as a URL, relative URL
     *  or the path to a resource, depending on the type of asset.
     *
     **/

    public String getPath()
    {
        return path;
    }

    public AssetType getType()
    {
        return type;
    }
}