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
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.valid.IField;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.valid.ValidationDelegate;
import net.sf.tapestry.valid.ValidatorException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *  Component of the {@link Inspector} page used control log4j logging
 *  behavior.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 **/

public class ShowLogging extends BaseComponent implements PageDetachListener
{
    private Logger _logger;
    private String _newLogger;
    private IValidationDelegate _validationDelegate;
    private IPropertySelectionModel _rootLevelModel;
    private IPropertySelectionModel _levelModel;

    /**
     *  Registers this component as a {@link PageDetachListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        getPage().addPageDetachListener(this);
    }

    /**
     *  Clears out everything when the page is detached.
     *
     *  @since 1.0.5
     *
     **/

    public void pageDetached(PageEvent event)
    {
        _logger = null;
        _newLogger = null;
    }

    public String getNewLogger()
    {
        return _newLogger;
    }

    public void setNewLogger(String value)
    {
        _newLogger = value;
    }

    public void setLoggerName(String value)
    {
        _logger = LogManager.getLogger(value);
    }

    public Logger getLogger()
    {
        return _logger;
    }

    /**
     *  Returns a sorted list of all known loggers.
     *
     **/

    public List getLoggerNames()
    {
        List result = new ArrayList();
        Enumeration e;

        e = LogManager.getCurrentLoggers();
        while (e.hasMoreElements())
        {
            Logger l = (Logger) e.nextElement();

            result.add(l.getName());
        }

        Collections.sort(result);

        return result;
    }

    public Logger getRootLogger()
    {
        return LogManager.getRootLogger();
    }

    /**
     *  Returns a {@link IPropertySelectionModel} for {@link org.apache.log4j.Level}
     *  that does not allow a null value to be selected.
     *
     **/

    public IPropertySelectionModel getRootLevelModel()
    {
        if (_rootLevelModel == null)
            _rootLevelModel = new PriorityModel(false);

        return _rootLevelModel;
    }

    /**
     *  Returns a {@link IPropertySelectionModel} for 
     *  {@link org.apache.log4j.Level}
     *  include a null option.
     *
     **/

    public IPropertySelectionModel getLevelModel()
    {
        if (_levelModel == null)
            _levelModel = new PriorityModel();

        return _levelModel;
    }

    public IValidationDelegate getValidationDelegate()
    {
        if (_validationDelegate == null)
            _validationDelegate = new ValidationDelegate();

        return _validationDelegate;
    }

    public void levelChange(IRequestCycle cycle)
    {
        // Do nothing.  This will redisplay the logging page after the
        // priorities are updated.
    }

    public void addNewLogger(IRequestCycle cycle)
    {
        // If the validating text field has an error, then go no further.

        IValidationDelegate delegate =
            (IValidationDelegate) getBeans().getBean("delegate");

        if (delegate.getHasErrors())
            return;

        if (LogManager.exists(_newLogger) == null)
        {
            // Force the new category into existence

            LogManager.getLogger(_newLogger);
            // Clear the field
            _newLogger = null;

            return;
        }

        IField field = (IField) getComponent("inputNewLogger");

        delegate.setFormComponent(field);
        delegate.record(
            new ValidatorException(
                "Logger " + _newLogger + " already exists.",
                null,
                _newLogger));

    }
}