package org.apache.tapestry.annotations;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Test case for {@link org.apache.tapestry.annotations.MetaAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MetaAnnotationWorkerTest extends BaseAnnotationTestCase
{

    public void testMeta()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Location l = newLocation();

        spec.setProperty("foo", "bar");
        spec.setProperty("biff", "bazz");

        replayControls();

        new MetaAnnotationWorker().performEnhancement(op, spec, MetaPage.class, l);

        verifyControls();
    }

    public void testMetaInSubclass()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Location l = newLocation();

        // From MetaPage
        spec.setProperty("foo", "bar");
        spec.setProperty("biff", "bazz");

        // From MetaPageSubclass
        spec.setProperty("in-subclass", "true");

        replayControls();

        new MetaAnnotationWorker().performEnhancement(op, spec, MetaPageSubclass.class, l);

        verifyControls();

    }

    public void testNoEqualsSign()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Location l = newLocation();

        replayControls();

        MetaAnnotationWorker worker = new MetaAnnotationWorker();

        try
        {
            worker.performEnhancement(op, spec, MissingEqualsMetaPage.class, l);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "The meta value 'noequals' must include an equals sign to seperate the key and the value.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();

    }
}
