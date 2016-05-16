/**
 * base info js
 */
window.tochat = window.tochat || {
	envConf: {
		dev: {
			base_url: 'http://127.0.0.1:8020/webfacade/'
		},
		prod: {

		},
		current: function() {
			return window.tochat.envConf.dev;
		}
	},
	isLogin: function() {
		if (sessionStorage && sessionStorage.getItem("tochat@user")) {
			var strVal = sessionStorage.getItem("tochat@user");
			try {
				var userObj = new UserInfo(JSON.parse(strVal));
				if (userObj.getId() != "") {
					return true;
				}
			} catch (e) {}
		}
		return false;
	},
	startNow: function() {
		if (window.tochat.isLogin()) {
			window.location = getFullUrl("channels.html");
		} else {
			window.location = getFullUrl("login.html");
		}
	}
};

function getBaseUrl() {
	return window.tochat.envConf.current().base_url;
};

function getFullUrl(path) {
	return getBaseUrl() + '' + path;
}