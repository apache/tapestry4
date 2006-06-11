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
package org.apache.tapestry.timetracker.dao;

import java.sql.PreparedStatement;

import org.apache.tapestry.timetracker.model.Task;


/**
 * Manages DB operations for {@link Task}s.
 * 
 * @author jkuhnert
 */
public class TaskDao extends BaseDao
{
    
    /**
     * Adds a new task to the project.
     * @param task 
     *          The pre-populated task to add.
     */
    public void addTask(Task task)
    {
        PreparedStatement ps = null;
        
        try {
            ps = _conn.prepareStatement("insert into tasks (project_id, start_dt, end_dt, "
                    + "descr_txt) values (?, ?, ?, ?) ");
            int x=0;
            ps.setLong(++x, task.getProjectId());
            ps.setTimestamp(++x, new java.sql.Timestamp(task.getStartDate().getTime()));
            ps.setTimestamp(++x, new java.sql.Timestamp(task.getEndDate().getTime()));
            ps.setString(++x, task.getDescription());
            ps.execute();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { }
        }
    }
}
