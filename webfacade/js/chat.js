window.tochat = window.tochat || {};
window.tochat.Chat = window.tochat.Chat || {
	V: {},
	M: {},
	C: {}
};

var chat_V = window.tochat.Chat.V;
var chat_M = window.tochat.Chat.M;
var chat_C = window.tochat.Chat.C;
//////////////////////
//chat view components
chat_V.names = {
	BTN_SEND_MSG: ".sender-btn",
	INPUT_SEND_TEXT: "input.sender-input",
	VIEW_REGION:".viewer-content"
};

chat_V.components = {
	tcWS: {},
	init: function() {
		chat_V.components.tcWS = new TcWebSocket("ws://localhost:8080/tochatserver/chat/" + chat_M.getChannelId());
	}
};

chat_V.templates = {
	applyFromSelf: function(cmJson) {
		var msgObjSupport = chat_M.createMessageSupport(cmJson);
		var temp = "";
		temp = temp + '<li class="mar-btm">';
		temp = temp + '<div class="media-left">';
		temp = temp + '<img src="http://bootdey.com/img/Content/avatar/avatar1.png" class="img-circle img-sm z-depth-1" alt="Profile Picture" />';
		temp = temp + '</div>';
		temp = temp + '<div class="media-body pad-hor">';
		temp = temp + '<div class="speech z-depth-1">';
		temp = temp + '<a href="#" class="media-heading">' + msgObjSupport.getFromName() + '</a>';
		temp = temp + '<p >' + msgObjSupport.getContent() + '</p>';
		temp = temp + '<p class="speech-time">';
		temp = temp + '<i class="zmdi zmdi-time"></i>' + msgObjSupport.getFormatTime();
		temp = temp + '</p>';
		temp = temp + '</div>';
		temp = temp + '</div>';
		temp = temp + '</li>';
		return temp;
	},
	applyFromOther: function(cmJson) {
		var msgObjSupport = chat_M.createMessageSupport(cmJson);
		var temp = "";
		temp = temp + '<li class="mar-btm">';
		temp = temp + '<div class="media-right">';
		temp = temp + '<img src="http://bootdey.com/img/Content/avatar/avatar2.png" class="img-circle img-sm z-depth-1" alt="Profile Picture" />';
		temp = temp + '</div>';
		temp = temp + '<div class="media-body pad-hor speech-right">';
		temp = temp + '<div class="speech z-depth-1">';
		temp = temp + '<a href="#" class="media-heading">' + msgObjSupport.getFromName() + '</a>';
		temp = temp + '<p>' + msgObjSupport.getContent() + '</p>';
		temp = temp + '<p class="speech-time">';
		temp = temp + '<i class="zmdi zmdi-time"></i>' + msgObjSupport.getFormatTime();
		temp = temp + '</p>';
		temp = temp + '</div>';
		temp = temp + '</div>';
		temp = temp + '</li>';
		return temp;
	},
	autoApply: function(cmJson) {
		var msgObjSupport = chat_M.createMessageSupport(cmJson);
		if (chat_M.current_user) {
			if (msgObjSupport.getFrom() == chat_M.current_user.getId()) {
				return this.applyFromSelf(cmJson);
			} else {
				return this.applyFromOther(cmJson);
			}
		}
		return "";
	}
};
//////////////////////
//chat model definetions
chat_M.current_user = {}; //current user : userinfo object
chat_M.online_users = {}; //online users map : id<=>userinfo object
chat_M.current_channel = {}; //current channel : channel object
chat_M.updateOnlineUsers = function(jsonArrUsers) {
	var online_users = {};
	for (var i = 0, j = jsonArrUsers.length; i < j; i++) {
		var jsonUser = jsonArrUsers[i];
		var userObj = new UserInfo(jsonUser);
		var sUId = userObj.getId();
		if (sUId) {
			online_users[sUId] = userObj;
		}
	}
	chat_M.online_users = online_users;
};
chat_M.createMessageSupport = function(jsonObj) {
	var msgObj = new ChatMessage(jsonObj);
	msgObj.getFromName = function() {
		var userObj = chat_M.online_users[this.getFrom()];
		if (userObj) {
			return userObj.getName();
		}
		return "";
	};
	msgObj.getFormatTime = function() {
		var _this =this;
		return $.format.date(Dates.convert(_this.getTimestamp()), "dd/MM/yy HH:mm:ss");
	};
	return msgObj;
};
chat_M.getChannelId = function() {
	var strChannelId = "";
	if (chat_M.current_channel && chat_M.current_channel.getId) {
		strChannelId = chat_M.current_channel.getId();
	}
	if (strChannelId == "") {
		strChannelId = window.location.hash.replace("#", "");
	}
	return strChannelId;
};
chat_M.getInputTxt=function(){
	return encodeURI($(chat_V.names.INPUT_SEND_TEXT).val());
}
////////////////////////
//chat controller
chat_C.init = function() {
	//init view
	chat_V.components.init();

	// init event binder
	$(chat_V.names.BTN_SEND_MSG).unbind('click').click(chat_C.sendTCMessage);
	$(chat_V.names.INPUT_SEND_TEXT).unbind('keydown').keydown();
	// init message handler
	var msgHandlers = new MessageHandlers();
	msgHandlers.addHander(UserInfoMessage, function(jobj) {
		var usermsg = new UserInfoMessage(jobj);
		var useinfo = usermsg.getContent();
		chat_M.current_user = useinfo;
	});
	
	msgHandlers.addHander(TextChatMessage,function(jobj){
		var htmlLiCtx = chat_V.templates.autoApply(jobj);
		$(chat_V.names.VIEW_REGION).append(htmlLiCtx);
	});
	
	msgHandlers.addHander(LoginMessage,function(jobj){
		var loginmsg = new LoginMessage(jobj);
		chat_M.updateOnlineUsers(loginmsg.getJsonUsers());
	});

	chat_V.components.tcWS.addMessageHander(function(evt) {
		console.log(JSON.parse(evt.data));
		msgHandlers.exec(JSON.parse(evt.data));
	});

	// open connection
	chat_V.components.tcWS.openConnection();

};

chat_C.sendTCMessage = function() {
	var txtmsg = new TextChatMessage();
	txtmsg.setFrom(chat_M.current_user.getId());
	//txtmsg.setTo();
	txtmsg.setChannel(chat_M.getChannelId());
	txtmsg.setContent(chat_M.getInputTxt());
	txtmsg.setTimestamp(Date.now());
	chat_V.components.tcWS.sendMessage(txtmsg.stringify());
};

chat_C.sendInputKeydown = function(evt) {
	if (!evt) {
		var evt = window.event;
	}

	if (evt.keyCode == 13) {
		evt.preventDefault(); // sometimes useful
		chat_C.sendTCMessage();
	}
};

chat_C.destroy = function() {
	chat_V.components.tcWS.closeConnection();
	chat_M.current_user = {}; 
	chat_M.online_users = {}; 
	chat_M.current_channel = {}; 
}