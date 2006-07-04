// Copyright Jun 11, 2006 The Apache Software Foundation
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
package org.apache.tapestry.timetracker.model;

import java.io.Serializable;
import java.util.Date;


/**
 * A time entry task for a {@link Project}.
 * 
 * @author jkuhnert
 */
public class Task implements Serializable
{
    /** generated. */
    private static final long serialVersionUID = 7257377128183799387L;
    
    protected long _id;
    protected long _projectId;
    protected Date _startDate;
    protected Date _endDate;
    protected String _description;
    
    public Task() { }
    
    /**
     * @return the description
     */
    public String getDescription()
    {
        return _description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        _description = description;
    }
    
    /**
     * @return the endDate
     */
    public Date getEndDate()
    {
        return _endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate)
    {
        _endDate = endDate;
    }

    /**
     * @return the id
     */
    public long getId()
    {
        return _id;
    }

    
    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        _id = id;
    }

    /**
     * @return the projectId
     */
    public long getProjectId()
    {
        return _projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(long projectId)
    {
        _projectId = projectId;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate()
    {
        return _startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate)
    {
        _startDate = startDate;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_description == null) ? 0 : _description.hashCode());
        result = prime * result + ((_endDate == null) ? 0 : _endDate.hashCode());
        result = prime * result + (int) (_id ^ (_id >>> 32));
        result = prime * result + (int) (_projectId ^ (_projectId >>> 32));
        result = prime * result + ((_startDate == null) ? 0 : _startDate.hashCode());
        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Task other = (Task) obj;
        if (_description == null) {
            if (other._description != null) return false;
        } else if (!_description.equals(other._description)) return false;
        if (_endDate == null) {
            if (other._endDate != null) return false;
        } else if (!_endDate.equals(other._endDate)) return false;
        if (_id != other._id) return false;
        if (_projectId != other._projectId) return false;
        if (_startDate == null) {
            if (other._startDate != null) return false;
        } else if (!_startDate.equals(other._startDate)) return false;
        return true;
    }
}
