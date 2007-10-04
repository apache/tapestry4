package org.apache.tapestry.pages;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.engine.ISpecificationSource;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.easymock.EasyMock.*;

@Test
public class TestException extends BaseComponentTestCase {

    @Test(dataProvider = "packages")
    public void test_getPackages(String pages, String comps, String[] expected) {

        ISpecificationSource source = newMock(ISpecificationSource.class);
        INamespace namespace = newMock(INamespace.class);
        expect(source.getApplicationNamespace()).andReturn(namespace);
        expect(namespace.getPropertyValue("page-prop")).andReturn(pages);
        expect(namespace.getPropertyValue("comp-prop")).andReturn(comps);

        replay();

        Exception exceptionPage = newInstance(Exception.class,
                "specificationSource", source,
                "pagePackages", "page-prop",
                "componentPackages", "comp-prop");

        String[] packages = exceptionPage.getPackages();
        assertListEquals(packages, expected);

        verify();

    }

    @DataProvider(name="packages")
    public Object[][] createPackages() {
        return new Object[][] {
                {null, null, new String[0]},
                {"pages", "components",
                        new String[]{"pages", "components"} },
                {"ajax", null, new String[]{"ajax"} },
                {null, "ajax", new String[]{"ajax"} }

        };
    }

}
