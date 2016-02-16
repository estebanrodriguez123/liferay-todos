/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.rivetlogic.portlet.todo.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link TaskService}.
 *
 * @author Christopher Jimenez
 * @see TaskService
 * @generated
 */
public class TaskServiceWrapper implements TaskService,
	ServiceWrapper<TaskService> {
	public TaskServiceWrapper(TaskService taskService) {
		_taskService = taskService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _taskService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_taskService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _taskService.invokeMethod(name, parameterTypes, arguments);
	}

	@Override
	public com.rivetlogic.portlet.todo.model.Task createTask(
		com.rivetlogic.portlet.todo.model.Task task)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _taskService.createTask(task);
	}

	@Override
	public java.util.List<com.rivetlogic.portlet.todo.model.Task> getTaskByUserId(
		java.lang.Long userId) {
		return _taskService.getTaskByUserId(userId);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public TaskService getWrappedTaskService() {
		return _taskService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedTaskService(TaskService taskService) {
		_taskService = taskService;
	}

	@Override
	public TaskService getWrappedService() {
		return _taskService;
	}

	@Override
	public void setWrappedService(TaskService taskService) {
		_taskService = taskService;
	}

	private TaskService _taskService;
}