/**
 * All message objects
 */
// base message definition
var Message = Class.create();
Message.prototype = {
	init: function(data) {
		this.data = data || {};
	},
	getData: function() { //return origin json data object
		return this.data;
	},
	getType: function() { // return message type 
		return this.data['mt'];
	},
	getTimestamp: function() { // return message timestamp(number), like Date.now()
		var ts = this.data['ti'];
		try{
			ts = parseInt(ts);
		}catch(e){
			ts = NaN;
		}
		return ts;
	},
	getContent: function() {
		return this.data['co'];
	},
	setType: function(stype) {
		this.data['mt'] = stype;
	},
	setTimestamp: function(lTime) {
		this.data['ti'] = lTime;
	},
	setContent: function(content) {
		this.data['co'] = content;
	},
	stringify: function() {
		return JSON.stringify(this.data);
	}
};
var ChatMessage = Class.extend(Message, {
	init: function(data) {
		this.data = data || {};
	},
	getId: function() { //return message id
		return this.data['id'];
	},
	getFrom: function() {
		return this.data['fr'];
	},
	getChannel: function() {
		return this.data['ro'];
	},
	getTo: function() {
		return this.data['to'];
	},
	setId: function(sid) {
		this.data['id'] = sid;
	},
	setFrom: function(sfromid) {
		this.data['fr'] = sfromid;
	},
	setChannel: function(sChannelId) {
		this.data['ro'] = sChannelId;
	},
	setTo: function(stoid) {
		this.data['to'] = stoid;
	}
});
//message handler
var MessageHandler = Class.create();
MessageHandler.prototype = {
	init: function(fnMsg, fnValue) {
		this.type = fnMsg;
		this.handler = fnValue;
	},
	exec: function(jsonObjData) {
		this.handler.call(this, jsonObjData);
	},
	accept: function(fnMsg_strType) {
		if (this.type.code && (this.type.code == fnMsg_strType || this.type.code == fnMsg_strType.code)) {
			return true;
		}
		return false;
	}
};
//message handlers collections and container
var MessageHandlers = Class.create();
MessageHandlers.prototype = {
	init: function(jsonObjData) {
		this.ctx = [];
		this.data = jsonObjData;
	},
	addHander: function(fnMsg, fnValue) {
		var msgHandler = new MessageHandler(fnMsg, fnValue);
		this.ctx.push(msgHandler);
	},
	exec: function(jsonObjData) {
		var jsonObj = this.data;
		if (jsonObjData) {
			jsonObj = jsonObjData;
		}

		var messageObj = new Message(jsonObj);
		for (var i = 0, j = this.size(); i < j; i++) {
			var msgHandler = this.ctx[i];
			if (msgHandler.accept(messageObj.getType())) {
				try {
					msgHandler.exec(jsonObj);
				} catch (e) {
					console.log(e);
				}
			}
		}
	},
	size: function() {
		return this.ctx.length;
	}
};
//
//Channel definition
var Channel = Class.create();
Channel.prototype = {
	init: function(data) {
		this.data = data || {};
	},
	getId: function() {
		return this.data['id'];
	},
	getName: function() {
		return this.data['name'];
	},
	getOwner: function() {
		return this.data['owner'];
	},
	getActive: function() {
		return this.data['active'];
	},
	getCreateon: function() {
		return this.data['createon'];
	},
	getPeriod: function() {
		return this.data['period'];
	},
	getDesc: function() {
		return this.data['desc'];
	},
	stringify: function() {
		return JSON.stringify(this.data);
	}
};
//User definition
var UserInfo = Class.create();
UserInfo.prototype = {
	init: function(data) {
		this.data = data || {};
		this.data.timestamp = this.data['timestamp'] || Date.now();
	},
	getId: function() {
		return this.data['id'];
	},
	getName: function() {
		return this.data['name'];
	},
	getEmail: function() {
		return this.data['email'];
	},
	getAlias: function() {
		return this.data['alias'];
	},
	getTimestamp: function() {
		return this.data.timestamp;
	},
	stringify: function() {
		return JSON.stringify(this.data);
	}
};
//User message definition
var UserInfoMessage = Class.extend(Message, {
	init: function(data) {
		this.data = data || {};
		this.data['mt'] = 'B00';
	},
	getContent: function() {
		if (this.userObj == undefined)
			this.userObj = new UserInfo(this.data['user']);
		return this.userObj;
	}

}, 'B00');
//Fetch Channels message definition
var FetchChannelsMessage = Class.extend(Message, {
	init: function(data) {
		this.data = data || {};
		this.data['mt'] = 'A01';
	},
	getFrom: function() {
		return this.data['fr'];
	},
	getCount: function() {
		return this.data['cou'];
	},
	getOffset: function() {
		return this.data['of'];
	},
	getContent: function() {
		var channels = [];
		var i = 0,
			j = this.data['co'].length ? this.data['co'].length : 0;
		for (; i < j; i++) {
			var e = this.data['co'][i];
			channels.push(new Channel(e));
		}
		return channels;
	},
	setOffset: function(offset) {
		this.data['of'] = offset;
	},
	setFrom: function(fromUsr) {
		this.data['fr'] = fromUsr;
	},
	setCount: function(lcount) {
		this.data['cou'] = lcount;
	}
}, 'A01');

// Text Chat message definition
var TextChatMessage = Class.extend(ChatMessage, {
	init: function(data) {
		this.data = data || {};
		this.data['mt'] = 'C00';
	}
}, 'C00');

var LoginMessage = Class.extend(ChatMessage, {
	init: function(data) {
		this.data = data || {};
		this.data['mt'] = 'C01';
	},
	getContent: function() {
		var usersObj = [];
		var usersJson = this.getJsonUsers();
		for (var i = 0, j = usersJson.length; i < j; i++) {
			var userinfo = new UserInfo(usersJson[i]);
			usersObj.push(userinfo);
		}
		return usersObj;
	},
	getJsonUsers: function() {
		return this.data['users'] || [];
	}
}, 'C01');

var LogoutMessage = Class.extend(ChatMessage, {
	init: function(data) {
		this.data = data || {};
		this.data['mt'] = 'C02';
	},
	getContent: function() {
		var usersObj = [];
		var usersJson = this.getJsonUsers();
		for (var i = 0, j = usersJson.length; i < j; i++) {
			var userinfo = new UserInfo(usersJson[i]);
			usersObj.push(userinfo);
		}
		return usersObj;
	},
	getJsonUsers: function() {
		return this.data['users'] || [];
	}
}, 'C02');