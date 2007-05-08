package org.apache.tapestry.resolver;

import org.apache.hivemind.Resource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

import java.util.Locale;

/**
 * Service responsible for finding resources relative or specific to a {@link org.apache.tapestry.IComponent}.
 * 
 */
public interface IComponentResourceResolver {

    /**
     * Searches for a resource relative to the specified {@link IComponent}, optionally also attemping to
     * find a localized version of the resource using the specified {@link Locale}.
     * 
     * @param component
     *          The component to find the resource relative to.
     * @param cycle
     *          The current request.
     * @param name
     *          Optional resource name to search for, the default is to use the component name.
     * @param extension
     *          Extension name of the resource, such as &lt;ComponentName&gt;.properties for properties
     *          / &lt;ComponentName&gt;.html for templates and so on.  
     * @param locale
     *          Optional localization specifier.
     * 
     * @return The resolved resource, or null if none could be found. The returned {@link Resource} may
     *          also be not null but still not valid. To ensure validity check {@link org.apache.hivemind.Resource#getResourceURL()} for
     *          a not null value.
     */
    Resource findComponentResource(IComponent component, IRequestCycle cycle, String name, String extension, Locale locale);
}
