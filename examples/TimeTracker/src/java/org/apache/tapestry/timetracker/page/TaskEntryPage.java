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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.dojo.form.Autocompleter;
import org.apache.tapestry.dojo.form.DropdownDatePicker;
import org.apache.tapestry.dojo.form.DropdownTimePicker;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.TextField;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.timetracker.dao.ProjectDao;
import org.apache.tapestry.timetracker.dao.TaskDao;
import org.apache.tapestry.timetracker.model.Project;
import org.apache.tapestry.timetracker.model.Task;


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
            "displayName=message:choose.project", "filterOnChange=true"})
    public abstract Autocompleter getProjectSelection();
    
    @InjectObject("service:timetracker.dao.ProjectDao")
    public abstract ProjectDao getProjectDao();
    
    @Persist("session")
    public abstract Project getSelectedProject();
    
    public abstract Project getCurrentProject();
    
    @Component(type = "DropdownDatePicker", id = "datePicker",
            bindings = {"value=date", "displayName=message:task.start.date"})
    public abstract DropdownDatePicker getDatePicker();
    public abstract Date getDate();
    
    @Component(type = "DropdownTimePicker", id = "startPicker",
            bindings = {"value=startTime", "displayName=message:task.start.time"})
    public abstract DropdownTimePicker getStartPicker();
    public abstract Date getStartTime();
    
    @Component(type = "DropdownTimePicker", id = "endPicker",
            bindings = {"value=endTime", "displayName=message:task.end.time"})
    public abstract DropdownTimePicker getEndPicker();
    public abstract Date getEndTime();
    
    @Component(type = "TextField", id = "descriptionField",
            bindings = { "value=description", 
            "displayName=message:task.description"})
    public abstract TextField getDescriptionField();
    public abstract String getDescription();
    
    @InjectObject("service:timetracker.dao.TaskDao")
    public abstract TaskDao getTaskDao();
    
    /**
     * Selection model for projects.
     * @return
     */
    public IPropertySelectionModel getProjectModel()
    {
        return new BeanPropertySelectionModel(getProjectDao().listProjects(), "name");
    }
    
    /**
     * Invoked when an item is selected from the project
     * selection list.
     */
    @EventListener(events = "selectOption", targets = "projectChoose",
            submitForm = "taskForm")
    public void projectSelected(IRequestCycle cycle)
    {
        _log.debug("projectSelected() " + getSelectedProject());
        cycle.getResponseBuilder().updateComponent("projectDescription");
    }
    
    /**
     * Invoked by form to add a new task.
     */
    public void addTask()
    {
        Task task = new Task();
        task.setProjectId(getSelectedProject().getId());
        task.setDescription(getDescription());
        task.setStartDate(getStartTime());
        task.setEndDate(getEndTime());
        
        getTaskDao().addTask(task);
    }
}
