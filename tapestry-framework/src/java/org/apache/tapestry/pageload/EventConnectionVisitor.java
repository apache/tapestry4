package org.apache.tapestry.pageload;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.PoolManageable;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.internal.Component;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;

import java.util.*;

/**
 * Handles connecting up components and forms targeted with the EventListener annotation.
 */
public class EventConnectionVisitor implements IComponentVisitor, PoolManageable {

    IComponentEventInvoker _invoker;

    IPage _currentPage = null;
    List _forms = new ArrayList();

    public void visitComponent(IComponent component)
    {
        checkComponentPage(component);
        
        Map events = component.getSpecification().getComponentEvents();
        Set keySet = events.keySet();
        String[] compIds = (String[]) keySet.toArray(new String[keySet.size()]);
        
        for (int i=0; i < compIds.length; i++)
        {
            String compId = compIds[i];
            ComponentEventProperty property = (ComponentEventProperty) events.get(compId);

            // find the targeted component

            IComponent comp = findComponent(compId, component.getPage());

            if (comp == null)
                continue;

            // wire up with idPath

            String idPath = comp.getExtendedId();
            
            component.getSpecification().rewireComponentId(compId, idPath);
            
            _invoker.addEventListener(idPath, component.getSpecification());
            wireFormEvents(comp, component.getSpecification());
        }
        
        // find form element targets for re-mapping with proper idpath && IEventInvoker connection

        events = component.getSpecification().getElementEvents();
        Iterator it = events.keySet().iterator();

        while (it.hasNext())
        {
            String elementId = (String) it.next();
            ComponentEventProperty property = (ComponentEventProperty) events.get(elementId);

            Iterator bindingIt  = property.getFormEvents().iterator();
            while (bindingIt.hasNext())
            {
                String key = (String) bindingIt.next();
                List listeners = property.getFormEventListeners(key);

                for (int i=0; i < listeners.size(); i++) {
                    
                    EventBoundListener listener = (EventBoundListener) listeners.get(i);
                    wireElementFormEvents(listener, component, component.getSpecification());
                }
            }
        }
    }

    void wireElementFormEvents(EventBoundListener listener, IComponent component, IComponentSpecification spec)
    {
        if (listener.getFormId() == null)
            return;

        if (_forms.size() < 1)
            discoverPageForms(component.getPage());

        IForm form = null;
        for (int i=0; i < _forms.size(); i++) {

            IForm f = (IForm) _forms.get(i);
            if (listener.getFormId().equals(f.getId())) {
                form = f;
                break;
            }
        }

        // couldn't find the form they specified

        if (form == null)
            throw new ApplicationRuntimeException(PageloadMessages.componentNotFound(listener.getFormId()), component, component.getLocation(), null);

        String idPath = form.getExtendedId();
        
        listener.setFormId(idPath);
        _invoker.addFormEventListener(idPath, spec);
    }

    void wireFormEvents(IComponent component, IComponentSpecification listener)
    {
        if (!IFormComponent.class.isInstance(component))
            return;

        IFormComponent fcomp = (IFormComponent) component;

        if (_forms.size() < 1)
            discoverPageForms(fcomp.getPage());

        IForm form = findComponentForm(fcomp);
        if (form == null)
            return;

        listener.connectAutoSubmitEvents(component, form);
        _invoker.addFormEventListener(form.getExtendedId(), listener);
    }

    IComponent findComponent(String id, IComponent target)
    {
        Map components = target.getComponents();
        if (components == null)
            return null;
        
        IComponent comp = (IComponent) components.get(id);
        if (comp != null)
            return comp;

        Iterator children = components.values().iterator();

        while (children.hasNext())
        {
            IComponent child = (IComponent) children.next();

            comp = findComponent(id, child);
            if (comp != null)
                return comp;
        }

        return null;
    }

    void discoverPageForms(IComponent parent)
    {
        if (IForm.class.isInstance(parent))
            _forms.add(parent);

        Iterator it = parent.getComponents().values().iterator();
        while (it.hasNext())
        {
            IComponent comp = (IComponent)it.next();

            discoverPageForms(comp);
        }
    }

    IForm findComponentForm(IFormComponent child)
    {
        for (int i = 0; i < _forms.size(); i++) {

            IForm form = (IForm) _forms.get(i);

            IComponent match = findContainedComponent(child.getExtendedId(), (Component)form);
            if (match != null)
                return form;
        }

        return null;
    }

    IComponent findContainedComponent(String idPath, Component container)
    {
        IComponent comp = (IComponent) container;

        if (idPath.equals(comp.getExtendedId()))
            return comp;

        IRender[] children = container.getContainedRenderers();
        if (children == null)
            return null;

        for (int i=0; i < children.length; i++) {

            if (children[i] == null)
                return null;

            if (!Component.class.isInstance(children[i]))
                continue;

            IComponent found = findContainedComponent(idPath, (Component)children[i]);
            if (found != null)
                return found;
        }
        
        return null;
    }

    void checkComponentPage(IComponent component)
    {
        if (_currentPage == null) {

            _currentPage = component.getPage();
            _forms.clear();
        } else if (component.getPage() != _currentPage) {

            _currentPage = component.getPage();
            _forms.clear();
        }
    }

    public void activateService()
    {
        _currentPage = null;
        _forms.clear();
    }

    public void passivateService()
    {
        _currentPage = null;
        _forms.clear();
    }

    // injected
    public void setEventInvoker(IComponentEventInvoker invoker)
    {
        _invoker = invoker;
    }
}
