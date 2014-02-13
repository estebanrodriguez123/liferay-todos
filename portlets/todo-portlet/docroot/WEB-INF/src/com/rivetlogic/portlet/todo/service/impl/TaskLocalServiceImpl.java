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

package com.rivetlogic.portlet.todo.service.impl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.rivetlogic.portlet.todo.model.Task;
import com.rivetlogic.portlet.todo.service.base.TaskLocalServiceBaseImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the task local service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link com.rivetlogic.portlet.todo.service.TaskLocalService} interface.
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Christopher Jimenez
 * @see com.rivetlogic.portlet.todo.service.base.TaskLocalServiceBaseImpl
 * @see com.rivetlogic.portlet.todo.service.TaskLocalServiceUtil
 */
public class TaskLocalServiceImpl extends TaskLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     * 
     * Never reference this interface directly. Always use {@link
     * com.rivetlogic.portlet.todo.service.TaskLocalServiceUtil} to access the
     * task local service.
     */
    private static final Log LOG = LogFactoryUtil.getLog(TaskLocalServiceImpl.class);
    
    public Task createTask(Task task) throws SystemException {
        Task newTask = taskPersistence.create(counterLocalService.increment(Task.class.getName()));
        newTask.setCompleted(task.getCompleted());
        newTask.setDate(task.getDate());
        newTask.setDescription(task.getDescription());
        newTask.setName(task.getName());
        newTask.setUserId(task.getUserId());
        taskPersistence.update(newTask);
        return newTask;
    }
    
    public List<Task> getTaskByUserId(Long userId) {
        List<Task> tasks = new ArrayList<Task>();
        try {
            tasks = taskPersistence.findByuserId(userId);
        } catch (Exception e) {
            LOG.error(e);
        }
        return tasks;
    }
}