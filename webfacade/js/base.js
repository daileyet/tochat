/**
 * base info js
 */
var SESSION_USER_ITEM_KEY ="tochat@user";
window.tochat = window.tochat || {
	envConf: {
		dev: {
			base_url: {
				client: 'http://127.0.0.1:8020/webfacade/',
				server: 'http://localhost:8080/tochatserver/'
			},
			timeout:60
		},
		prod: {

		},
		current: function() {
			return window.tochat.envConf.dev;
		}
	},
	isLogin: function() {
		if (sessionStorage && sessionStorage.getItem(SESSION_USER_ITEM_KEY)) {
			var strVal = sessionStorage.getItem(SESSION_USER_ITEM_KEY);
			try {
				var userObj = new UserInfo(JSON.parse(strVal));
				if (userObj.getId() != "") {
					return true;
				}
			} catch (e) {}
		}
		return false;
	},
	getLoginUser:function(){
		if(window.tochat.isLogin()){
			var strVal = sessionStorage.getItem(SESSION_USER_ITEM_KEY);
			var userObj = new UserInfo(JSON.parse(strVal));
			return userObj;
		}
		return null;
	},
	storeLoginUser:function(userinfo){
		if (sessionStorage) {
			var itemVal = sessionStorage.getItem(SESSION_USER_ITEM_KEY);
			if(itemVal==undefined || itemVal ==""){
				sessionStorage.setItem(SESSION_USER_ITEM_KEY, userinfo.stringify());
			}
		}
	}
};

function getClentUrl(path) {
	var sPath = '';
	if(path){
		sPath = ''+path;
	}
	return window.tochat.envConf.current().base_url.client+sPath;
}

function getServerUrl(path) {
	var sPath = '';
	if(path){
		sPath = ''+path;
	}
	return window.tochat.envConf.current().base_url.server+sPath;
}

function getSessionTimeOut(){
	var stout = window.tochat.envConf.current().timeout || 1800;
	return stout;
}
