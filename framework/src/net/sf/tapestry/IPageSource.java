package net.sf.tapestry;

/**
 *  Abstracts the process of loading pages from thier specifications as
 *  well as pooling of pages once loaded.  
 *
 *  <p>If the required page is not available, a page source may use an
 *  instance of {@link IPageLoader} to actually load the
 *  page (and all of its nested components).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IPageSource
{
    /**
     *  Gets a given page for the engine.  This may involve using a previously
     *  loaded page from a pool of available pages, or the page may be loaded as needed.
     * 
     *  <p>Incompatible change in 2.2:  First parameter changed from
     *  {@link IEngine} to {@link IRequestCycle}.
     *
     **/

    public IPage getPage(IRequestCycle cycle, String pageName, IMonitor monitor) throws PageLoaderException;

    /**
     *  Invoked after the engine is done with the page
     *  (typically, after the response to the client has been sent).
     *  The page is returned to the pool for later reuse.
     *
     **/

    public void releasePage(IPage page);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     **/

    public void reset();

    /**
     *  Gets a field binding for the named field (the name includes the class name
     *  and the field).  If no such binding exists, then one is created, otherwise
     *  the existing binding is returned. 
     *
     * @since 1.0.2
     **/

    public IBinding getFieldBinding(String fieldName);

    /**
     *  Like {@link #getFieldBinding(String)}, except for static bindings.
     *
     * @since 1.0.2
     **/

    public IBinding getStaticBinding(String value);

    /**
     *  Gets a cached asset.
     *
     *  @since 1.0.2
     *
     **/

    public IAsset getExternalAsset(String URL);

    /**
     *  Gets a cached asset.
     *
     *  @since 1.0.2
     *
     **/

    public IAsset getContextAsset(String assetPath);

    /**
     *  Gets a cached asset.
     *
     *  @since 1.0.2
     *
     **/

    public IAsset getPrivateAsset(String resourcePath);
    
    /**
     * 
     *  @since NEXT_RELEASE
     * 
     **/
    
    public IResourceResolver getResourceResolver();
        
}