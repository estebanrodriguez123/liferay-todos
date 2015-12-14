/**
 * Copyright (C) 2005-2014 Rivet Logic Corporation.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.rivetlogic.portlet.todo.bean;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.rivetlogic.portlet.todo.model.Task;
import com.rivetlogic.portlet.todo.service.TaskLocalServiceUtil;
import com.rivetlogic.portlet.todo.util.TodoUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author christopherjimenez
 * 
 */
public class TasksBean {
    
    private List<Task> previousTasks;
    private List<Task> todayTasks;
    private List<Task> tomorrowTasks;
    private List<Task> futureTasks;
    
    private static final int ONE_MORE_DAY = 1;
    
    private static final String JSON_DATA_PREVIOUS_TASKS = "previousTasks";
    private static final String JSON_DATA_TODAY_TASKS = "todayTasks";
    private static final String JSON_DATA_TOMORROW_TASKS = "tomorrowTasks";
    private static final String JSON_DATA_FUTURE_TASKS = "futureTasks";
    
    public static final String JSON_TASK_DATA_ID = "taskId";
    public static final String JSON_TASK_DATA_NAME = "name";
    public static final String JSON_TASK_DATA_DESCRIPTION = "description";
    public static final String JSON_TASK_DATA_IS_COMPLETED = "isCompleted";
    public static final String JSON_TASK_DATA_DATE = "date";
    public static final String JSON_TASK_DATA_CALENDAR_ID = "calendarId";
    
    public static final String ACTION_KEY_MANAGE_BOOKINGS = "MANAGE_BOOKINGS";
    
    
    public TasksBean(Long userId, Date now) {
        
        previousTasks = new ArrayList<Task>();
        todayTasks = new ArrayList<Task>();
        tomorrowTasks = new ArrayList<Task>();
        futureTasks = new ArrayList<Task>();
        
        Calendar today = TodoUtil.getCalendarWithOutTime(now);

        Calendar tomorrow = TodoUtil.getCalendarWithOutTime(now);
        tomorrow.add(Calendar.DATE, ONE_MORE_DAY);
        
        List<Task> allTasks = TaskLocalServiceUtil.getTaskByUserId(userId);
        
        for (Task task : allTasks){
            
            Calendar taskDate = TodoUtil.getCalendarWithOutTime(task.getDate());
            
            if (taskDate.equals(today)) {
                todayTasks.add(task);
            }
            else if (taskDate.before(today)) {
                previousTasks.add(task);
            }
            else if (taskDate.equals(tomorrow)) {
                tomorrowTasks.add(task);
            }
            else {
                futureTasks.add(task);
            }
        }

    }
    
    public List<Task> getPreviousTasks() {
        return previousTasks;
    }
    
    public List<Task> getTodayTasks() {
        return todayTasks;
    }
    
    public List<Task> getTomorrowTasks() {
        return tomorrowTasks;
    }
    
    public List<Task> getFutureTasks() {
        return futureTasks;
    }
    
    public void setPreviousTasks(List<Task> previousTasks) {
        this.previousTasks = previousTasks;
    }
    
    public void setTodayTasks(List<Task> todayTasks) {
        this.todayTasks = todayTasks;
    }
    
    public void setTomorrowTasks(List<Task> tomorrowTasks) {
        this.tomorrowTasks = tomorrowTasks;
    }
    
    public void setFutureTasks(List<Task> futureTasks) {
        this.futureTasks = futureTasks;
    }
    
    public JSONObject toJSON() {
        JSONObject document = JSONFactoryUtil.createJSONObject();
        
        document.put(JSON_DATA_PREVIOUS_TASKS, tasksToJsonArray(this.previousTasks));
        document.put(JSON_DATA_TODAY_TASKS, tasksToJsonArray(this.todayTasks));
        document.put(JSON_DATA_TOMORROW_TASKS, tasksToJsonArray(this.tomorrowTasks));
        document.put(JSON_DATA_FUTURE_TASKS, tasksToJsonArray(this.futureTasks));
        
        return document;
    }
    
    private JSONArray tasksToJsonArray(List<Task> tasks) {
        JSONArray array = JSONFactoryUtil.createJSONArray();
        for (Task t : tasks) {
            array.put(taskToJson(t));
        }
        return array;
    }
    
    private JSONObject taskToJson(Task task) {
        JSONObject document = JSONFactoryUtil.createJSONObject();
        document.put(JSON_TASK_DATA_ID, task.getTaskId());
        document.put(JSON_TASK_DATA_NAME, task.getName());
        document.put(JSON_TASK_DATA_DESCRIPTION, task.getDescription());
        document.put(JSON_TASK_DATA_IS_COMPLETED, task.getCompleted());
        document.put(JSON_TASK_DATA_DATE, TodoUtil.SDF.format(task.getDate()));
        return document;
    }
}
