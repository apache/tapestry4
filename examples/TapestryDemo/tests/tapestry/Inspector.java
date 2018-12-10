package tests.tapestry;

import com.primix.tapestry.parse.*;
import com.primix.tapestry.pageload.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import java.util.*;


/*
 * OPENSOURCE-PROJECT-NAME
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Inspector extends BaseComponent implements ILifecycle
{
	private String inspectedIdPath;
	private IComponent inspectedComponent;

	private boolean visible;
	private IComponent currentComponent;
	private String childComponentId;
	private boolean first;
	private boolean last;
	private IBinding binding;
	private String bindingName;
	private IPageChange pageChange;
	private String assetName;

	public Inspector(IPage page, IComponent container, String id, 
		ComponentSpecification spec)
	{
		super(page, container, id, spec);
	}

	public void cleanupAfterRender(IRequestCycle cycle)
	{
	}

	public IAsset getAsset()
	{
		return (IAsset)inspectedComponent.getAssets().get(assetName);
	}

	public String getAssetName()
	{
		return assetName;
	}

	public Collection getAssetNames()
	{
		Map assets;

		assets = inspectedComponent.getAssets();

		if (assets == null)
			return null;

		return sort(assets.keySet());
	}

	public String getBindingName()
	{
		return bindingName;
	}

	public Collection getBindingNames()
	{
		String id;
		IComponent inspected;

		inspected = getInspectedComponent();

		id = inspected.getId();

		if (id == null)
			return null;

		return sort(inspected.getContainer().
			getSpecification().
			getComponent(id).getBindingNames());
	}

	public String getBindingValue()
	{
		if (binding == null)
			return "<unset>";

		return binding.toString();	
	}

	public ContainedComponent getChildComponent()
	{
		return inspectedComponent.getSpecification().getComponent(childComponentId);
	}


	public String getChildComponentId()
	{
		return childComponentId;
	}

	public String getChildComponentIdPath()
	{
		return getInspectedComponent().getComponent(childComponentId).getIdPath();
	}

	public Collection getChildComponentIds()
	{
		return sort(getInspectedComponent().getSpecification().getComponentIds());
	}

	/**
	*  Gets a sequence of IComponent instances corresponding to the inspected id path.
	*
	*/

	public Collection getComponentPath()
	{
		LinkedList list;
		IComponent component;

		list = new LinkedList();

		component = inspectedComponent;

		while (component != null)
		{
			if (component.getId() == null)
				break;

			list.addFirst(component);

			component = component.getContainer();
		}

		return list;		
	}

	public IComponent getCurrentComponent()
	{
		return currentComponent;
	}

	public boolean getHasTemplate()
	{
		return inspectedComponent instanceof BaseComponent;
	}

	public IDirectListener getHideInspectorListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
				IRequestCycle cycle)
			{
				setVisible(false);
				setInspectedIdPath(null);
			}
		};
	}

	public IComponent getInspectedComponent()
	{
		if (inspectedComponent == null)
			inspectedComponent = getPage();

		return inspectedComponent;
	}

	public String getInspectedIdPath()
	{
		return inspectedIdPath;
	}

	public IPageChange getPageChange()
	{
		return pageChange;
	}

	public Collection getPageChanges()
	{
		IPageRecorder recorder;

		recorder = page.getApplication().getPageRecorder(page.getName());

		return recorder.getChanges();
	}

	public IDirectListener getSelectChildListener()
	{
		return new IDirectListener ()
		{
			public void directTriggered(IComponent component, String[] context, 
				IRequestCycle cycle)
			{
				setInspectedComponent(getPage().getNestedComponent(context[0]));
			}
		};
	}

	public IDirectListener getSelectComponentListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
				IRequestCycle cycle)
			{
				setInspectedComponent(getPage().getNestedComponent(context[0]));
			}
		};
	}

	public IDirectListener getSelectPageListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context, 
				IRequestCycle cycle)
			{
				setInspectedIdPath(null);
			}
		};
	}

	public IDirectListener getShowInspectorListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
				IRequestCycle cycle)
			{
				setVisible(true);
			}
		};
	}

	public IRender getTemplateDelegate()
	{
		return new IRender()
		{
			public void render(IResponseWriter writer, IRequestCycle cycle)
			{
				writeHTMLTemplate(writer);
			}
		};
	}

	public boolean isFirst()
	{
		return first;
	}

	public boolean isLast()
	{
		return last;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void reset()
	{
		visible = false;
		inspectedIdPath = null;
		inspectedComponent = null;
		currentComponent = null;
		childComponentId = null;
		binding = null;
		bindingName = null;
		pageChange = null;
		assetName = null;
	}

	public void setAssetName(String value)
	{
		assetName = value;
	}

	public void setBindingName(String value)
	{
		bindingName = value;

		binding = inspectedComponent.getBinding(value);
	}

	public void setChildComponentId(String newChildComponentId)
	{
		childComponentId = newChildComponentId;
	}

	public void setCurrentComponent(IComponent newCurrentComponent)
	{
		currentComponent = newCurrentComponent;
	}

	public void setFirst(boolean value)
	{
		first = value;
	}

	public void setInspectedComponent(IComponent value)
	{
		setInspectedIdPath(value.getIdPath());
	}

	public void setInspectedIdPath(java.lang.String newInspectedIdPath)
	{
		inspectedIdPath = newInspectedIdPath;

		if (inspectedIdPath == null)
			inspectedComponent = getPage();
		else
			inspectedComponent = getPage().getNestedComponent(inspectedIdPath);

		fireObservedChange("inspectedIdPath", inspectedIdPath);
	}

	public void setLast(boolean value)
	{
		last = value;
	}

	public void setPageChange(IPageChange value)
	{
		pageChange = value;
	}

	public void setVisible(boolean value)
	{
		visible = value;

		fireObservedChange("visible", visible);
	}

	private Collection sort(Collection c)
	{
    	List result;
        
        result = new ArrayList(c);

		Collections.sort(result);
        
        return result;
	}
  
	public void writeHTMLTemplate(IResponseWriter writer)
	{
		String templateName;
		ITemplateSource source;
		ComponentTemplate template;
		int count;
		char[] data;
		TemplateToken token;
		int i;
		boolean compressed;

		source = page.getApplication().getTemplateSource();

		try
		{
			template = source.getTemplate(inspectedComponent);
		}
		catch (ResourceUnavailableException e)
		{
			return;
		}

		compressed = writer.compress(true);
		writer.begin("pre");

			count = template.getTokenCount();
		data = template.getTemplateData();

		for (i = 0; i < count; i++)
		{
			token = template.getToken(i);

			if (token.getType() == TokenType.TEXT)
			{
				int start;
				int end;

				start = token.getStartIndex();
				end = token.getEndIndex();

				writer.print(data, start, end - start + 1);

				continue;
			}

			if (token.getType() == TokenType.CLOSE)
			{
				writer.begin("b");
				writer.print("</jwc>");
				writer.end();

				continue;
			}

			// Only other type is OPEN

			writer.begin("b");
			writer.print("<jwc id=\"");
			writer.begin("i");
			writer.print(token.getId());
			writer.end();
			writer.print('"');

			// Collapse an open & close down to a single tag.

			if (i + 1 < count &&
				template.getToken(i + 1).getType() == TokenType.CLOSE)
			{
				writer.print('/');
				i++;
			}

			writer.print('>');
			writer.end();  // <b>
		}

		writer.end(); // <pre>
		writer.setCompressed(compressed);
	}

}

