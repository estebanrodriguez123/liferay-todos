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

package com.rivetlogic.portlet.todo.model;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PortalUtil;

import com.rivetlogic.portlet.todo.service.ClpSerializer;
import com.rivetlogic.portlet.todo.service.TaskLocalServiceUtil;

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christopher Jimenez
 */
public class TaskClp extends BaseModelImpl<Task> implements Task {
	public TaskClp() {
	}

	@Override
	public Class<?> getModelClass() {
		return Task.class;
	}

	@Override
	public String getModelClassName() {
		return Task.class.getName();
	}

	@Override
	public long getPrimaryKey() {
		return _taskId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setTaskId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _taskId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("taskId", getTaskId());
		attributes.put("userId", getUserId());
		attributes.put("name", getName());
		attributes.put("description", getDescription());
		attributes.put("date", getDate());
		attributes.put("completed", getCompleted());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long taskId = (Long)attributes.get("taskId");

		if (taskId != null) {
			setTaskId(taskId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Date date = (Date)attributes.get("date");

		if (date != null) {
			setDate(date);
		}

		Boolean completed = (Boolean)attributes.get("completed");

		if (completed != null) {
			setCompleted(completed);
		}
	}

	@Override
	public long getTaskId() {
		return _taskId;
	}

	@Override
	public void setTaskId(long taskId) {
		_taskId = taskId;

		if (_taskRemoteModel != null) {
			try {
				Class<?> clazz = _taskRemoteModel.getClass();

				Method method = clazz.getMethod("setTaskId", long.class);

				method.invoke(_taskRemoteModel, taskId);
			}
			catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;

		if (_taskRemoteModel != null) {
			try {
				Class<?> clazz = _taskRemoteModel.getClass();

				Method method = clazz.getMethod("setUserId", long.class);

				method.invoke(_taskRemoteModel, userId);
			}
			catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	@Override
	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	@Override
	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;

		if (_taskRemoteModel != null) {
			try {
				Class<?> clazz = _taskRemoteModel.getClass();

				Method method = clazz.getMethod("setName", String.class);

				method.invoke(_taskRemoteModel, name);
			}
			catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	@Override
	public String getDescription() {
		return _description;
	}

	@Override
	public void setDescription(String description) {
		_description = description;

		if (_taskRemoteModel != null) {
			try {
				Class<?> clazz = _taskRemoteModel.getClass();

				Method method = clazz.getMethod("setDescription", String.class);

				method.invoke(_taskRemoteModel, description);
			}
			catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	@Override
	public Date getDate() {
		return _date;
	}

	@Override
	public void setDate(Date date) {
		_date = date;

		if (_taskRemoteModel != null) {
			try {
				Class<?> clazz = _taskRemoteModel.getClass();

				Method method = clazz.getMethod("setDate", Date.class);

				method.invoke(_taskRemoteModel, date);
			}
			catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	@Override
	public Boolean getCompleted() {
		return _completed;
	}

	@Override
	public void setCompleted(Boolean completed) {
		_completed = completed;

		if (_taskRemoteModel != null) {
			try {
				Class<?> clazz = _taskRemoteModel.getClass();

				Method method = clazz.getMethod("setCompleted", Boolean.class);

				method.invoke(_taskRemoteModel, completed);
			}
			catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	public BaseModel<?> getTaskRemoteModel() {
		return _taskRemoteModel;
	}

	public void setTaskRemoteModel(BaseModel<?> taskRemoteModel) {
		_taskRemoteModel = taskRemoteModel;
	}

	public Object invokeOnRemoteModel(String methodName,
		Class<?>[] parameterTypes, Object[] parameterValues)
		throws Exception {
		Object[] remoteParameterValues = new Object[parameterValues.length];

		for (int i = 0; i < parameterValues.length; i++) {
			if (parameterValues[i] != null) {
				remoteParameterValues[i] = ClpSerializer.translateInput(parameterValues[i]);
			}
		}

		Class<?> remoteModelClass = _taskRemoteModel.getClass();

		ClassLoader remoteModelClassLoader = remoteModelClass.getClassLoader();

		Class<?>[] remoteParameterTypes = new Class[parameterTypes.length];

		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i].isPrimitive()) {
				remoteParameterTypes[i] = parameterTypes[i];
			}
			else {
				String parameterTypeName = parameterTypes[i].getName();

				remoteParameterTypes[i] = remoteModelClassLoader.loadClass(parameterTypeName);
			}
		}

		Method method = remoteModelClass.getMethod(methodName,
				remoteParameterTypes);

		Object returnValue = method.invoke(_taskRemoteModel,
				remoteParameterValues);

		if (returnValue != null) {
			returnValue = ClpSerializer.translateOutput(returnValue);
		}

		return returnValue;
	}

	@Override
	public void persist() throws SystemException {
		if (this.isNew()) {
			TaskLocalServiceUtil.addTask(this);
		}
		else {
			TaskLocalServiceUtil.updateTask(this);
		}
	}

	@Override
	public Task toEscapedModel() {
		return (Task)ProxyUtil.newProxyInstance(Task.class.getClassLoader(),
			new Class[] { Task.class }, new AutoEscapeBeanHandler(this));
	}

	@Override
	public Object clone() {
		TaskClp clone = new TaskClp();

		clone.setTaskId(getTaskId());
		clone.setUserId(getUserId());
		clone.setName(getName());
		clone.setDescription(getDescription());
		clone.setDate(getDate());
		clone.setCompleted(getCompleted());

		return clone;
	}

	@Override
	public int compareTo(Task task) {
		int value = 0;

		value = DateUtil.compareTo(getDate(), task.getDate());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TaskClp)) {
			return false;
		}

		TaskClp task = (TaskClp)obj;

		long primaryKey = task.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{taskId=");
		sb.append(getTaskId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", date=");
		sb.append(getDate());
		sb.append(", completed=");
		sb.append(getCompleted());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(22);

		sb.append("<model><model-name>");
		sb.append("com.rivetlogic.portlet.todo.model.Task");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>taskId</column-name><column-value><![CDATA[");
		sb.append(getTaskId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>description</column-name><column-value><![CDATA[");
		sb.append(getDescription());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>date</column-name><column-value><![CDATA[");
		sb.append(getDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>completed</column-name><column-value><![CDATA[");
		sb.append(getCompleted());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private long _taskId;
	private long _userId;
	private String _userUuid;
	private String _name;
	private String _description;
	private Date _date;
	private Boolean _completed;
	private BaseModel<?> _taskRemoteModel;
}