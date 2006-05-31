// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.timetracker.page;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.dojo.form.Autocompleter;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.DatePicker;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.timetracker.dao.ProjectDao;
import org.apache.tapestry.timetracker.model.Project;


/**
 * Manages entering tasks.
 *
 * @author jkuhnert
 */
public abstract class TaskEntryPage extends BasePage
{
    private static final Log _log = LogFactory.getLog(TaskEntryPage.class);
    
    @Component(type = "Autocompleter", id = "projectChoose",
            bindings = { "model=projectModel", "value=selectedProject",
            "displayName=message:choose.project", "filterOnChange=ognl:true"})
    public abstract Autocompleter getProjectSelection();
    
    @InjectObject("service:timetracker.dao.ProjectDao")
    public abstract ProjectDao getProjectDao();
    
    public abstract Project getSelectedProject();
    
    @Component(type = "DatePicker", id = "startPicker",
            bindings = {"value=startTime"})
    public abstract DatePicker getStartPicker();
    
    public abstract Date getStartTime();
    
    /**
     * Selection model for projects.
     * @return
     */
    public IPropertySelectionModel getProjectModel()
    {
        return new BeanPropertySelectionModel(getProjectDao().listProjects(), "name");
    }
    
    /**
     * Invoked by form to add a new task.
     */
    public void addTask()
    {
        _log.debug("addTask() selected task is " + getSelectedProject().getName());
    }
    
    /**
     * Invoked when an item is selected from the project
     * selection list.
     */
    @EventListener(events = "selectOption", targets = "projectChoose")
    public void projectSelected()
    {
        _log.debug("projectSelected()");
        throw new ApplicationRuntimeException("Oops!");
    }
}
