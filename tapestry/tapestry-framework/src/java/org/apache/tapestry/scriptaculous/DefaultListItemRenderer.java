package org.apache.tapestry.scriptaculous;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import java.util.Iterator;

/**
 * Default implementation of {@link ListItemRenderer}. Simply displays
 * the values of each object supplied by invoking toString(). Other more
 * advanced renderers may add html content to the <code>&lt;li&gt;</code> elements or
 * similarly advanced UI displays.
 */
public class DefaultListItemRenderer implements ListItemRenderer {

    /**
     * Shared global instance default used by {@link Suggest} when no custom renderer
     * is specified.
     */
    public static final ListItemRenderer SHARED_INSTANCE = new DefaultListItemRenderer();

    /**
     * {@inheritDoc}
     */
    public void renderList(IMarkupWriter writer, IRequestCycle cycle, Iterator values)
    {
        if (cycle.isRewinding())
            return;

        writer.begin("ul");
        
        while (values.hasNext())
        {
            Object value = values.next();
            
            if (value == null)
                continue;

            writer.begin("li");
            writer.print(value.toString());
            writer.end("li");
        }

        writer.end();
    }
}
