package tutorial.components;

import net.sf.tapestry.html.BasePage;

/**
 * Example code for the "creating components" section of the tutorial
 * @author neil clayton
 */
public class Home extends BasePage {
	public Object[] getArraySource() {
		return new Object[] {
			new Object[] { "This is", "a test", "of the array viewer" },
			new Object[] { "There should be nothing in the next two columns", null, null },
			new Object[] { new Integer(1234), Boolean.TRUE, this }
		};
	}
}
