/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.junit.prop;

import junit.framework.*;
import com.primix.tapestry.util.prop.*;
import java.io.*;
import java.util.*;

/**
 *  Tests for various aspects of dynamic property access via {@link PropertyHelper}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class PropertyHelperTest extends TestCase
{
	public static class PublicBean implements IPublicBean
	{
		public int intProperty;
		public Object objectProperty;
	}

	public static class BasicBean
	{
		private String stringProperty;

		public BasicBean()
		{
		}

		public BasicBean(String stringProperty)
		{
			this.stringProperty = stringProperty;
		}

		public String getStringProperty()
		{
			return stringProperty;
		}

		public void setStringProperty(String value)
		{
			stringProperty = value;
		}
	}

	public static Test suite()
	{
		return new TestSuite(PropertyHelperTest.class);
	}

	public PropertyHelperTest(String name)
	
	{
		super(name);
	}

	public void testSimpleStringProperty()
	{
		PropertyHelper helper = PropertyHelper.forInstance(this);

		assertEquals(
			"Check stringProperty.",
			helper.get(this, "stringProperty"),
			getStringProperty());
	}

	public void testSimpleIntProperty()
	{
		PropertyHelper helper = PropertyHelper.forInstance(this);

		assertEquals(
			"Check intProperty.",
			new Integer(getIntProperty()),
			helper.get(this, "intProperty"));
	}

	public void testSplitBasicPath()
	{
		String path = "foo.bar.baz";

		String[] split = PropertyHelper.splitPropertyPath(path);

		assertEquals(3, split.length);
		assertEquals("foo", split[0]);
		assertEquals("bar", split[1]);
		assertEquals("baz", split[2]);
	}

	public void testPublicBean()
	{
		PublicBean bean = new PublicBean();
		bean.intProperty = 2020;

		PropertyHelper helper = PropertyHelper.forInstance(bean);

		assertEquals(
			"Check intProperty.",
			new Integer(bean.intProperty),
			helper.get(bean, "intProperty"));
	}

	public void testPropertyPath()
	{
		PublicBean bean = new PublicBean();
		PublicBean bean2 = new PublicBean();

		bean2.intProperty = 1000;
		bean.objectProperty = bean2;

		PropertyHelper helper = PropertyHelper.forInstance(bean);

		assertEquals(
			"Check objectProperty.intProperty.",
			new Integer(bean2.intProperty),
			helper.getPath(bean, "objectProperty.intProperty"));
	}

	public void testSplitPropertyPath()
	{
		PublicBean bean = new PublicBean();
		PublicBean bean2 = new PublicBean();

		bean2.intProperty = 500;
		bean.objectProperty = bean2;

		PropertyHelper helper = PropertyHelper.forInstance(bean);

		String[] splitPath =
			PropertyHelper.splitPropertyPath("objectProperty.intProperty");

		assertEquals(new Integer(bean2.intProperty), helper.getPath(bean, splitPath));
	}

	public void testNormalPropertyRead()
	{
		BasicBean bean = new BasicBean("Atlantis");
		PropertyHelper helper = PropertyHelper.forInstance(bean);

		assertEquals(
			"Check stringProperty.",
			helper.get(bean, "stringProperty"),
			bean.getStringProperty());
	}

	public void testSimplePropertyWrite()
	{
		BasicBean bean = new BasicBean("Alpha");
		PropertyHelper helper = PropertyHelper.forInstance(bean);

		bean.setStringProperty("Beta");

		assertEquals("Check stringProperty.", "Beta", bean.getStringProperty());
	}

	public void testPathPropertyWrite()
	{
		PublicBean bean1 = new PublicBean();
		BasicBean bean2 = new BasicBean("Alpha");
		PropertyHelper helper = PropertyHelper.forInstance(bean1);

		bean1.objectProperty = bean2;

		helper.setPath(bean1, "objectProperty.stringProperty", "Beta");

		assertEquals(
			"Check objectProperty.stringProperty.",
			"Beta",
			bean2.getStringProperty());
	}

	public void testSplitPathPropertyWrite()
	{
		PublicBean bean1 = new PublicBean();
		BasicBean bean2 = new BasicBean("Alpha");
		PropertyHelper helper = PropertyHelper.forInstance(bean1);

		bean1.objectProperty = bean2;

		helper.setPath(
			bean1,
			new String[] { "objectProperty", "stringProperty" },
			"Gamma");

		assertEquals(
			"Check objectProperty.stringProperty.",
			"Gamma",
			bean2.getStringProperty());
	}

	public void testUnknownProperty()
	{
		PropertyHelper helper = PropertyHelper.forInstance(this);

		try
		{
			helper.get(this, "fooProperty");

			fail("Unknown property should throw MissingPropertyException.");
		}
		catch (MissingPropertyException ex)
		{
			assertEquals("Exception instance.", this, ex.getInstance());
			assertEquals("Exception propertyName.", "fooProperty", ex.getPropertyName());
			assertEquals("Exception rootObject.", this, ex.getRootObject());
			assertEquals("Exception propertyPath.", "fooProperty", ex.getPropertyPath());
		}
	}

	public void testUnknownPropertyPath()
	{
		PublicBean bean1 = new PublicBean();
		PublicBean bean2 = new PublicBean();
		bean1.objectProperty = bean2;

		PropertyHelper helper = PropertyHelper.forInstance(bean1);

		try
		{
			helper.getPath(bean1, "objectProperty.fooProperty");

			fail("Unknown property should throw MissingPropertyException.");
		}
		catch (MissingPropertyException ex)
		{
			assertEquals("Exception instance.", bean2, ex.getInstance());
			assertEquals("Exception propertyName.", "fooProperty", ex.getPropertyName());
			assertEquals("Exception rootObject.", bean1, ex.getRootObject());
			assertEquals(
				"Exception propertyPath.",
				"objectProperty.fooProperty",
				ex.getPropertyPath());
		}
	}

	/**
	 *  Gets accessors for a standard bean.
	 *
	 */

	public void testGetAccessorsStandard()
	{
		Map accessors = getAccessors(this);

		assertEquals("Number of accessors for this.", 4, accessors.size());

		assertNotNull("Accessors contains class.", accessors.get("class"));
		assertNotNull(
			"Accessors contains stringProperty.",
			accessors.get("stringProperty"));
		assertNotNull("Accessors contains intProperty.", accessors.get("intProperty"));
		assertNotNull("Accessors contains name.", accessors.get("name"));
	}

	public void testGetAccessorsPublicBean()
	{
		Map accessors = getAccessors(new PublicBean());

		assertEquals("Number of accessors for public bean.", 3, accessors.size());

		assertNotNull("Accessors contains class.", accessors.get("class"));
		assertNotNull(
			"Accessors contains objectProperty.",
			accessors.get("objectProperty"));
		assertNotNull("Accessors contains intProperty.", accessors.get("intProperty"));
	}

	public void testGetAccessorsMap()
	{
		Map map = new HashMap();
		map.put("foo", "foo");
		map.put("bar", new PublicBean());

		Map accessors = getAccessors(map);

		assertEquals("Number of accessors for Map.", 4, accessors.size());
		assertNotNull("Accessors contains class.", accessors.get("class"));
		assertNotNull("Accessors contains foo.", accessors.get("foo"));
		assertNotNull("Accessors contains bar.", accessors.get("bar"));
		assertNotNull("Accessors contains empty.", accessors.get("empty"));
	}

	public void testGetAccessorReadWrite()
	{
		BasicBean bean = new BasicBean();
		PropertyHelper helper = PropertyHelper.forInstance(bean);
		IPropertyAccessor accessor = helper.getAccessor(bean, "stringProperty");

		assertEquals("Accessor name.", "stringProperty", accessor.getName());
		assertEquals("Accessor type.", String.class, accessor.getType());
		assertEquals("Accessor readable.", true, accessor.isReadable());
		assertEquals("Accessor writable", true, accessor.isWritable());
		assertEquals("Accessor readWrite", true, accessor.isReadWrite());
	}

	public void testGetAccessorReadOnly()
	
	{
		PropertyHelper helper = PropertyHelper.forInstance(this);
		IPropertyAccessor accessor = helper.getAccessor(this, "intProperty");

		assertEquals("Accessor name.", "intProperty", accessor.getName());
		assertEquals("Accessor type.", Integer.TYPE, accessor.getType());
		assertEquals("Accessor readable.", true, accessor.isReadable());
		assertEquals("Accessor writable", false, accessor.isWritable());
		assertEquals("Accessor readWrite", false, accessor.isReadWrite());
	}

	public void testGetAccessorPublicBean()
	{
		PublicBean bean = new PublicBean();
		PropertyHelper helper = PropertyHelper.forInstance(bean);
		IPropertyAccessor accessor = helper.getAccessor(bean, "intProperty");

		assertEquals("Accessor name.", "intProperty", accessor.getName());
		assertEquals("Accessor type.", Integer.TYPE, accessor.getType());
		assertEquals("Accessor readable.", true, accessor.isReadable());
		assertEquals("Accessor writable", true, accessor.isWritable());
		assertEquals("Accessor readWrite", true, accessor.isReadWrite());
	}

	public void testGetAccessorMap()
	{
		Map map = new HashMap();
		map.put("foo", "bar");

		PropertyHelper helper = PropertyHelper.forInstance(map);
		IPropertyAccessor accessor = helper.getAccessor(map, "foo");

		assertEquals("Accessor name.", "foo", accessor.getName());
		assertEquals("Accessor type.", Object.class, accessor.getType());
		assertEquals("Accessor readable.", true, accessor.isReadable());
		assertEquals("Accessor writable", true, accessor.isWritable());
		assertEquals("Accessor readWrite", true, accessor.isReadWrite());
	}

	public void testGetUnknownAccessor()
	
	{
		PropertyHelper helper = PropertyHelper.forInstance(this);

		assertNull(
			"No accessor for unknown property foo.",
			helper.getAccessor(this, "foo"));
	}

	public String getStringProperty()
	{
		return "StringProperty";
	}

	public int getIntProperty()
	{
		return -200;
	}

	/**
	 *  Gets the accessors as a collection, and forms a Map of
	 *  accessors keyed on property name.
	 *
	 */

	private Map getAccessors(Object instance)
	{
		PropertyHelper helper = PropertyHelper.forInstance(instance);

		Collection c = helper.getAccessors(instance);

		Map result = new HashMap();
		Iterator i = c.iterator();
		while (i.hasNext())
		{
			IPropertyAccessor p = (IPropertyAccessor) i.next();

			result.put(p.getName(), p);
		}

		return result;
	}
}