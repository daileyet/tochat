/**
 * javascript oop class extend
 * @desc:class create and extends, the created class need define the method 'init' at least in its prototype
 * @author:dailey.dai@oracle.com
 * @date:2015/06/18
 * @param win window 
 */
(function(win){
	if (!win['classes']) { //define class cache, user defined class could push in the map
		win['classes'] = {};// user can use the map to define a factory class
	};
	if (!win['Class']) {
		win['Class'] = { //define Class object
			create: function() {
				return function() {
					this.init.apply(this, arguments);
				}
			},
			extend: function(parentClass, extendObj) {// class inherit
				var childClass = Class.create();
				var F = function() {};
				F.prototype = parentClass.prototype;
				childClass.prototype = new F();
				childClass.prototype.constructor = childClass;
				if (extendObj){
					 for(var item in extendObj){
					 	if(extendObj.hasOwnProperty(item)){
					 		childClass.prototype[item] = extendObj[item];
					 	}
					 }
				}
				childClass.prototype.uber = F.prototype;
				return childClass;
			}
		}
	}; //end of define Class object
})(window);

/** Utilities 
** Author: open-thinks@outlook.com
** Current version 1.0 (Mar 22th, 14)
**/
if (typeof String.prototype.trim != 'function') {
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
}
if (typeof String.prototype.startsWith != 'function') {
	// see below for better implementation!
	String.prototype.startsWith = function(str) {
		return this.indexOf(str) === 0;
	};
}
if (typeof String.prototype.toPascalCase != 'function') {
	String.prototype.toPascalCase = function(splaceholder) {
		var placeholder = splaceholder || "";
		var sPascal = this.replace(/([A-Za-z]*)/g, function($1) {
			return $1.slice(0, 1).toUpperCase() + $1.slice(1).toLowerCase();
		});
		return sPascal.replace(/([-\_])/g, placeholder);
	}
}

function isFunction(functionToCheck) {
 var getType = {};
 return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}

function addSheetFile(path, id) {
	if (jQuery("#" + id).length > 0)
		return;
	var fileref = document.createElement("link");
	fileref.rel = "stylesheet";
	fileref.type = "text/css";
	fileref.href = path;
	fileref.media = "screen";
	fileref.id = id;
	var headobj = document.getElementsByTagName('head')[0];
	headobj.appendChild(fileref);
}

Dates = {
	convert: function(d) {
		// Converts the date in d to a date-object. The input can be:
		//   a date object: returned without modification
		//  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
		//   a number     : Interpreted as number of milliseconds
		//                  since 1 Jan 1970 (a timestamp) 
		//   a string     : Any format supported by the javascript engine, like
		//                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
		//  an object     : Interpreted as an object with year, month and date
		//                  attributes.  **NOTE** month is 0-11.
		return (
			d.constructor === Date ? d :
			d.constructor === Array ? new Date(d[0], d[1], d[2]) :
			d.constructor === Number ? new Date(d) :
			d.constructor === String ? new Date(d) :
			typeof d === "object" ? new Date(d.year, d.month, d.date) :
			NaN
		);
	},
	compare: function(a, b) {
		// Compare two dates (could be of any type supported by the convert
		// function above) and returns:
		//  -1 : if a < b
		//   0 : if a = b
		//   1 : if a > b
		// NaN : if a or b is an illegal date
		// NOTE: The code inside isFinite does an assignment (=).
		return (
			isFinite(a = this.convert(a).valueOf()) &&
			isFinite(b = this.convert(b).valueOf()) ?
			(a > b) - (a < b) :
			NaN
		);
	},
	inRange: function(d, start, end) {
		// Checks if date in d is between dates in start and end.
		// Returns a boolean or NaN:
		//    true  : if d is between start and end (inclusive)
		//    false : if d is before start or after end
		//    NaN   : if one or more of the dates is illegal.
		// NOTE: The code inside isFinite does an assignment (=).
		return (
			isFinite(d = this.convert(d).valueOf()) &&
			isFinite(start = this.convert(start).valueOf()) &&
			isFinite(end = this.convert(end).valueOf()) ?
			start <= d && d <= end :
			NaN
		);
	}
}
