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
			timeout: 30 * 60
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
				if (!isValided) {
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
			try {
				var userObj = new UserInfo(JSON.parse(strVal));
				return userObj;
			} catch (e) {}
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
	},
	removeLoginUser: function() {
		if (sessionStorage) {
			sessionStorage.removeItem(SESSION_USER_ITEM_KEY);
		}
	},
	view: {
		enableNavbar: function() {
			$(".button-collapse").sideNav();
			$('.modal-trigger').leanModal();
			$('.dropdown-button').dropdown({
				inDuration: 300,
				outDuration: 225,
				constrain_width: false, // Does not change width of dropdown to that of the activator
				hover: true, // Activate on hover
				gutter: 0, // Spacing from edge
				belowOrigin: true, // Displays dropdown below the button
				alignment: 'left' // Displays dropdown with edge aligned to the left of button
			});
			$(".logout-item").click(logout);
			
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
/////////////////////

function updateViewIfLogin() {
	var user = window.tochat.getLoginUser();
	if (user && user != null) {
		$('.account-item').removeClass("hide");
		$('.logout-item').removeClass("hide");
		$('.login-item').addClass("hide");
		$('.account-item-text').text(user.getName());
	} else {
		$('.account-item').addClass("hide");
		$('.logout-item').addClass("hide");
		$('.login-item').removeClass("hide");
		$('.account-item-text').empty();
	}
}

function logout() {
	$.ajax({
		type: "post",
		url: getServerUrl("auth/logout.htm"),
		async: false,
		dataType: "jsonp",
		success: function(data) {},
		error: function() {},
		complete: function() {
			window.tochat.removeLoginUser();
			window.location = getClientUrl("index.html");
		}
	});
}