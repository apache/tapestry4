package net.sf.tapestry.junit.valid;

import java.util.Collection;
import java.util.Map;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.FormEventType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.valid.IValidationDelegate;

/**
 *  Simulates an {@link IForm} for the test suite.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.1
 *
 **/

public class TestingForm implements IForm
{
    private String _name;

    public TestingForm()
    {
        this("DefaultFormName");
    }

    public TestingForm(String name)
    {
        _name = name;
    }

    public void rewind(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

    public void addEventHandler(FormEventType type, String functionName)
    {
    }

    public String getElementId(IComponent component)
    {
        return null;
    }

    public String getElementId(String baseId)
    {
        return null;
    }

    public String getName()
    {
        return _name;
    }

    public boolean isRewinding()
    {
        return false;
    }

    public IValidationDelegate getDelegate() throws RequestCycleException
    {
        return null;
    }

    public boolean getRequiresSession()
    {
        return false;
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

    public void renderWrapped(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

    public void setBinding(String name, IBinding binding)
    {
    }

    public Map getComponents()
    {
        return null;
    }

    public void finishLoad(IRequestCycle cycle, IPageLoader loader, ComponentSpecification specification)
        throws PageLoaderException
    {
    }

    public String getString(String key)
    {
        return null;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

    public INamespace getNamespace()
    {
        return null;
    }

    public void setNamespace(INamespace namespace)
    {
    }

    public void addBody(IRender element)
    {
    }

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

}
