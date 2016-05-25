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
	VIEW_REGION: ".viewer-content",
	VIEW_PANEL: "#chat-viewer",
	VIEW_CONTAINER: "#chat-viewer .viewer-container",
	VIEW_HEADER: "#chat-viewer .viewer-header",
	VIEW_FOOTER: "#chat-viewer .panel-footer",
	USERS_LIST_PANEL: "#chat-users-list",
	USERS_LIST_CONTENT: ".user-list-content",
	USERS_LIST_CONTAINER: "#chat-users-list .users-list-container",
	
	USERS_LIST_HEADER: "#chat-users-list .users-list-header",
	LINK_CHAT_COLLAPSE: "#chat-collapse-link",
	
	LINK_CHAT_EXIT:".exit-item a"
};

chat_V.components = {
	tcWS: {},
	init: function() {
		chat_V.components.chat_users.init();
		chat_V.components.tcWS = new TcWebSocket("ws://localhost:8080/tochatserver/chat/" + chat_M.getChannelId());
	}
};

chat_V.components.chat_viewer = {
	getViewPanelHeight: function() {
		return $(chat_V.names.VIEW_PANEL).height();
	},
	getViewContainerHeight: function() {
		return $(chat_V.names.VIEW_CONTAINER).height();
	},
	getViewHeaderHeight: function() {
		return $(chat_V.names.VIEW_HEADER).height();
	}
};

chat_V.components.chat_users = {
	getUsersListheaderHeight: function() {
		return $(chat_V.names.USERS_LIST_HEADER).height();
	},
	init: function() {
		var ht1 = chat_V.components.chat_viewer.getViewPanelHeight();
		var ht2 = chat_V.components.chat_users.getUsersListheaderHeight();
		$(chat_V.names.USERS_LIST_CONTAINER).css(
			"max-height", (ht1 - ht2) + "px"
		);
	},
	updateView: function() {
		$(chat_V.names.USERS_LIST_CONTENT).empty();
		for (var uid in chat_M.online_users) {
			var userInfo = chat_M.online_users[uid];
			var strItemHtml = chat_V.components.chat_users.templates.applyMemberItem(userInfo);
			$(chat_V.names.USERS_LIST_CONTENT).append(strItemHtml);
		}
		$(chat_V.names.USERS_LIST_CONTENT).collapsible({
			accordion: false
		});
	},
	templates: {
		applyMemberItem: function(useInfoObj) {
			var temp = '';
			temp = temp + '<li class="user-list-item" data-user="' + useInfoObj.getId() + '">';
			temp = temp + '<div class="collapsible-header ">';
			temp = temp + '<i class="zmdi zmdi-face"></i>';
			temp = temp + '' + useInfoObj.getName();
			temp = temp + '</div>';
			temp = temp + '<div class="collapsible-body ">';
			temp = temp + '<p class=""><i class="zmdi zmdi-email"></i>' + useInfoObj.getEmail() + '</p>';
			temp = temp + '</div>';
			temp = temp + '</li>';
			return temp;
		}
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
		var _this = this;
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
chat_M.getInputTxt = function() {
		return encodeURI($(chat_V.names.INPUT_SEND_TEXT).val());
	}
	////////////////////////
	//chat controller
chat_C.init = function() {
	//init view
	chat_V.components.init();
	tochat.view.enableNavbar();
	updateViewIfLogin();
	// init event binder
	//$(chat_V.names.BTN_SEND_MSG).unbind('click').click(chat_C.sendTCMessage);
	$(chat_V.names.LINK_CHAT_EXIT).unbind('click').click();
	$(chat_V.names.INPUT_SEND_TEXT).unbind('keydown').keydown();
	$(chat_V.names.LINK_CHAT_COLLAPSE).unbind('click').click(chat_C.collapseChatPanel);
	$(window).resize(function() {
		chat_V.components.chat_users.init();
	});

	// init message handler
	var msgHandlers = new MessageHandlers();
	msgHandlers.addHander(UserInfoMessage, function(jobj) { // hand user info message
		var usermsg = new UserInfoMessage(jobj);
		var useinfo = usermsg.getContent();
		chat_M.current_user = useinfo;
	});

	msgHandlers.addHander(TextChatMessage, function(jobj) { // hand text chat message
		var htmlLiCtx = chat_V.templates.autoApply(jobj);
		$(chat_V.names.VIEW_REGION).append(htmlLiCtx);
	});

	msgHandlers.addHander(LoginMessage, function(jobj) { // hand user login message
		var loginmsg = new LoginMessage(jobj);
		chat_M.updateOnlineUsers(loginmsg.getJsonUsers());
		chat_V.components.chat_users.updateView();
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
	$(chat_V.names.INPUT_SEND_TEXT).val("");
	return false;
};

chat_C.collapseChatPanel = function() {
	var $link_icon = $("i", chat_V.names.LINK_CHAT_COLLAPSE);
	var willCollapse = $link_icon.hasClass("zmdi-hc-rotate-180");
	if (willCollapse) {
		$(chat_V.names.VIEW_CONTAINER).slideUp();
		$(chat_V.names.VIEW_FOOTER).slideUp();
		if ($(chat_V.names.USERS_LIST_HEADER).hasClass("active")) {
			$(chat_V.names.USERS_LIST_HEADER).trigger('click');
		}
		$link_icon.removeClass("zmdi-hc-rotate-180");
	} else {
		$(chat_V.names.VIEW_CONTAINER).slideDown();
		$(chat_V.names.VIEW_FOOTER).slideDown();
		if (!$(chat_V.names.USERS_LIST_HEADER).hasClass("active")) {
			$(chat_V.names.USERS_LIST_HEADER).trigger('click');
		}
		$link_icon.addClass("zmdi-hc-rotate-180");
	}
}

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