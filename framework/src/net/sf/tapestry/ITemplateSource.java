package net.sf.tapestry;

import net.sf.tapestry.parse.ComponentTemplate;

/**
 * A source of localized HTML templates for components.  
 * The cache is the means of access for components to load thier templates,
 * which they need not do until just before rendering.
 *
 * <p>The template cache must be able to locate and parse templates as needed.
 * It may maintain templates in memory.
 *
 * @author Howard Ship
 * @version $Id$
 * 
 **/

public interface ITemplateSource
{
    /**
     *  Name of an {@link IAsset} of a component that provides the template
     *  for the asset.  This overrides the default (that the template is in
     *  the same directory as the specification).  This allows
     *  pages or component templates to be located properly, relative to static
     *  assets (such as images and stylesheets).
     * 
     *  @since 2.2
     * 
     **/
    
    public static final String TEMPLATE_ASSET_NAME = "$template";
    
    /**
     *  Locates the template for the component.
     * 
     *  @param cycle The request cycle loading the template; this is required
     *  in some cases when the template is loaded from an {@link IAsset}.
     *  @param component The component for which a template should be loaded.
     *
     *  @throws ApplicationRuntimeException if the resource cannot be located or loaded.
     * 
     **/

    public ComponentTemplate getTemplate(IRequestCycle cycle, IComponent component);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     **/

    public void reset();
}