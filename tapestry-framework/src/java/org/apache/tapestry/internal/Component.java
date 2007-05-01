package org.apache.tapestry.internal;

import org.apache.tapestry.IRender;

/**
 * Represents the <em>internal</em> component api exposed for use by core framework code only.
 *
 * <p>
 * Use at your own risk as everything in this API is subject to change without notice from release to
 * release.
 * </p>
 */
public interface Component {

    /**
     * Returns the list of of {@link IRender} elements contained by this component. Ie whatever
     * has been added via {@link org.apache.tapestry.IComponent#addBody(IRender)}.
     *
     * @return The values, if any. Null otherwise.
     */
    IRender[] getContainedRenderers();

    /**
     * In some rare cases a component has both outer and inner renderers - such as with {@link org.apache.tapestry.BaseComponent}. This
     * value should return the normal inner renderers most components do in those instances while the other
     * {@link #getContainedRenderers()} should return the outer renderers.
     * 
     * @return The inner renderers if this component supports more than one type, null otherwise.
     */
    IRender[] getInnerRenderers();
}
