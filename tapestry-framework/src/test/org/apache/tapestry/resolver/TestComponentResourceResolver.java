package org.apache.tapestry.resolver;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.*;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Locale;

/**
 * Tests functionality of {@link ComponentResourceResolverImpl}.
 */
@Test
public class TestComponentResourceResolver extends TestBase {

    public void test_Context_Spec_Resource()
    {
        IComponent comp = newMock(IComponent.class);
        checkOrder(comp, false);
        WebContext context = newMock(WebContext.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        IComponentSpecification spec = new ComponentSpecification();
        WebContextResource base = new WebContextResource(context, "/WEB-INF/MyComponent.jwc");
        spec.setSpecificationLocation(base);
        
        ComponentResourceResolverImpl resolver = new ComponentResourceResolverImpl();

        expect(comp.getSpecification()).andReturn(spec).anyTimes();
        expect(context.getResource("/WEB-INF/MyComponent.html")).andReturn(newURL());

        replay();

        Resource resolved = resolver.findComponentResource(comp, cycle, null, ".html", null);
        assert resolved != null;
        assert resolved.getResourceURL() != null;

        verify();
    }

    public void test_Context_Spec_Localized_Resource()
    {
        IComponent comp = newMock(IComponent.class);
        checkOrder(comp, false);
        WebContext context = newMock(WebContext.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        IComponentSpecification spec = new ComponentSpecification();
        WebContextResource base = new WebContextResource(context, "/WEB-INF/MyComponent.jwc");
        spec.setSpecificationLocation(base);

        ComponentResourceResolverImpl resolver = new ComponentResourceResolverImpl();

        expect(comp.getSpecification()).andReturn(spec).anyTimes();
        expect(context.getResource("/WEB-INF/MyComponent_en.html")).andReturn(newURL()).anyTimes();
        expect(context.getResource("/WEB-INF/MyComponent_en_US.html")).andReturn(null);

        replay();

        Resource resolved = resolver.findComponentResource(comp, cycle, null, ".html", Locale.US);
        assert resolved != null;
        assert resolved.getResourceURL() != null;
        
        verify();
    }

    public void test_Classpath_Spec_Resource_App_Context_Resolved()
    {
        IComponent comp = newMock(IComponent.class);
        checkOrder(comp, false);

        INamespace namespace = newMock(INamespace.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        IComponentSpecification spec = new ComponentSpecification();
        ClasspathResource base = new ClasspathResource(new DefaultClassResolver(), "/org/apache/tapestry/resolver/MyComponent.jwc");
        spec.setSpecificationLocation(base);
        spec.setComponentClassName("org.apache.tapestry.resolver.MyComponent");

        AssetFactory classpathFactory = newMock(AssetFactory.class);
        AssetFactory contextFactory = newMock(AssetFactory.class);
        Resource contextRoot = newMock(Resource.class);
        Resource webinfLocation = newMock(Resource.class);
        Resource webinfAppLocation = newMock(Resource.class);

        ComponentResourceResolverImpl resolver = new ComponentResourceResolverImpl();
        resolver.setApplicationId("foo");
        resolver.setClasspathAssetFactory(classpathFactory);
        resolver.setContextAssetFactory(contextFactory);
        resolver.setContextRoot(contextRoot);
        
        expect(contextRoot.getRelativeResource("WEB-INF/")).andReturn(webinfLocation);
        expect(webinfLocation.getRelativeResource("foo/")).andReturn(webinfAppLocation);

        expect(comp.getSpecification()).andReturn(spec).anyTimes();
        expect(comp.getNamespace()).andReturn(namespace);
        expect(namespace.getPropertyValue("org.apache.tapestry.component-class-packages")).andReturn("org.apache.tapestry.resolver");

        Location l = newMock(Location.class);
        IAsset asset = newMock(IAsset.class);
        Resource resource = newMock(Resource.class);
        
        expect(contextFactory.assetExists(spec, webinfAppLocation, "MyComponent.html", null)).andReturn(true);
        expect(comp.getLocation()).andReturn(l);
        expect(contextFactory.createAsset(webinfAppLocation, spec, "MyComponent.html", null, l)).andReturn(asset);
        expect(asset.getResourceLocation()).andReturn(resource);

        replay();

        resolver.initializeService();

        Resource resolved = resolver.findComponentResource(comp, cycle, null, ".html", null);
        assertEquals(resolved, resource);

        verify();
    }

    public void test_Classpath_Spec_Resource_WebInf_Context_Resolved()
    {
        IComponent comp = newMock(IComponent.class);
        checkOrder(comp, false);

        INamespace namespace = newMock(INamespace.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        IComponentSpecification spec = new ComponentSpecification();
        ClasspathResource base = new ClasspathResource(new DefaultClassResolver(), "/org/apache/tapestry/resolver/MyComponent.jwc");
        spec.setSpecificationLocation(base);
        spec.setComponentClassName("org.apache.tapestry.resolver.MyComponent");

        AssetFactory classpathFactory = newMock(AssetFactory.class);
        AssetFactory contextFactory = newMock(AssetFactory.class);
        Resource contextRoot = newMock(Resource.class);
        Resource webinfLocation = newMock(Resource.class);
        Resource webinfAppLocation = newMock(Resource.class);

        ComponentResourceResolverImpl resolver = new ComponentResourceResolverImpl();
        resolver.setApplicationId("foo");
        resolver.setClasspathAssetFactory(classpathFactory);
        resolver.setContextAssetFactory(contextFactory);
        resolver.setContextRoot(contextRoot);

        expect(contextRoot.getRelativeResource("WEB-INF/")).andReturn(webinfLocation);
        expect(webinfLocation.getRelativeResource("foo/")).andReturn(webinfAppLocation);

        expect(comp.getSpecification()).andReturn(spec).anyTimes();
        expect(comp.getNamespace()).andReturn(namespace);
        expect(namespace.getPropertyValue("org.apache.tapestry.component-class-packages")).andReturn("org.apache.tapestry.resolver");

        Location l = newMock(Location.class);
        IAsset asset = newMock(IAsset.class);
        Resource resource = newMock(Resource.class);

        expect(contextFactory.assetExists(spec, webinfAppLocation, "MyComponent.html", null)).andReturn(false);
        expect(contextFactory.assetExists(spec, webinfLocation, "MyComponent.html", null)).andReturn(true);
        expect(comp.getLocation()).andReturn(l);
        expect(contextFactory.createAsset(webinfLocation, spec, "MyComponent.html", null, l)).andReturn(asset);
        expect(asset.getResourceLocation()).andReturn(resource);

        replay();

        resolver.initializeService();

        Resource resolved = resolver.findComponentResource(comp, cycle, null, ".html", null);
        assertEquals(resolved, resource);

        verify();
    }

     public void test_Classpath_Spec_Resource_Classpath_Resolved()
    {
        IComponent comp = newMock(IComponent.class);
        checkOrder(comp, false);

        INamespace namespace = newMock(INamespace.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        IComponentSpecification spec = new ComponentSpecification();
        ClasspathResource base = new ClasspathResource(new DefaultClassResolver(), "/org/apache/tapestry/resolver/MyComponent.jwc");
        spec.setSpecificationLocation(base);
        spec.setComponentClassName("org.apache.tapestry.resolver.MyComponent");

        AssetFactory classpathFactory = newMock(AssetFactory.class);
        AssetFactory contextFactory = newMock(AssetFactory.class);
        Resource contextRoot = newMock(Resource.class);
        Resource webinfLocation = newMock(Resource.class);
        Resource webinfAppLocation = newMock(Resource.class);

        ComponentResourceResolverImpl resolver = new ComponentResourceResolverImpl();
        resolver.setApplicationId("foo");
        resolver.setClasspathAssetFactory(classpathFactory);
        resolver.setContextAssetFactory(contextFactory);
        resolver.setContextRoot(contextRoot);

        expect(contextRoot.getRelativeResource("WEB-INF/")).andReturn(webinfLocation);
        expect(webinfLocation.getRelativeResource("foo/")).andReturn(webinfAppLocation);

        expect(comp.getSpecification()).andReturn(spec).anyTimes();
        expect(comp.getNamespace()).andReturn(namespace);
        expect(namespace.getPropertyValue("org.apache.tapestry.component-class-packages")).andReturn("org.apache.tapestry.resolver");

        Location l = newMock(Location.class);
        IAsset asset = newMock(IAsset.class);
        Resource resource = newMock(Resource.class);

        expect(contextFactory.assetExists(spec, webinfAppLocation, "MyComponent.html", null)).andReturn(false);
        expect(contextFactory.assetExists(spec, webinfLocation, "MyComponent.html", null)).andReturn(false);
        expect(classpathFactory.assetExists(spec, base, "MyComponent.html", null)).andReturn(true);

        expect(comp.getLocation()).andReturn(l);
        expect(classpathFactory.createAsset(base, spec, "MyComponent.html", null, l)).andReturn(asset);
        expect(asset.getResourceLocation()).andReturn(resource);

        replay();

        resolver.initializeService();

        Resource resolved = resolver.findComponentResource(comp, cycle, null, ".html", null);
        assertEquals(resolved, resource);

        verify();
    }

    // Returns the same URL object pointing to any arbitrary test classpath resource
    public URL newURL()
    {
        return this.getClass().getResource("MyComponent.jwc");
    }
}
