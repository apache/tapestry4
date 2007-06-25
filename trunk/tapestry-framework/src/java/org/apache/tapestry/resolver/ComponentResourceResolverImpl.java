package org.apache.tapestry.resolver;

import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.web.WebContextResource;

import java.util.Locale;

/**
 * Implementation of {@link IComponentResourceResolver}.
 */
public class ComponentResourceResolverImpl implements IComponentResourceResolver {

    /**
     * Used to find resources under the context WEB-INF/ directory.
     */
    private static final String WEB_INF = "WEB-INF/";

    private AssetFactory _classpathAssetFactory;
    private AssetFactory _contextAssetFactory;

    /** Application id of the current tapestry app - as in applicationId.application */
    private String _applicationId;
    
    /** Root web context directory */
    private Resource _contextRoot;
    /** The location of the WEB-INF context directory */
    private Resource _webInfLocation;
    /** The location of the application within the WEB-INF/ folder */
    private Resource _webInfAppLocation;

    /**
     * Called by hivemind automatically.
     */
    public void initializeService()
    {
        _webInfLocation = _contextRoot.getRelativeResource(WEB_INF);
        
        _webInfAppLocation = _webInfLocation.getRelativeResource(_applicationId + "/");
    }

    public Resource findComponentResource(IComponent component, IRequestCycle cycle, String path, String extension, Locale locale)
    {
        Resource base = component.getSpecification().getSpecificationLocation();
        String baseName = path == null ? extractBaseName(base) : path;
        Resource resource = null;

        // have to do explicit check for context resource first
        // as it might be a classpath based spec and then we need to manually figure out
        // the best location to start from as context paths always get resolved first
        // before classpath resources
        
        if (WebContextResource.class.isInstance(base) || ContextResource.class.isInstance(base))
        {
            resource = base.getRelativeResource(baseName + extension);

            if (resource != null)
                return localizeResource(resource, locale);
        }

        resource = findComponentClassResource(component, cycle, baseName, extension, locale);

        // In some cases the generic classpath resource path is fine - such as bundled component properties

        if (resource == null) {
            
            resource = base.getRelativeResource(baseName + extension);
            
            if (resource != null)
                resource = localizeResource(resource, locale);
        }

        return resource;
    }

    String extractBaseName(Resource baseResourceLocation)
    {
        String fileName = baseResourceLocation.getName();
        int dotx = fileName.lastIndexOf('.');

        return dotx > -1 ? fileName.substring(0, dotx) : fileName;
    }

    Resource localizeResource(Resource resource, Locale locale)
    {
        if (locale == null)
            return resource;

        Resource localized = resource.getLocalization(locale);
        if (localized != null && localized.getResourceURL() != null)
            return localized;

        return resource;
    }

    Resource findComponentClassResource(IComponent component, IRequestCycle cycle, String baseName, String extension, Locale locale)
    {
        Resource base = component.getSpecification().getSpecificationLocation();
        String componentPackages = component.getNamespace().getPropertyValue("org.apache.tapestry.component-class-packages");

        // this relies on finding things from the component class name

        if (componentPackages == null)
            return null;

        String className = component.getSpecification().getComponentClassName();
        if (className == null)
            return null;

        String[] packages = TapestryUtils.split(componentPackages);
        for (int i=0; i < packages.length; i++)
        {
            // find matching package in class
            int index = className.lastIndexOf(packages[i]);
            if (index < 0)
                continue;

            // First try context

            String templateName = className.substring((index + packages[i].length()) + 1, className.length()).replaceAll("\\.", "/");
            templateName =  templateName + extension;

            if (_contextAssetFactory.assetExists(component.getSpecification(), _webInfAppLocation, templateName, locale)) {

                return _contextAssetFactory.createAsset(_webInfAppLocation, component.getSpecification(),  templateName, locale, component.getLocation()).getResourceLocation();
            } else if (_contextAssetFactory.assetExists(component.getSpecification(), _webInfLocation, templateName, locale)) {

                return _contextAssetFactory.createAsset(_webInfLocation, component.getSpecification(), templateName, locale, component.getLocation()).getResourceLocation();
            }

            // else classpath

            String resourceName = baseName + extension;

            if (_classpathAssetFactory.assetExists(component.getSpecification(), base, resourceName, locale))
                return _classpathAssetFactory.createAsset(base, component.getSpecification(), resourceName, locale, component.getLocation()).getResourceLocation();
        }

        return null;
    }

    public void setContextRoot(Resource contextRoot)
    {
        _contextRoot = contextRoot;
    }

    public void setClasspathAssetFactory(AssetFactory classpathAssetFactory)
    {
        _classpathAssetFactory = classpathAssetFactory;
    }

    public void setContextAssetFactory(AssetFactory contextAssetFactory)
    {
        _contextAssetFactory = contextAssetFactory;
    }

    public void setApplicationId(String applicationId)
    {
        _applicationId = applicationId;
    }
}
