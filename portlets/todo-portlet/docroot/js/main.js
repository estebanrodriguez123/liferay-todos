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

YUI().use('node',
	'event',
	'anim',
	'dd-constrain',
	'dd-proxy',
	'dd-drop',
	'aui-datepicker',
	'aui-timepicker',
    'aui-toggler',
	function (Y) {

    /** Portlet variables **/
    var lis = Y.all(".tasks li:not(.add-reminder):not(.done)");
    	activities = Y.all(".activities-list .activity");
    	deleteActivity = Y.all(".activity-delete");
    	submit = Y.all(".edit-submit");
    	cancel = Y.all(".edit-cancel");
    	done = Y.all(".activity-finished");
    	undo = Y.all(".activity-undo");
    	inputs = Y.all(".edit input, .edit button, .edit textarea");
    	goingUp = false,
    	lastY = 0;


    /** Shows edit mode when clicking on a task **/
    activities.each(function(activity){
    	activity.on("click",
    		function() {
    			var element = getMembers(this.get("parentNode"));

    			element.activity.addClass("hide");
    			element.edit.addClass("show");
    			//element.titleInput.set("value",element.title.get("text"));
    			//element.timeInput.set("value",element.time.get("text"));
    		});
    	
    });

    /** Sets the function that removes an activity when clicking on the X button **/
    deleteActivity.each(function(button){
    	button.on("click",
    		function(e) {
    			e.stopPropagation();
    			var element = getMembers(this.get("parentNode").get("parentNode"));
    			element.li.remove(true);
    		});
    });
    	

    submit.each(function(button){
    	button.on("click",
    		function(e) {
    			e.stopPropagation();
    			var element = getMembers(this.get("parentNode").get("parentNode").get("parentNode"));

    			element.activity.addClass("show").removeClass("hide");
    			element.edit.addClass("hide").removeClass("show");
    			//element.title.set("text",element.titleInput.get("value"));
    			//element.time.set("text",element.timeInput.get("value"));
    		});
    });

    cancel.each(function(button){
    	button.on("click",
    		function(e) {
    			e.stopPropagation();
    			var element = getMembers(this.get("parentNode").get("parentNode").get("parentNode"));

    			element.activity.addClass("show").removeClass("hide");
    			element.edit.addClass("hide").removeClass("show");
    		});
    });

    /** Sets the function that marks an activity as finished when clicking on the check mark **/
    done.each(function(button){
    	button.on("click",
    		function(e) {
    			e.stopPropagation();
    			var element = getMembers(this.get("parentNode").get("parentNode"));

    			element.li.addClass("done");
    		});
    });

    undo.each(function(button){
    	button.on("click",
    		function(e) {
    			e.stopPropagation();
    			var element = getMembers(this.get("parentNode").get("parentNode"));

    			element.li.removeClass("done");
    		});
    });

    /** Removes bubbling so that edit panel is not shown when clicking on a button **/
    inputs.each(function(button){
    	button.on("click",
    		function(e){
    			e.stopPropagation();
    		});
    });

    /** Returns the different members of a single task
        @element Must be a li containing a task **/
    function getMembers(element) {
    	var activity = element.one(".activity");
    		edit = element.one(".edit");
    		title = activity.one(".activity-title");
    		time = activity.one(".activity-time");
    		titleInput = edit.one(".edit-title");
    		timeInput = edit.one(".edit-time")
    		;

    	return {
    		li : element,
    		activity: activity,
    		edit: edit,
    		title: title,
    		time: time,
    		titleInput: titleInput,
    		timeInput : timeInput
    	};
    }

    /**************************8888888*** DRAG AND DROP ***************************************/
    /** Sets the function that marks an activity as finished when clicking on the check mark **/
	lis.each(function(li) {
	    var dd = new Y.DD.Drag({
	        node: li,
	        
	        target: {
	            padding: '0 0 0 20'
	        }
	    }).plug(Y.Plugin.DDProxy, {
	        
	        moveOnEnd: false
	    }).plug(Y.Plugin.DDConstrained, {
	        
	        constrain2node: '.tasks'
	    });
	});

    /** Sets the node where the item (task) will be dropped **/
	var drop = new Y.DD.Drop({
        node: '.tasks'
    });

    /** Function that gets triggered when dragging starts **/
    Y.DD.DDM.on('drag:start', function(e) {   
	    var drag = e.target;
	    
	    drag.get('node').setStyle('opacity', '.25');
	    drag.get('dragNode').set('innerHTML', drag.get('node').get('innerHTML'));
	    drag.get('dragNode').addClass("dragged");
	});

    /** Function that gets triggered when dragging ends **/
	Y.DD.DDM.on('drag:end', function(e) {
	    var drag = e.target;
	    
	    drag.get('node').setStyles({
	        visibility: '',
	        opacity: '1'
	    });
	});

    /** Function that gets triggered when dragging on every mouse move **/
	Y.DD.DDM.on('drag:drag', function(e) {
	    //Get the last y point
	    var y = e.target.lastXY[1];
	    
	    goingUp = (y < lastY);

	    lastY = y;
	});

    /** Function that gets triggered when the dragged node is over a drop target **/
	Y.DD.DDM.on('drop:over', function(e) {
	    
	    var drag = e.drag.get('node'),
	        drop = e.drop.get('node');
	    
	    
	    if (drop.get('tagName').toLowerCase() === 'li') {
	        
	        if (!goingUp) {
	            drop = drop.get('nextSibling');
	        }
	        
	        e.drop.get('node').get('parentNode').insertBefore(drag, drop);
	        
	        e.drop.sizeShim();
	    }
	});

    /** Toggling tasks categories **/
    var toggler = new Y.TogglerDelegate(
      {
        animated: true,
        closeAllOnExpand: true,
        container: '#h',
        content: '.tasks',
        expanded: true,
        header: '.subtitle',
        transition: {
          duration: 0.2,
          easing: 'cubic-bezier(0, 0.1, 0, 1)'
        }
      }
    );

});