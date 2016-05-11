package com.rivetlogic.portlet.todo;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.util.PortalUtil;
import com.rivetlogic.portlet.todo.beans.PreferencesBean;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

public class ConfigurationAction extends DefaultConfigurationAction {
	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		// TODO Auto-generated method stub
		super.processAction(portletConfig, actionRequest, actionResponse);
		
		PreferencesBean prefBean = new PreferencesBean(actionRequest);
        prefBean.save(actionRequest);
        SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) +
                SessionMessages.KEY_SUFFIX_UPDATED_PREFERENCES);
	}
}
