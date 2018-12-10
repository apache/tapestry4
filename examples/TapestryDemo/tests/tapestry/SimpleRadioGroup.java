package tests.tapestry;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
/**
 * Simplified way to present a radio group.
 */
public class SimpleRadioGroup extends BaseComponent 
implements ILifecycle
{
	private SelectionAdaptor adaptor;

	protected static final String ADAPTOR_NAME = "adaptor";
/**
 * SimpleRadioGroup constructor comment.
 * @param page com.primix.tapestry.IPage
 * @param container com.primix.tapestry.IComponent
 * @param id java.lang.String
 * @param spec com.primix.tapestry.spec.ComponentSpecification
 */
public SimpleRadioGroup(IPage page, IComponent container, String id, ComponentSpecification spec) {
	super(page, container, id, spec);
}
public void cleanupAfterRender(IRequestCycle cycle)
{
	adaptor = null;
}
public SelectionAdaptor getAdaptor()
{
	if (adaptor == null)
		adaptor = (SelectionAdaptor)getBinding(ADAPTOR_NAME).getValue();

	return adaptor;
}
}
