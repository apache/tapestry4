package org.apache.tapestry.link;

import java.util.Set;
import java.util.TreeSet;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

@Test
public class DirectLinkTest extends BaseComponentTestCase
{
	public void testConstructServiceParameters()
	{
		assertNull(DirectLink.constructServiceParameters(null));

		String plainObject = "plainObject";
		Object[] plainObjectResult = DirectLink.constructServiceParameters(plainObject);
		assertEquals(1, plainObjectResult.length);
		assertTrue(plainObject.equals(plainObjectResult[0]));

		Object[] objectArray = new Object[] {1, 2, 3};
		Object[] objectArrayResult = DirectLink.constructServiceParameters(objectArray);
		assertEquals(3, objectArrayResult.length);
		assertEquals(3, objectArrayResult[2]);

		Set set = new TreeSet();
		set.add(1);
		set.add(2);
		set.add(3);
		Object[] setResult = DirectLink.constructServiceParameters(set);
		assertEquals(3, setResult.length);
		assertEquals(2, setResult[1]);
	}
}
