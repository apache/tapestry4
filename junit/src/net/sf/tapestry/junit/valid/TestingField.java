package net.sf.tapestry.junit.valid;

import java.util.Collection;
import java.util.Map;

import com.primix.tapestry.IAsset;
import com.primix.tapestry.IBinding;
import com.primix.tapestry.IComponent;
import com.primix.tapestry.IPage;
import com.primix.tapestry.IPageLoader;
import com.primix.tapestry.IRender;
import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IResponseWriter;
import com.primix.tapestry.PageLoaderException;
import com.primix.tapestry.RequestCycleException;
import com.primix.tapestry.spec.ComponentSpecification;
import com.primix.tapestry.valid.IValidator;
import com.primix.tapestry.valid.IField;

/**
 *  Used as a stand-in for a real component when testing the {@link ITranslator}
 *  implementations.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

public class TestingField implements IField
{
	private String displayName;
	private Class valueType = String.class;

	public TestingField(String displayName)
	{
		this.displayName = displayName;
	}

	public TestingField(String displayName, Class type)
	{
		this.displayName = displayName;
		this.valueType = type;
	}
	
	public Class getValueType()
	{
		return valueType;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getName()
	{
		return displayName;
	}

	public void addAsset(String name, IAsset asset)
	{
	}

	public void addComponent(IComponent component)
	{
	}

	public void addWrapped(IRender element)
	{
	}

	public Map getAssets()
	{
		return null;
	}

	public IAsset getAsset(String name)
	{
		return null;
	}

	public IBinding getBinding(String name)
	{
		return null;
	}

	public Collection getBindingNames()
	{
		return null;
	}

	public Map getBindings()
	{
		return null;
	}

	public IComponent getComponent(String id)
	{
		return null;
	}

	public IComponent getContainer()
	{
		return null;
	}

	public void setContainer(IComponent value)
	{
	}

	public String getExtendedId()
	{
		return null;
	}

	public String getId()
	{
		return null;
	}

	public void setId(String value)
	{
	}

	public String getIdPath()
	{
		return null;
	}

	public IPage getPage()
	{
		return null;
	}

	public void setPage(IPage value)
	{
	}

	public ComponentSpecification getSpecification()
	{
		return null;
	}

	public void setSpecification(ComponentSpecification value)
	{
	}

	public void renderWrapped(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
	}

	public void setBinding(String name, IBinding binding)
	{
	}

	public Map getComponents()
	{
		return null;
	}

	public void finishLoad(
		IPageLoader loader,
		ComponentSpecification specification)
		throws PageLoaderException
	{
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
	}

}