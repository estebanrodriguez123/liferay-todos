/*
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

package com.rivetlogic.portlet.todo;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.rivetlogic.portlet.todo.bean.TasksBean;
import com.rivetlogic.portlet.todo.model.Task;
import com.rivetlogic.portlet.todo.model.impl.TaskImpl;
import com.rivetlogic.portlet.todo.service.TaskLocalServiceUtil;
import com.rivetlogic.portlet.todo.util.TodoUtil;
import com.rivetlogic.portlet.todo.validator.TodoValidator;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.portlet.PortletException;
import javax.portlet.PortletResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author christopherjimenez
 * 
 */
public class TodoPortlet extends MVCPortlet {
    private static final Log LOG = LogFactoryUtil.getLog(TodoPortlet.class);
    
    private static final String COMMAND_KEY = "cmd";
    private static final String COMMAND_SUCCESS = "success";
    private static final String COMMAND_TOGGLE_TASK = "toggle-task";
    private static final String COMMAND_LIST_TASKS = "list-tasks";
    private static final String COMMAND_ADD_TASK = "add-task";
    private static final String COMMAND_DELETE_TASK = "delete-task";
    private static final String COMMAND_UPDATE_TASK = "update-task";
    private static final String DATE_DAY = "day";
    private static final String DATE_MONTH = "month";
    private static final String DATE_YEAR = "year";
    
    private static final int UNDEFINED_ID = -1;
    private static final int DEFAULT_INT_VALUE = 0;
    
    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException,
        PortletException {
        HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil
                .getHttpServletRequest(resourceRequest));
        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
        JSONObject jsonObject = null;
        String cmd = ParamUtil.getString(httpReq, COMMAND_KEY);
        if (COMMAND_TOGGLE_TASK.equals(cmd)) {
            jsonObject = JSONFactoryUtil.createJSONObject();
            toggleTask(httpReq, jsonObject);
        }
        if (COMMAND_LIST_TASKS.equals(cmd)) {
            listTasks(resourceResponse, themeDisplay);
        }
        if (COMMAND_ADD_TASK.equals(cmd)) {
            jsonObject = JSONFactoryUtil.createJSONObject();
            addTask(httpReq, jsonObject);
        }
        if (COMMAND_DELETE_TASK.equals(cmd)) {
            jsonObject = JSONFactoryUtil.createJSONObject();
            deleteTask(httpReq, jsonObject);
        }
        if (COMMAND_UPDATE_TASK.equals(cmd)) {
            jsonObject = JSONFactoryUtil.createJSONObject();
            updateTask(httpReq, jsonObject);
        }
        
        if (jsonObject != null) {
            TodoUtil.returnJSONObject(resourceResponse, jsonObject);
        }
    }
    
    private void toggleTask(HttpServletRequest request, JSONObject jsonObject) {
        Long taskId = ParamUtil.getLong(request, TasksBean.JSON_TASK_DATA_ID, UNDEFINED_ID);
        if (taskId != UNDEFINED_ID) {
            try {
                Task task = TaskLocalServiceUtil.getTask(taskId);
                task.setCompleted(!task.getCompleted());
                TaskLocalServiceUtil.updateTask(task);
                jsonObject.put(COMMAND_SUCCESS, true);
            } catch (Exception e) {
                jsonObject.put(COMMAND_SUCCESS, false);
                LOG.error(e);
            }
        } else {
            jsonObject.put(COMMAND_SUCCESS, false);
        }
    }
    
    private void listTasks(PortletResponse response, ThemeDisplay themeDisplay) {
        TasksBean tb = new TasksBean(themeDisplay.getUserId(), new Date());
        TodoUtil.returnJSONObject(response, tb.toJSON());
    }
    
    private void addTask(HttpServletRequest request, JSONObject jsonObject) {
        Task task = createTaskFromRequest(request);
        if (TodoValidator.validateNewTask(task)) {
            try {
                task = TaskLocalServiceUtil.createTask(task);
                jsonObject.put(COMMAND_SUCCESS, true);
                jsonObject.put(TasksBean.JSON_TASK_DATA_ID, task.getTaskId());
            } catch (Exception e) {
                jsonObject.put(COMMAND_SUCCESS, false);
                LOG.error(e);
            }
        } else {
            jsonObject.put(COMMAND_SUCCESS, false);
        }
        
    }
    
    private void deleteTask(HttpServletRequest request, JSONObject jsonObject) {
        Long taskId = ParamUtil.getLong(request, TasksBean.JSON_TASK_DATA_ID, UNDEFINED_ID);
        if (taskId != UNDEFINED_ID) {
            try {
                TaskLocalServiceUtil.deleteTask(taskId);
                jsonObject.put(COMMAND_SUCCESS, true);
            } catch (Exception e) {
                jsonObject.put(COMMAND_SUCCESS, false);
                LOG.error(e);
            }
        } else {
            jsonObject.put(COMMAND_SUCCESS, false);
        }
    }
    
    private void updateTask(HttpServletRequest request, JSONObject jsonObject) {
        Long taskId = ParamUtil.getLong(request, TasksBean.JSON_TASK_DATA_ID, UNDEFINED_ID);
        try {
            Task task = TaskLocalServiceUtil.getTask(taskId);
            setCommonTaskFields(request, task);
            if (TodoValidator.validateNewTask(task)) {
                task = TaskLocalServiceUtil.updateTask(task);
                jsonObject.put(COMMAND_SUCCESS, true);
                jsonObject.put(TasksBean.JSON_TASK_DATA_ID, task.getTaskId());
            } else {
                jsonObject.put(COMMAND_SUCCESS, false);
            }
        } catch (Exception e) {
            jsonObject.put(COMMAND_SUCCESS, false);
            LOG.error(e);
        }
    }
    
    private void setCommonTaskFields(HttpServletRequest request, Task task) {
        Calendar calendar = getDateFromRequest(request);
        task.setDate(calendar.getTime());
        task.setDescription(ParamUtil.getString(request, TasksBean.JSON_TASK_DATA_DESCRIPTION, StringPool.BLANK));
        task.setName(ParamUtil.getString(request, TasksBean.JSON_TASK_DATA_NAME, StringPool.BLANK));
    }
    
    private Task createTaskFromRequest(HttpServletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        Task task = new TaskImpl();
        task.setCompleted(false);
        task.setUserId(themeDisplay.getUserId());
        setCommonTaskFields(request, task);
        return task;
    }
    
    private Calendar getDateFromRequest(HttpServletRequest request) {
        int day = ParamUtil.getInteger(request, DATE_DAY, DEFAULT_INT_VALUE);
        int month = ParamUtil.getInteger(request, DATE_MONTH, DEFAULT_INT_VALUE);
        int year = ParamUtil.getInteger(request, DATE_YEAR, DEFAULT_INT_VALUE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }
}
