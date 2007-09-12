package org.apache.tapestry.scriptaculous;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * Error message formatter.
 */
public class ScriptaculousMessages {

    private static final MessageFormatter _formatter = new MessageFormatter(ScriptaculousMessages.class);

    // defeat instantiation
    private ScriptaculousMessages() {}

    public static String invalidOptions(String options, Throwable cause)
    {
        return _formatter.format("invalid-options", options, cause);
    }
}
