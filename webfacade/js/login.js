function hideLoginFailed() {
	$("#login-failed-panel").slideUp("fast")
}

function showLoginPreLoader() {
	$("#login-preloader").fadeIn("fast")
}

function hideLoginPreLoader() {
	$("#login-preloader").fadeOut("fast")
}

function showLoginFailed() {
	$("#login-failed-panel").slideDown("fast")
}

function startNow() {
	updateViewIfLogin();
	if (window.tochat.isLogin()) {
		window.location = getClientUrl("channels.html");
	} else {
		//window.location = getClientUrl("index.html#loginmodal");
		$('#loginmodal').openModal();
	}
}

function login() {
	hideLoginFailed();
	showLoginPreLoader();
	$.ajax({
		type: "post",
		url: getServerUrl("auth/login.htm"),
		async: false,
		data: {
			tochat_name: $("#tochat_name").val(),
			tochat_pass: $("#tochat_pass").val()
		},
		dataType: "jsonp",
		success: function(data) {
			
			hideLoginPreLoader();
			if (data.type == "SUCESS") {
				window.tochat.storeLoginUser(data.other);
				window.location = getClientUrl("channels.html?token=" + data.msg);
			} else {
				setTimeout(showLoginFailed, 1000);
			}
		},
		error: function() {
			showLoginFailed();
		},
		complete: function() {
			hideLoginPreLoader();
		}
	});
	return false;
}

$(document).ready(function() {
	$('.parallax').parallax();
	$('#startBtn').click(startNow);
	updateViewIfLogin();
	tochat.view.enableNavbar();
});