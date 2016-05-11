window.tochat = window.tochat || {};
window.tochat.Chat = window.tochat.Chat || {
	V: {},
	M: {},
	C: {}
};

var chat_V = window.tochat.Chat.V;
var chat_M = window.tochat.Chat.M;
var chat_C = window.tochat.Chat.C;
//chat view components
chat_V.templates = {
	applyFromSelf: function(cmJson) {
		var msgObj = chat_M.ChatMessage.create(cmJson);
		var temp = "";
		temp = temp + '<li class="mar-btm">';
		temp = temp + '<div class="media-left">';
		temp = temp + '<img src="http://bootdey.com/img/Content/avatar/avatar1.png" class="img-circle img-sm" alt="Profile Picture" />';
		temp = temp + '</div>';
		temp = temp + '<div class="media-body pad-hor">';
		temp = temp + '<div class="speech">';
		temp = temp + '<a href="#" class="media-heading">' + msgObj.getFromName() + '</a>';
		temp = temp + '<p >' + msgObj.getContent() + '</p>';
		temp = temp + '<p class="speech-time">';
		temp = temp + '<i class="fa fa-clock-o fa-fw"></i>' + msgObj.getFormatTime();
		temp = temp + '</p>';
		temp = temp + '</div>';
		temp = temp + '</div>';
		temp = temp + '</li>';
		return temp;
	},
	applyFromOther: function(cmJson) {
		var msgObj = chat_M.ChatMessage.create(cmJson);
		var temp = "";
		temp = temp + '<li class="mar-btm">';
		temp = temp + '<div class="media-right">';
		temp = temp + '<img src="http://bootdey.com/img/Content/avatar/avatar2.png" class="img-circle img-sm" alt="Profile Picture" />';
		temp = temp + '</div>';
		temp = temp + '<div class="media-body pad-hor speech-right">';
		temp = temp + '<div class="speech">';
		temp = temp + '<a href="#" class="media-heading">' + msgObj.getFromName()  + '</a>';
		temp = temp + '<p>' + msgObj.getContent() + '</p>';
		temp = temp + '<p class="speech-time">';
		temp = temp + '<i class="fa fa-clock-o fa-fw"></i>' + msgObj.getFormatTime();
		temp = temp + '</p>';
		temp = temp + '</div>';
		temp = temp + '</div>';
		temp = temp + '</li>';
		return temp;
	},
	autoApply: function(cmJson) {
		var msgObj = chat_M.ChatMessage.create(cmJson);
		if ( chat_M.current_user) {
			if (msgObj.getFromId() == chat_M.current_user.id) {
				return this.applyFromSelf(cmJson);
			}else{
				return this.applyFromOther(cmJson);
			}
		}
		return "";
	}
};
//chat model definetions
chat_M.current_user = {};//current user
chat_M.online_users = {};//online users map
chat_M.updateOnlineUsers=function(arrUsers){
	var online_users = {};
	for(var i=0,j=arrUsers.length;i<j;i++){
		var userObj = arrUsers[i];
		online_users[userObj.id] = userObj; 
	}
	chat_M.online_users = online_users;
};
chat_M.ChatMessage =function(cmJson){
	this.message = cmJson || {};
	this.getOriginMessage=function(){
		return this.message;
	}
	this.getFrom=function(){
		return chat_M.online_users[this.message["fr"]];
	};
	this.getFromName=function(){
		var userObj = this.getFrom();
		if(userObj){
			return userObj["name"];
		}
		return "";
	};
	this.getFromId=function(){
		return this.message["fr"];
	}
	this.getTo=function(){
		return chat_M.online_users[this.message["to"]];
	};
	this.getContent=function(){
		return this.message["co"];
	};
	this.getRoom=function(){
		return this.message["ro"];
	};
	this.getType=function(){
		return this.message["mt"];
	};
	this.getTime=function(){
		var sendDate = new Date();sendDate.setTime(this.message["ti"]);
		return sendDate;
	};
	this.getFormatTime=function(){
		return $.format.date(this.getTime(),"dd/MM/yy HH:mm:ss");
	}
};
chat_M.ChatMessage.create=function(cmJson){
	return new chat_M.ChatMessage(cmJson);
}

