/**
 * Js websocket libary
 */
var TcWebSocket = Class.create();
//definition
TcWebSocket.prototype = {
	init:function(strUrl){
		this.url = strUrl;
		this.instance = {};
		this.handlers = {
			"onopen":[],
			"onmessage":[],
			"onerror":[],
			"onclose":[]
		};
	},
	
	openConnection:function(){
		var _this = this;
		this.instance = new WebSocket(this.url);
		
		function execHandlers(handlers,evt){
			for(var i=0,j=handlers.length;i<j;i++){
				var handler = handlers[i];
				
				if(handler && isFunction(handler)){
					
					try{
						handler.call(_this,evt);
					}catch(e){
						console.log(e);
					}
				}
			}
		};
		
		this.instance.onopen = function(evt){
			console.log("onopen");
			var handlers = _this.handlers["onopen"];
			execHandlers(handlers,evt);
		};
		this.instance.onmessage = function(evt){
			console.log("onmessage");
			var handlers = _this.handlers["onmessage"];
			execHandlers(handlers,evt);
		};
		this.instance.onerror = function(evt){
			console.log("onerror");
			var handlers = _this.handlers["onerror"];
			execHandlers(handlers,evt);
		};
		this.instance.onclose = function(evt){
			console.log("onclose");
			var handlers = _this.handlers["onclose"];
			execHandlers(handlers,evt);
		};
		
	},
	
	closeConnection:function(){
		if(this.getStatus() == 1){
			//
		}
		try{
			this.instance.close();	
		}catch(e){
		}
		
	},
	
	sendMessage:function(strMsg){
		this.instance.send(strMsg);
	},
	
	addOpenHander:function(fnHander){
		this.handlers["onopen"].push(fnHander);
	},
	addMessageHander:function(fnHander){
		this.handlers["onmessage"].push(fnHander);
	},
	
	addErrorHander:function(fnHander){
		this.handlers["onerror"].push(fnHander);
	},
	addCloseHander:function(fnHander){
		this.handlers["onclose"].push(fnHander);
	},
	
	getStatus:function(){
		if(this.instance && this.instance.readyState){
			return this.instance.readyState;	
		}
		return "";
	}
	
};
