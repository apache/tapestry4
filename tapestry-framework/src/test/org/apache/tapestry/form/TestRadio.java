package org.apache.tapestry.form;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import org.testng.annotations.Test;

@Test
public class TestRadio extends BaseFormComponentTestCase
{
	private Radio radio;
	private RadioGroup group;

	private IMarkupWriter writer;
	private IRequestCycle cycle;

	public void test_Render_Selected()
	{
		setupComponent(0, false);

		replay();

		radio.render(writer, cycle);

		verify();

		assertBuffer("<input type=\"radio\" name=\"group\" id=\"group0\" checked=\"checked\" value=\"0\" />");
	}

	public void test_Render_Unselected()
	{
		setupComponent(1, false);

		replay();

		radio.render(writer, cycle);

		verify();

		assertBuffer("<input type=\"radio\" name=\"group\" id=\"group0\" value=\"0\" />");
	}

	public void test_Rewind_Selected()
	{
		setupComponent(0, true);

		IBinding binding = newMock(IBinding.class);
        group.setBinding("selected", binding);
		binding.setObject(0);
		expectLastCall();

		replay();

		radio.render(writer, cycle);

		verify();
	}

	public void test_Rewind_Unselected()
	{
		setupComponent(1, true);

		replay();

		radio.render(writer, cycle);

		verify();
	}

	private void setupComponent(int selection, boolean rewinding)
	{
		group = newInstance(RadioGroup.class);
		group.setName("group");
		group._rendering = true;
		group._rewinding = rewinding;
		group._selection = selection;
		group._selectedOption = selection;
		radio = newInstance(Radio.class,
						          "value", 0);

		writer = newBufferWriter();
		cycle = newCycle();
		IForm form = newMock(IForm.class);
		MockDelegate delegate = new MockDelegate();

		expect(cycle.getAttribute(RadioGroup.ATTRIBUTE_NAME)).andReturn(group);
		expect(cycle.renderStackPush(radio)).andReturn(radio);

		trainGetForm(cycle, form);
		trainWasPrerendered(form, writer, radio, false);
		trainGetDelegate(form, delegate);

		//trainGetElementId(form, radio, "id");
		trainIsRewinding(form, rewinding);
		if (!rewinding)
		{
			trainIsRewinding(cycle, rewinding);
			form.setFormFieldUpdating(true);
		}

		expect(cycle.renderStackPop()).andReturn(radio);
	}
}
