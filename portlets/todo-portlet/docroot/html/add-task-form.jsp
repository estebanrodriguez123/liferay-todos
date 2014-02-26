<%--
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
--%>
<%
Calendar defaultValueDate = CalendarFactoryUtil.getCalendar();
defaultValueDate.setTime(new Date());
%>

<script id="<portlet:namespace/>add-task-template" type="text/x-html-template">
<div class="add">
    <form>

    <div class="control-group">
        <label class="control-label" for="title"><liferay-ui:message key="edit-task-title" /></label>
        <div class="controls">
            <input name="title" type="text" class="add-title field-required"></input>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="description"><liferay-ui:message key="edit-task-description" /></label>
        <div class="controls">
            <textarea name="description" class="add-description" placeholder="<liferay-ui:message key="edit-task-description-placeholder" />"></textarea>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="time"><liferay-ui:message key="edit-task-date" /></label>
        <div class="lfr-input-date controls">
            <input name="time" type="{dateFieldType}" class="edit-time"></input>
        </div>
    </div>
       
        <button class="btn add-submit"><liferay-ui:message key="edit-task-submit" /></button>
        <button class="btn add-cancel"><liferay-ui:message key="edit-task-cancel" /></button>

    </form>
</div>
</script>