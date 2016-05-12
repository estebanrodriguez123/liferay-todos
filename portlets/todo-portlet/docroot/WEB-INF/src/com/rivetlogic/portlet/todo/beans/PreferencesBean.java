package com.rivetlogic.portlet.todo.beans;

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


import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.rivetlogic.portlet.todo.util.Constants;

import java.io.IOException;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

/**
 * @author alejandrosoto
 *
 */
public class PreferencesBean {
	private boolean enableLRCalendarIntegration;
	private String portletId;
	
	public PreferencesBean(PortletRequest request){
		PortletPreferences preferences = request.getPreferences();
		
		portletId = GetterUtil.getString(request.getAttribute(WebKeys.PORTLET_ID));
		enableLRCalendarIntegration = GetterUtil.getBoolean(preferences.getValue(Constants.ENABLE_LR_CALENDAR_INTEGRATION, String.valueOf(false)));
	}
	
	public PreferencesBean(){
		enableLRCalendarIntegration = false;
	}
	
	public void save(PortletRequest request) throws ReadOnlyException, ValidatorException, IOException{
		PortletPreferences preferences = request.getPreferences();
		
		enableLRCalendarIntegration = ParamUtil.getBoolean(request, Constants.ENABLE_LR_CALENDAR_INTEGRATION, enableLRCalendarIntegration);
		
		preferences.setValue(Constants.ENABLE_LR_CALENDAR_INTEGRATION, String.valueOf(enableLRCalendarIntegration));
		
		preferences.store();
	}

	public boolean getEnableLRCalendarIntegration() {
		return enableLRCalendarIntegration;
	}

	public String getPortletId(){
		return portletId;
	}
	
	public void setEnableLRCalendarIntegration(boolean enableLRCalendarIntegration) {
		this.enableLRCalendarIntegration = enableLRCalendarIntegration;
	}
	
	public void setPortletId(String portletId){
		this.portletId = portletId;
	}
}
