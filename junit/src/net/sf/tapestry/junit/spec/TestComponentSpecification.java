package net.sf.tapestry.junit.spec;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.AssetSpecification;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;

/**
 *  Test cases for page and component specifications.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestComponentSpecification extends TapestryTestCase
{

    public TestComponentSpecification(String name)
    {
        super(name);
    }

    public void testBeanProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("BeanProperty.jwc");
        BeanSpecification fred = s.getBeanSpecification("fred");

        checkList("propertyNames", new String[] { "bruce", "nicole", "zeta" }, fred.getPropertyNames());

        checkProperty(fred, "bruce", "wayne");
        checkProperty(fred, "nicole", "kidman");
        checkProperty(fred, "zeta", "jones");

    }

    public void testComponentProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("ComponentProperty.jwc");
        ContainedComponent c = s.getComponent("barney");

        checkList("propertyNames", new String[] { "apple", "chocolate", "frozen" }, c.getPropertyNames());

        checkProperty(c, "apple", "pie");
        checkProperty(c, "chocolate", "cake");
        checkProperty(c, "frozen", "yogurt");

    }
    
    public void testAssetProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("AssetProperty.jwc");
        
        checkAsset(s, "private", "hugh", "grant");
        checkAsset(s, "external", "joan", "rivers");
        checkAsset(s, "context", "john", "cusak");
    }
    
    private void checkAsset(ComponentSpecification s, String assetName, String propertyName,
    String expectedValue)
    {
        AssetSpecification a = s.getAsset(assetName);
        
        assertEquals("Property " + propertyName + ".",
        expectedValue,
        a.getProperty(propertyName));
    }
}
