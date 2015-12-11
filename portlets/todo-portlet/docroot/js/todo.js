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

AUI.add('todo-portlet', function (Y, NAME) {
    var LIFERAY_PHONE_MEDIA_BREAK = 768;
    var TASK_VALIDATION_RULES = {
        title: {
            rangeLength: [4, 75]
        },
        description: {
            required: true
          },
        time: {
            required: true
          }
    };
    
    var SELECT_CALENDAR = '.select-calendar';
    var CHECKBOX_CALENDAR = '.chk-calendar';
    
    Y.Todo = Y.Base.create('todo-portlet', Y.Base, [], {

        lis: null,
        activities: null,
        deleteActivity: null,
        submit: null,
        cancel: null,
        done: null,
        undo: null,
        inputs: null,
        goingUp: null,
        last: null,
        addButton: null,
        toggler: null,
        
        initializer: function () {
            var box = this.get('container');      
            this.addButton = box.one('.add-submit');
            this.goingUp = false,
            this.lastY = 0;
            this.setComponents();
            this.setAddTask();
            this.updateTaskListUI();            
        },
        
        /**
         * Updates tasks list with the data provided
         * 
         */
        updateTaskListUI: function(callback) {
            var me = this;
            var box = this.get('container');
            var dataKeys = ['previousTasks', 'todayTasks', 'tomorrowTasks', 'futureTasks'];
            var containerClasses = ['.previous', '.today', '.tomorrow', '.future'];
            var dateFieldType = (this.get('isMobile') ? "date" : "text");
            this.listTasksCall({}, function(data) {          
                for (var i = 0; i < dataKeys.length; i++) {
                    var tasks = data[dataKeys[i]];
                    var markup = '';
                    var count = "("+tasks.length+")";
                    box.one(containerClasses[i]+'-tasks-count').set('innerHTML',count);
                    for (var j = 0; j < tasks.length; j++) {
                        tasks[j].dateFieldType = dateFieldType;
                        tasks[j].done = (tasks[j].isCompleted) ? "done" : "";
                        markup += Y.Lang.sub(box.one('#' + me.get('portletNamespace') + 'task-list-item-template').get('innerHTML'), tasks[j]);
                    }
                    box.one('.tasks' + containerClasses[i]).empty();
                    box.one('.tasks' + containerClasses[i]).append(markup);
                }
                me.bindTasksList();
                if (typeof callback == 'function') {
                    callback();
                }
            });
        },
        
        /**
         * Creates a calendar component with the given field selector as trigger
         * 
         */
        _createCalendar: function(trigger) {
            var calendarClass = this.get('calendarWidgetClass');
            var datePicker = new calendarClass({
                mask:  '%Y-%m-%d',
                on: {
                    disabledChange: function(event) {

                    }
                    // remove validation which fails when the value is changed through javascript
                    // and not by the user, as in this case
                },
                after: {
                    selectionChange: function(event) {
                        var group = Y.one(".lfr-input-date").get("parentNode");

                            group.removeClass("error").addClass("success");

                            try {
                                group.one(".help-inline").remove(false);
                            }
                            catch(e) {

                            }
                        
                    }
                },
                calendar: {
                    minimumDate: new Date()
                },
                popover: {
                    zIndex: Liferay.zIndex.TOOLTIP
                },
                trigger: trigger
            });
            return datePicker;
        },
        
        /**
         * Binds events to current list
         * 
         */
        bindTasksList: function() {
            var me = this;
            var box = this.get('container');
            this.lis = box.all(".tasks li:not(.add-reminder):not(.done)");
            this.activities = box.all(".activities-list .activity");
            this.deleteActivity = box.all(".activity-delete");
            this.submit = box.all(".edit-submit");
            this.cancel = box.all(".edit-cancel");
            this.done = box.all(".activity-finished");
            this.undo = box.all(".activity-undo");
            this.inputs = box.all(".edit input, .edit button, .edit textarea");
            
            /* initializes calendar and select */
            box.all(".tasks li").each(function(node) {
                me._createCalendar('#' + node.one('.edit-time').get('id'));
                
                // TODO: load the state of the select according the calendarId of the task for now, the select
                // is disabled by default
                node.one(SELECT_CALENDAR).setAttribute("disabled", "disabled");
                node.one(CHECKBOX_CALENDAR).on('change', function (event) { 
                	me.checkHandler(event, node.one(SELECT_CALENDAR));
                });
            });
            /** Shows edit mode when clicking on a task **/
            this.activities.each(function (activity) {
                activity.on("click", function () {
                    var element = me.getMembers(this.get("parentNode"));
                    element.activity.addClass("hide");
                    element.edit.addClass("show");
                    //element.titleInput.set("value",element.title.get("text"));
                    //element.timeInput.set("value",element.time.get("text"));
                });

            });

            /* delete task */
            this.deleteActivity.each(function (button) {
                button.on("click", function (e) {
                    e.stopPropagation();
                    
                    var message = Liferay.Language.get('todo-confirm-task-delete');
                    var actionConfirmation = confirm(message);
            		if (actionConfirmation == true){

	                    var listNode = this.get("parentNode").get("parentNode");
	                    var element = me.getMembers(listNode);
	                    var listHeader = listNode.ancestor('li').one('span.subtitle');
	                    var oldCountStr =  listHeader.one('.taskscount').get('innerHTML').replace("(","").replace(")","");
	                    var oldCount = parseInt(oldCountStr);
	                    var newCount = (oldCount != NaN? oldCount-1:0);
	                    var newCountStr = "("+newCount+")";
	                    me.deleteTaskCall({taskId: element.edit.one('input[type="hidden"]').get('value')}, function() {
	                        element.li.remove(true);
	                        listHeader.one('.taskscount').set('innerHTML', newCountStr);
	                    });
            		}
                });
            });

            /* update task submit */
            this.submit.each(function (button) {
                var cont = button.get("parentNode").get("parentNode").get("parentNode");
                var validator = new Y.FormValidator({
                    boundingBox: cont.one('form'),
                    rules: TASK_VALIDATION_RULES
                });
                button.on("click", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if (cont.all('.error').size() == 0) {
                        var element = me.getMembers(this.get("parentNode").get("parentNode").get("parentNode"));
                        var id = element.edit.one('input[type="hidden"]').get('value');
                        var title = element.edit.one('.edit-title').get('value');
                        var description = element.edit.one('.edit-description').get('value');
                        var date = element.edit.one('.lfr-input-date input').get('value');
                        
                        date = new Date(date);
                        
                        me.updateTaskCall({taskId: id, name: title, description: description, day: (date.getDate() + 1), month: date.getMonth(), year: date.getFullYear()}, function() {
                            me.updateTaskListUI(function() {
                                me.openTaskGroup(id);
                            });
                        });
                    }
                });
            });

            /* cancel the task edit form */
            this.cancel.each(function (button) {
                button.on("click", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    var element = me.getMembers(this.get("parentNode").get("parentNode").get("parentNode"));

                    element.activity.addClass("show").removeClass("hide");
                    element.edit.addClass("hide").removeClass("show");
                });
            });

            /** Sets the function that marks an activity as finished when clicking on the check mark **/
            this.done.each(function (button) {
                button.on("click", function (e) {
                    e.stopPropagation();
                    var element = me.getMembers(this.get("parentNode").get("parentNode"));
                    me.toggleTaskCall({taskId: element.edit.one('input[type="hidden"]').get('value')}, function() {});
                    element.li.addClass("done");
                });
            });

            this.undo.each(function (button) {
                button.on("click", function (e) {
                    e.stopPropagation();
                    var element = me.getMembers(this.get("parentNode").get("parentNode"));
                    me.toggleTaskCall({taskId: element.edit.one('input[type="hidden"]').get('value')}, function() {});
                    element.li.removeClass("done");
                });
            });

            /** Removes bubbling so that edit panel is not shown when clicking on a button **/
            this.inputs.each(function (button) {
                button.on("click", function (e) {
                    e.stopPropagation();
                });
            });
        },
        
        getCalendarId: function(select) {
        	if (select.attr("disabled")) {
        		return undefined;
        	} else {
        		return select.val();
        	}
        },
        
        /**
         * Common ajax wrapper
         * 
         * @param Object configuration
         * { 
         *    method: '',
         *    data: {}
         * }
         * @param String url for testing purposes
         */
        executeAjax: function(configuration, callback, testResponseUrl) {
            var url = testResponseUrl ? testResponseUrl : this.get('resourceUrl');
            configuration.data.ajax_timestamp = new Date().getTime();
            Y.io.request(url, {
                method: configuration.method,
                data: configuration.data,
                dataType: 'json',
                on: {
                    success: function(e) {
                        var data = this.get('responseData');
                        callback(data);
                    }
                }
            });
        },
        
        /**
         * Add task call
         * 
         */
        addTaskCall: function(data, callback) {
            data.cmd = 'add-task';
            this.executeAjax({method: 'POST', data: data}, function(d) {
                callback(d); 
            });
        },
        
        /**
         * Delete
         * 
         */
        deleteTaskCall: function(data, callback) {
            data.cmd = 'delete-task';
            this.executeAjax({method: 'POST', data: data}, function() {
                callback(); 
            });
        },
        
        /**
         * Toggles task from undone to done and viceversa
         * 
         */
        toggleTaskCall: function(data, callback) {
            data.cmd = 'toggle-task';
            this.executeAjax({method: 'POST', data: data}, function() {
                callback(); 
            });
        },
        
        /**
         * Update
         * 
         */
        updateTaskCall: function(data, callback) {
            data.cmd = 'update-task';
            this.executeAjax({method: 'POST', data: data}, function() {
                callback(); 
            });
        },
        
        /**
         * List
         * 
         */
        listTasksCall: function(data, callback) {
            data.cmd = 'list-tasks';
            this.executeAjax({method: 'GET', data: data}, function(d) {
                callback(d); 
            });
        },
        
        /**
         * Retrieve device view port
         * 
         */
        getViewport: function() {
            var e = window, a = 'inner';
            if (!('innerWidth' in window )) {
                a = 'client';
                e = document.documentElement || document.body;
            }
            return { width : e[ a+'Width' ] , height : e[ a+'Height' ] };
        },
        
        /**
         * Configs add task popup
         */
        setAddTask: function () {
            var me = this;
            var modal = new Y.Modal({
                bodyContent: Y.Lang.sub(this.get('container').one('#' +  me.get('portletNamespace') + 'add-task-template').get('innerHTML'), {
                    dateFieldType: (this.get('isMobile') ? "date" : "text")
                }),
                centered: !(me.getViewport().width < LIFERAY_PHONE_MEDIA_BREAK),
                modal: true,
                width: 500,
                headerContent: Liferay.Language.get('add-task'),
                visible: false,
                zIndex: 1001
            }).render();

            var datePicker = this._createCalendar('.add .lfr-input-date input');
 
            var validator = new Y.FormValidator({
                boundingBox: modal.get('boundingBox').one('form'),
                rules: TASK_VALIDATION_RULES
            });
            
            this.addButton.on('click', function (e) {
                modal.set('width', (me.getViewport().width < LIFERAY_PHONE_MEDIA_BREAK ? (me.getViewport().width - 40) : 500));
                modal.get('boundingBox').one(SELECT_CALENDAR).setAttribute("disabled", "disabled");
                modal.show();
            });

            modal.get('boundingBox').one('.add-submit').on('click', function (e) {
                /* trigger validator */
                var title = modal.get('boundingBox').one('.add-title').get('value');
                var description = modal.get('boundingBox').one('.add-description').get('value');
                var date = modal.get('boundingBox').one('.lfr-input-date input').get('value');
                var calendarId = me.getCalendarId(modal.get('boundingBox').one(SELECT_CALENDAR));
                
                e.preventDefault();
                e.stopPropagation();
                
                if (modal.get('boundingBox').all('.error').size() == 0 && Y.Lang.trim(title) != '' && 
                        Y.Lang.trim(description) != '' && Y.Lang.trim(date) != '') {
                    date = new Date(date);
                    modal.get('boundingBox').one('.todo-portlet-loader').toggleClass('visible');
                    modal.get('boundingBox').one('.add-submit').setAttribute('disabled', 'true');
                    me.addTaskCall({name: title, description: description, day: (date.getDate() + 1), month: date.getMonth(), year: date.getFullYear(), calendarId: calendarId}, function(data) {
                        modal.get('boundingBox').one('.todo-portlet-loader').toggleClass('visible');
                        modal.get('boundingBox').one('.add-submit').removeAttribute('disabled');
                        me.updateTaskListUI(function() {
                            me.openTaskGroup(data.taskId);
                        });
                        modal.get('boundingBox').one('form').reset();
                        modal.hide();
                    });
                    
                }
            });

            modal.get('boundingBox').one('.add-cancel').on('click', function (e) {
                modal.hide();
            });
            
            /* 
             *  If the modal is closed using the "x" at the corner, 
             *  the datepicker has to be manually hidden 
             */
            modal.get('boundingBox').one('.close:button').on('click', function (e) {      
            	var popover = datePicker.getPopover();
            	if (popover.get('visible')) {
            		popover.hide();
            	}
            });
            
            modal.get('boundingBox').one(CHECKBOX_CALENDAR).on('change', function (event) {
            	me.checkHandler(event, modal.get('boundingBox').one(SELECT_CALENDAR));
            });
        },
        
        /** Handler for the checkbox to enable or disabled the calendar select **/
        checkHandler: function (event, selectCalendar) {
        	if (selectCalendar) {
        		if (selectCalendar.attr("disabled")) {
	        		selectCalendar.removeAttribute("disabled");
	        	} else {
	        		selectCalendar.setAttribute("disabled", "disabled");
	        	}
        	}
        },
        
        openTaskGroup: function(taskId) {
            var task = this.get('container').one('#task-' + taskId);
            var list = task.get('parentNode');
            list.removeClass('toggler-content-collapsed');
            list.addClass('toggler-content-expanded');
            list.setStyle('margin', '0');
        },
        
        
        
        setComponents: function () {
            /** Toggling tasks categories **/
            this.toggler = new Y.TogglerDelegate({
                animated: true,
                closeAllOnExpand: true,
                container: '#h',
                content: '.tasks',
                expanded: false,
                header: '.subtitle',
                transition: {
                    duration: 0.2,
                    easing: 'cubic-bezier(0, 0.1, 0, 1)'
                }
            });
            
            
        },


        /** Returns the different members of a single task
    @element Must be a li containing a task **/
        getMembers: function (element) {
            var activity = element.one(".activity");
            edit = element.one(".edit");
            title = activity.one(".activity-title");
            time = activity.one(".activity-time");
            titleInput = edit.one(".edit-title");
            timeInput = edit.one(".edit-time");

            return {
                li: element,
                activity: activity,
                edit: edit,
                title: title,
                time: time,
                titleInput: titleInput,
                timeInput: timeInput
            };
        }



    }, {
        ATTRS: {

            portletNamespace: {
                value: ''
            },
            
            container: {
                value: null
            },
            
            resourceUrl: {
                value: ''
            },
            
            calendarWidgetClass: {
                value: null
            },
            
            isMobile: {
                value: false
            }

        }
    });

}, '@VERSION@', {
    "requires": ["yui-base", "base-build", "node", "event",
 "anim",
 "dd-constrain",
 "dd-proxy",
 "dd-drop",
 "aui-datepicker",
 "aui-timepicker",
    "aui-toggler",
    "aui-modal", "aui-io-request", "json-parse", "aui-form-validator"]
});
