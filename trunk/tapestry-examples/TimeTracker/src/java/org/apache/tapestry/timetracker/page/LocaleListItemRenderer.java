package org.apache.tapestry.timetracker.page;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.scriptaculous.ListItemRenderer;

import java.util.Iterator;
import java.util.Locale;

/**
 * Sample implementation of a html suggest response.
 */
public class LocaleListItemRenderer implements ListItemRenderer {

    /**
     * {@inheritDoc}
     */
    public void renderList(IMarkupWriter writer, IRequestCycle cycle, Iterator values)
    {
        if (cycle.isRewinding())
            return;

        //Write values out as simple strings
        writer.begin("ul");

        while (values.hasNext()) {
            
            Locale value = (Locale)values.next();
            if (value == null)
                continue;

            writer.begin("li");

            writer.beginEmpty("img");
            writer.attribute("src", "http://setiathome.free.fr/images/flags/" + value.getCountry().toLowerCase() + ".gif");
            writer.print(value.getDisplayCountry());
            
            writer.end("li");
        }

        writer.end();
    }
}
