package org.apache.tapestry.asset;

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
class AssetMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(AssetMessages.class,
            "AssetStrings");

    public static String missingAsset(String path, Resource resource)
    {
        return _formatter.format("missing-asset", path, resource);
    }
}