/**
 * base info js
 */
var SESSION_USER_ITEM_KEY = "tochat@user";
window.tochat = window.tochat || {
	envConf: {
		dev: {
			base_url: {
				client: 'http://127.0.0.1:8020/webfacade/',
				server: 'http://localhost:8080/tochatserver/'
			},
			timeout: 30*60
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
				var userInfo = new UserInfo(JSON.parse(strVal));
				if (userInfo.getId() == undefined || userInfo.getId() == "")
					return false;
				var startTime = userInfo.getTimestamp();
				var isValided = Dates.inRange(Date.now(), startTime, startTime + getSessionTimeOut() * 1000);
				if(!isValided){
					sessionStorage.removeItem(SESSION_USER_ITEM_KEY);
				}
				return isValided;
			} catch (e) {}
		}
		return false;
	},
	getLoginUser: function() {
		if (window.tochat.isLogin()) {
			var strVal = sessionStorage.getItem(SESSION_USER_ITEM_KEY);
			var userObj = new UserInfo(JSON.parse(strVal));
			return userObj;
		}
		return null;
	},
	storeLoginUser: function(userJson) {
		if (sessionStorage) {
			var itemVal = sessionStorage.getItem(SESSION_USER_ITEM_KEY);
			if (itemVal == undefined || itemVal == "") {
				var userinfo = new UserInfo(userJson);
				sessionStorage.setItem(SESSION_USER_ITEM_KEY, userinfo.stringify());
			}
		}
	}
};

function getClientUrl(path) {
	var sPath = '';
	if (path) {
		sPath = '' + path;
	}
	return window.tochat.envConf.current().base_url.client + sPath;
}

function getServerUrl(path) {
	var sPath = '';
	if (path) {
		sPath = '' + path;
	}
	return window.tochat.envConf.current().base_url.server + sPath;
}

function getSessionTimeOut() {
	var stout = window.tochat.envConf.current().timeout || 1800;
	return stout;
}