package tutorial.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  Central object tracking the available Portlets and Channels.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Visit implements Serializable
{
    private static PortletChannel[] channels =
        {
            new PortletChannel(87, "Slashdot Headlines", "Slashdot", "content"),
            new PortletChannel(23, "Stock Quotes", "Stocks", "content"),
            new PortletChannel(373, "Weather", "Weather", "content")};

    private List models;

    public void removeModel(PortletModel model)
    {
        if (models != null)
            models.remove(model);
    }

    public Collection getModels()
    {
        return models;
    }

    public void addModel(int id)
    {
        for (int i = 0; i < channels.length; i++)
        {
            PortletChannel channel = channels[i];

            if (channel.getId() == id)
            {
                PortletModel model = channel.getModel();

                if (models == null)
                    models = new ArrayList();

                models.add(model);

                return;
            }
        }

        throw new ApplicationRuntimeException("No portlet channel with id " + id + ".");
    }

    /**
     *  Returns a portlet selection model that will produce a list
     *  of all portlets <em>not already in use</em>.  It will generate
     *  an integer property which is the id of they selected
     *  porlet, suitable for passing to {@link #addModel(int)}.
     *
     */

    public IPropertySelectionModel getPortletSelectionModel()
    {
        PortletSelectionModel model = new PortletSelectionModel();

        for (int i = 0; i < channels.length; i++)
        {
            PortletChannel channel = channels[i];

            if (!inUse(channel.getId()))
                model.add(channel);
        }

        return model;
    }

    private boolean inUse(int id)
    {
        if (models == null)
            return false;

        int count = models.size();
        for (int i = 0; i < count; i++)
        {
            PortletModel model = (PortletModel) models.get(i);

            if (model.getId() == id)
                return true;
        }

        return false;
    }

    /**
     *  Returns true if there are any additional portlets that the user has not
     *  already added.  If it returns false, then the UI should omit the
     *  form for adding a new portlet.
     *
     */

    public boolean getMaySelectPortlet()
    {
        if (models == null)
            return true;

        return models.size() < channels.length;
    }
}