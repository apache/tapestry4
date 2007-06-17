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

import org.apache.log4j.Logger;
import org.apache.tapestry.annotations.*;
import org.apache.tapestry.dojo.form.*;
import org.apache.tapestry.dojo.html.Dialog;
import org.apache.tapestry.form.TextField;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.timetracker.dao.GenericDao;
import org.apache.tapestry.timetracker.dao.TaskDao;
import org.apache.tapestry.timetracker.model.Persistent;
import org.apache.tapestry.timetracker.model.Project;
import org.apache.tapestry.timetracker.model.Task;

import java.util.Date;


/**
 * Manages entering tasks.
 *
 * @author jkuhnert
 */
public abstract class TaskEntryPage<E extends Persistent> extends BasePage
{   
    private static final Logger _log = Logger.getLogger(TaskEntryPage.class);
    
    @Component(id = "projectChoose", bindings = { "model=projectModel", "value=selectedProject",
            "displayName=message:choose.project",
            "validators=validators:required"})
    public abstract Autocompleter getProjectChoose();
    
    @InjectObject("service:timetracker.dao.ProjectDao")
    public abstract GenericDao<E> getProjectDao();
    
    @Persist
    public abstract E getSelectedProject();
    public abstract void setSelectedProject(E val);
    
    public abstract void setSubProject(E value);
    public abstract E getSubProject();
    
    @Persist
    public abstract void setShowSubProject(boolean show);
    public abstract boolean getShowSubProject();
    
    public abstract Project getCurrentProject();
    
    @Component(bindings = {"value=date", 
            "displayName=message:task.start.date"})
    public abstract DropdownDatePicker getDatePicker();
    public abstract Date getDate();
    
    @Component(bindings = {"value=startTime", "displayName=message:task.start.time",
            "validators=validators:required"})
    public abstract DropdownTimePicker getStartPicker();
    public abstract Date getStartTime();
    
    @Component(bindings = {"value=endTime", "displayName=message:task.end.time",
            "validators=validators:required,differ=startPicker"})
    public abstract DropdownTimePicker getEndPicker();
    public abstract Date getEndTime();
    
    @Component(bindings = { "value=description", 
            "displayName=message:task.description",
            "validators=validators:required,maxLength=20"})
    public abstract TextField getDescriptionField();
    public abstract String getDescription();
    
    @InjectObject("service:timetracker.dao.TaskDao")
    public abstract TaskDao getTaskDao();
    
    public abstract ResponseBuilder getBuilder();

    @InjectComponent("testDialog")
    public abstract Dialog getTestDialog();

    /**
     * Selection model for projects.
     * 
     * @return The project model.
     */
    public IAutocompleteModel getProjectModel()
    {
        return new DefaultAutocompleteModel(getProjectDao().list(), "id", "name");
    }
    
    /**
     * Invoked when an item is selected from the project
     * selection list.
     */
    @EventListener(events = "onValueChanged", targets = "projectChoose")
    public void projectSelected()
    {
        getBuilder().updateComponent("projectDescription");        
    }

    public void showDialog()
    {
        getTestDialog().show();
    }

    /**
     * Invoked by form to add a new task.
     */
    public void addTask()
    {
        Task task = new Task();
        task.setProjectId(getSelectedProject().getId());
        task.setDescription(getDescription());
        
        _log.debug("addTask date: " + getDate()
                + "\n startTime: " + getStartTime()
                + "\n endTime: " + getEndTime());
        
        task.setStartDate(getStartTime());
        task.setEndDate(getEndTime());
        
        getTaskDao().addTask(task);
    }

    @EventListener(events = "onSave", targets="projName")
    public void onNameUpdate()
    {
        if (getSelectedProject() != null) {
            getProjectDao().update(getSelectedProject());
            
            getBuilder().updateComponent("projectChoose");
            setSelectedProject(getSelectedProject());
        }
    }

    public void showSubProject()
    {
        setShowSubProject(true);
    }
}
