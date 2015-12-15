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


<script id="<portlet:namespace/>task-list-item-template" type="text/x-html-template">
<li id="task-{taskId}" class="{done}">
    <div class="activity">
        <button class="btn activity-finished icon-check-empty"></button>
        <button class="btn activity-undo icon-check"></button>
        <span class="activity-title">{name}</span> <span
            class="activity-date">{date}</span>
        <button class="btn activity-delete icon-remove-sign"></button>
        <input type="hidden" value="{taskId}" />
    </div>
    <div class="edit">
        <form>
            <input class="edit-task-id" type="hidden" value="{taskId}" />
			<input class="edit-calendar-booking-id" type="hidden" value="{calendarBookingId}" />
            <div class="control-group">
                <label class="control-label" for="title"><liferay-ui:message key="edit-task-title" /></label>
                <div class="controls">
                     <input name="title" type="text" class="edit-title field-required" value="{name}"></input>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="description"><liferay-ui:message key="edit-task-description" /></label>
                <div class="controls">
                    <textarea name="description" class="edit-description field-required" placeholder="<liferay-ui:message key="edit-task-description-placeholder" />">{description}</textarea>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label" for="time"><liferay-ui:message key="edit-task-date" /></label>
                <div class="lfr-input-date controls">
                    <input id="taskCal{taskId}" name="time" type="{dateFieldType}" class="edit-time field-required" value="{date}"></input>
                </div>
            </div>

			<div class="control-group">
				<label class="add-to-calendar"><input {checked} type="checkbox" class="chk-calendar" /> <liferay-ui:message key="edit-task-add-to-calendar" /></label>
       			<div class="controls">            
					<select class="select-calendar">
						<%
						for (com.liferay.calendar.model.Calendar curCalendar : manageableCalendars) {
						%>
						<option {<%= curCalendar.getCalendarId() %>} value="<%= curCalendar.getCalendarId() %>"><%= HtmlUtil.escape(curCalendar.getName(locale)) %></option>
						<%
						}
						%>
					</select>			
        		</div>
			</div>

       
            <button class="btn edit-submit"><liferay-ui:message key="edit-task-submit" /></button>
            <button class="btn edit-cancel"><liferay-ui:message key="edit-task-cancel" /></button>

        </form>
    </div>
</li>
</script>