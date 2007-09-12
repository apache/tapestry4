package org.apache.tapestry.engine;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.TestBase;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.LibrarySpecification;
import org.testng.annotations.Test;

/**
 * Tests property value getting logic of {@link Namespace}.
 */
@Test
public class TestNamespaceProperties extends TestBase {

    public void test_Simple_Get_Property()
    {
        ILibrarySpecification spec = new LibrarySpecification();
        INamespace ns = new Namespace("test", null, spec, null);

        spec.setProperty("key", "value");

        assertEquals(ns.getPropertyValue("key"), "value");
    }

    public void test_Get_Immediate_Property()
    {
        ILibrarySpecification parentSpec = new LibrarySpecification();
        INamespace parentNs = new Namespace("parent", null, parentSpec, null);

        ILibrarySpecification spec = new LibrarySpecification();
        INamespace ns = new Namespace("test", parentNs, spec, null);

        parentSpec.setProperty("barney", "rubble");
        spec.setProperty("barney", "bam bam");

        assertEquals(ns.getPropertyValue("barney"), "bam bam");
    }

    public void test_Get_Parent_Property()
    {
        ILibrarySpecification parentSpec = new LibrarySpecification();
        INamespace parentNs = new Namespace("parent", null, parentSpec, null);

        ILibrarySpecification spec = new LibrarySpecification();
        INamespace ns = new Namespace("test", parentNs, spec, null);

        parentSpec.setProperty("barney", "rubble");

        assertEquals(ns.getPropertyValue("barney"), "rubble");
        assert ns.getPropertyValue("nothere") == null;
    }
}
