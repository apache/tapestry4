package net.sf.tapestry.junit.load;

import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.pageload.ComponentClassLoader;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 * @author mindbridge
 *
 */
public class TestComponentClassLoader extends TapestryTestCase
{
    
    public TestComponentClassLoader(String name)
    {
        super(name);
    }

    
    public static void main(String[] args)
    {
        try
        {
            IResourceResolver res = new DefaultResourceResolver();
            ComponentClassLoader loader = new ComponentClassLoader(res);

            String resPath =
                "net/sf/tapestry/junit/load/EnhancedComponent.jwc";
            SpecificationParser parser = new SpecificationParser();
            ComponentSpecification spec =
                parser.parseComponentSpecification(
                    res.getResource(resPath).openStream(),
                    resPath);

            Class newClass = loader.findComponentClass(spec);
            EnhancedComponent testObj = (EnhancedComponent) newClass.newInstance();
            testObj.getCachedParameterString();

            System.out.println("Done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }

}
