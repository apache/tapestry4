package org.apache.tapestry.bean;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
class BeanMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(BeanMessages.class,
            "BeanStrings");

    public static String propertyInitializerName(String propertyName)
    {
        return _formatter.format("property-initializer-name", propertyName);
    }

}