//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.inspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageChange;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.util.exception.ExceptionAnalyzer;
import net.sf.tapestry.util.exception.ExceptionDescription;
import net.sf.tapestry.util.prop.IPropertyAccessor;
import net.sf.tapestry.util.prop.IPublicBean;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Component of the {@link Inspector} page used to display
 *  the persisent properties of the page as well as the
 *  properties explorer.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowProperties extends BaseComponent implements PageRenderListener
{
    /**
     *  Stores elements in the explorePath.
     *
     *  @since 1.0.6
     **/

    public static class ExplorePathElement implements IPublicBean
    {
        public ExplorePathElement(String path, String propertyName)
        {
            this.path = path;
            this.propertyName = propertyName;
        }

        public String path;
        public String propertyName;
    }

    public static class AccessorElement implements IPublicBean, Comparable
    {
        public String propertyPath;
        public String propertyName;
        public String propertyType;
        public boolean error;

        /**
         *  The names will be unique and we want to sort
         *  into ascending order by name.
         *
         **/

        public int compareTo(Object other)
        {
            AccessorElement otherElement = (AccessorElement) other;

            return propertyName.compareTo(otherElement.propertyName);
        }
    }

    private List properties;
    private IPageChange change;
    private IPage inspectedPage;
    private String errorPropertyName;
    private ExceptionDescription[] propertyException;

    /**
     *  Registers this component as a {@link PageRenderListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        page.addPageRenderListener(this);
    }

    /**
     *  Does nothing.
     *
     *  @since 1.0.5
     *
     **/

    public void pageBeginRender(PageEvent event)
    {
    }

    /**
     *  @since 1.0.5
     *
     **/

    public void pageEndRender(PageEvent event)
    {
        properties = null;
        change = null;
        inspectedPage = null;
        errorPropertyName = null;
        propertyException = null;
    }

    private void buildProperties()
    {
        Inspector inspector = (Inspector) page;

        inspectedPage = inspector.getInspectedPage();

        IEngine engine = page.getEngine();
        IPageRecorder recorder = engine.getPageRecorder(inspectedPage.getName());

        // No page recorder?  No properties.

        if (recorder == null)
            return;

        if (recorder.getHasChanges())
            properties = new ArrayList(recorder.getChanges());
    }

    /**
     *  Returns a {@link List} of {@link IPageChange} objects.
     *
     *  <p>Sort order is not defined.
     *
     **/

    public List getProperties()
    {
        if (properties == null)
            buildProperties();

        return properties;
    }

    public void setChange(IPageChange value)
    {
        change = value;
    }

    public IPageChange getChange()
    {
        return change;
    }

    /**
     *  Returns true if the current change has a null component path.
     *
     **/

    public boolean getDisableComponentLink()
    {
        return change.getComponentPath() == null;
    }

    /**
     *  Returns the name of the value's class, if the value is non-null.
     *
     **/

    public String getValueClassName()
    {
        Object value;

        value = change.getNewValue();

        if (value == null)
            return "<null>";

        return convertClassToName(value.getClass());
    }

    public List getExplorePath()
    {
        Inspector inspector = (Inspector) page;

        String explorePath = inspector.getExplorePath();
        if (explorePath == null)
            return null;

        String[] propertyName = PropertyHelper.splitPropertyPath(explorePath);
        List result = new ArrayList(propertyName.length);
        StringBuffer buffer = new StringBuffer(explorePath.length());

        for (int i = 0; i < propertyName.length; i++)
        {
            if (i > 0)
                buffer.append(PropertyHelper.PATH_SEPERATOR);

            buffer.append(propertyName[i]);

            result.add(new ExplorePathElement(buffer.toString(), propertyName[i]));
        }

        return result;
    }

    private Object getExploredObject()
    {
        Inspector inspector = (Inspector) page;

        return inspector.getExploredObject();
    }

    /**
     *  Gets the class name of the explored object.  This does some minor
     *  translation to be more useful with array types.
     *
     **/

    public String getExploredClassName()
    {
        Object explored = getExploredObject();

        if (explored == null)
            return "<Null>";

        return convertClassToName(explored.getClass());
    }

    private String convertClassToName(Class cl)
    {
        // TODO: This only handles one-dimensional arrays
        // property.

        if (cl.isArray())
            return "array of " + cl.getComponentType().getName();

        return cl.getName();
    }

    public String getExploredValue()
    {
        Object explored = getExploredObject();

        return (explored == null) ? "<null>" : explored.toString();
    }

    public void exploreComponent(IRequestCycle cycle)
    {
        Inspector inspector = (Inspector) page;

        inspector.setExplorePath(null);
    }

    public void selectExplorePath(IRequestCycle cycle)
    {
        String[] parameters = cycle.getServiceParameters();
        Inspector inspector = (Inspector) page;

        String fullPath = parameters[0];
        String[] splitPath = PropertyHelper.splitPropertyPath(fullPath);

        StringBuffer buffer = new StringBuffer();

        Object focus = inspector.getInspectedComponent();

        for (int i = 0; i < splitPath.length; i++)
        {
            String name = splitPath[i];

            PropertyHelper helper = PropertyHelper.forInstance(focus);

            try
            {
                focus = helper.get(focus, name);
            }
            catch (Throwable ex)
            {
                errorPropertyName = name;

                propertyException = new ExceptionAnalyzer().analyze(ex);

                break;
            }

            if (i > 0)
                buffer.append(PropertyHelper.PATH_SEPERATOR);

            buffer.append(name);
        }

        // Inform the page about the object we've explored.

        inspector.setExplorePath(buffer.toString());
    }

    /**
     *  Returns a List of AccessorElement.
     *
     **/

    public List getAccessors()
    {
        Object explored = getExploredObject();
        if (explored == null)
            return null;

        Inspector inspector = (Inspector) page;
        String currentPath = inspector.getExplorePath();

        StringBuffer buffer = new StringBuffer();

        if (currentPath != null)
        {
            buffer.append(currentPath);
            buffer.append(PropertyHelper.PATH_SEPERATOR);
        }

        int baseLength = buffer.length();

        List result = new ArrayList();
        PropertyHelper helper = PropertyHelper.forInstance(explored);

        Collection accessors = helper.getAccessors(explored);

        Iterator i = accessors.iterator();
        while (i.hasNext())
        {
            IPropertyAccessor ac = (IPropertyAccessor) i.next();

            if (ac.isReadable())
            {
                AccessorElement element = new AccessorElement();
                element.propertyName = ac.getName();

                buffer.setLength(baseLength);
                buffer.append(element.propertyName);
                element.propertyPath = buffer.toString();

                element.propertyType = convertClassToName(ac.getType());

                element.error = element.propertyName.equals(errorPropertyName);

                result.add(element);
            }
        }

        Collections.sort(result);
        return result;
    }

    public ExceptionDescription[] getPropertyException()
    {
        return propertyException;
    }

    public String getErrorPropertyName()
    {
        return errorPropertyName;
    }
}