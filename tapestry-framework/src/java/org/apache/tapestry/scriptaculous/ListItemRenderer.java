package org.apache.tapestry.scriptaculous;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import java.util.Iterator;

/**
 * Renderer used by {@link Suggest} component to render lists usable
 * by the currently integrated
 * <a href="http://wiki.script.aculo.us/scriptaculous/show/Ajax.Autocompleter">script.aculo.us</a>
 * javascript library.
 *
 * <p/>This particular library expects the rendered contents of this class to
 * be an unordered html list.
 * <p/>
 * <pre>
 * &lt;ul&gt;
 *   &lt;li&gt;Apple&lt;/li&gt;
 *   &lt;li&gt;Apricot&lt;/li&gt;
 * &lt;/ul&gt;
 * </pre>
 */
public interface ListItemRenderer {

    /**
     * Renders an unordered html list to the response, using the specified
     * collection of values as the contents to fill in the <pre><li></pre> elements
     * with.
     * 
     * @param writer
     *          Markup writer to write content in to.
     * @param cycle
     *          Current request cycle.
     * @param values
     *          Values used to render list.
     */
    public void renderList(IMarkupWriter writer, IRequestCycle cycle, Iterator values);
}

