package org.apache.tapestry.pageload;

import org.apache.hivemind.PoolManageable;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IPage;
import org.apache.tapestry.dojo.IWidget;

/**
 * Looks for components of type {@link org.apache.tapestry.IForm} and {@link org.apache.tapestry.dojo.IWidget} so
 * that the appropriate javascript includes can be made on an as needed basis by {@link org.apache.tapestry.dojo.AjaxShellDelegate}.
 */
public class ComponentTypeVisitor implements IComponentVisitor, PoolManageable {

    IPage _page;

    public void visitComponent(IComponent component)
    {
        if (IPage.class.isInstance(component)) {

            _page = (IPage) component;
            return;
        }

        if (IForm.class.isInstance(component) && _page != null) {

            _page.setHasFormComponents(true);
        }

        if (IWidget.class.isInstance(component) && _page != null) {

            _page.setHasWidgets(true);
        }
    }

    public void activateService()
    {
        _page = null;
    }

    public void passivateService()
    {
        _page = null;
    }
}
