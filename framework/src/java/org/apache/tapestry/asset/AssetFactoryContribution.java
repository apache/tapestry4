package org.apache.tapestry.asset;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * Contribution to the <code>tapestry.asset.AssetFactories</code> configuration point.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class AssetFactoryContribution extends BaseLocatable
{
    private String _prefix;

    private AssetFactory _factory;

    public AssetFactory getFactory()
    {
        return _factory;
    }

    public void setFactory(AssetFactory factory)
    {
        _factory = factory;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public void setPrefix(String prefix)
    {
        _prefix = prefix;
    }
}