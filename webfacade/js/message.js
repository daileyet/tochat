/**
 * All message objects
 */

var Message = Class.create();
Message.prototype = {
	init: function(data) {
		this.data= data || {};
	},
	getData:function(){
		return this.data;
	},
	getType:function(){
		return this.data['mt'];
	},
	getTimestamp:function(){
		return this.data['ti'];
	},
	getContent:function(){
		return this.data['co'];
	},
	setTimestamp:function(lTime){
		this.data['ti'] = lTime;
	},
	setContent:function(content){
		this.data['co'] = content;
	},
	stringify:function(){
		return JSON.stringify(this.data);
	}
};

var Channel = Class.create();
Channel.prototype = {
	init:function(data){
		this.data = data;
	},
	getId:function(){
		return this.data['id'];
	},
	getName:function(){
		return this.data['name'];
	},
	getOwner:function(){
		return this.data['owner'];
	},
	getActive:function(){
		return this.data['active'];
	},
	getCreateon:function(){
		return this.data['createon'];
	},
	getPeriod:function(){
		return this.data['period'];
	},
	getDesc:function(){
		return this.data['desc'];
	},
	stringify:function(){
		return JSON.stringify(this.data);
	}
};

var FetchChannelsMessage = Class.extend(Message,{
	init:function(data){
		this.uber.init.call(this,data);
		this.data['mt']='A01';
	},
	getFrom:function(){
		return this.data['fr'];	
	},
	getCount:function(){
		return this.data['cou'];
	},
	getOffset:function(){
		return this.data['of'];
	},
	getContent:function(){
		var channels = [];
		var i=0,j = this.data['co'].length ? this.data['co'].length:0;
		for(;i<j;i++){
			var e = this.data['co'][i];
			channels.push(new Channel(e));
		}
		return channels;
	},
	setOffset:function(offset){
		this.data['of']=offset;
	},
	setFrom:function(fromUsr){
		this.data['fr'] = fromUsr;
	},
	setCount:function(lcount){
		this.data['cou']=lcount;
	}
});
