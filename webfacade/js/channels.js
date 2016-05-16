window.tochat = window.tochat || {};
window.tochat.Channel = window.tochat.Channel || {
	V: {},
	M: {},
	C: {}
};

var channel_V = window.tochat.Channel.V;
var channel_M = window.tochat.Channel.M;
var channel_C = window.tochat.Channel.C;
//////////////////////
channel_V.names = {
	'LINK_LOAD_MOR': 'a#load-more',
	'CHANNEL_LIST': '#room-list tbody',
	'CHANNEL_LIST_ITEM': '#room-list tbody .room-item'
}

channel_V.components = {
	tcWS: {},
	init: function() {
		channel_V.components.tcWS = new TcWebSocket("ws://localhost:8080/tochatserver/room");
	}
}

channel_V.templates = {
	applyItem: function(channelObj) {
		var temp = "";
		temp = temp + '<tr class="room-item">';
		temp = temp + '<td>' + channelObj.getName() + '</td>';
		temp = temp + '<td>' + channelObj.getDesc() + '</td>';
		temp = temp + '<td>' + channelObj.getOwner() + '</td>';
		temp = temp + '<td>' + channelObj.getCreateon() + '</td>';
		temp = temp + '<td><a id="' + channelObj.getId() + '" class="waves-effect waves-light btn" href="#" onclick="joinChat(\'' + channelObj.getId() + '\')">Join</a></td>';
		temp = temp + '</tr>';
		return temp;
	}
};
//////////////////////
channel_M.current_user = {}; //current user
//////////////////////
channel_C.init = function() {
	channel_V.components.init();

	$(channel_V.names.LINK_LOAD_MOR).unbind('click').click(channel_C.sendFCMessage);

	var msgHandlers = new MessageHandlers();

	msgHandlers.addHander(UserInfoMessage, function(jobj) {
		var usermsg = new UserInfoMessage(jobj);
		var useinfo = usermsg.getContent();
		channel_M.current_user = useinfo;
		if (sessionStorage) {
			sessionStorage.setItem("tochat@user", useinfo.stringify());
		}
	});

	msgHandlers.addHander(FetchChannelsMessage, function(jobj) {
		var fcmsg = new FetchChannelsMessage(jobj);
		var channels = fcmsg.getContent();
		for (var i = 0, j = channels.length; i < j; i++) {
			var channel = channels[i];
			var htmlCtx = channel_V.templates.applyItem(channel);
			$(channel_V.names.CHANNEL_LIST).append(htmlCtx);
		}
	});

	channel_V.components.tcWS.addMessageHander(function(evt) {
		console.log(JSON.parse(evt.data));
		msgHandlers.exec(JSON.parse(evt.data));
	});

	channel_V.components.tcWS.openConnection();
}

channel_C.destroy = function() {
	channel_V.components.tcWS.closeConnection();
}

channel_C.sendFCMessage = function() {
	var fcmsg = new FetchChannelsMessage();
	fcmsg.setOffset($(channel_V.names.CHANNEL_LIST_ITEM).length);
	fcmsg.setFrom(channel_M.current_user.getId());
	fcmsg.setCount(15);
	fcmsg.setTimestamp(new Date().getTime());
	channel_V.components.tcWS.sendMessage(fcmsg.stringify());
}