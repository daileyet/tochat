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
	if (window.tochat.isLogin()) {
		window.location = getClentUrl("channels.html");
	} else {
		//window.location = getClentUrl("index.html#loginmodal");
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
				console.log(data);
				//window.location = getClentUrl("channels.html?token=" + data.msg);
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

function updateViewIfLogin(){
	var user = window.tochat.getLoginUser();
	if(user && user!=null){
		$('.account-item').removeClass("hide");
		$('.login-item').addClass("hide");
	}else{
		$('.account-item').addClass("hide");
		$('.login-item').removeClass("hide");
	}
}

$(document).ready(function() {
	$('.parallax').parallax();
	$(".button-collapse").sideNav();
	$('.modal-trigger').leanModal();
	$('#startBtn').click(startNow);
	updateViewIfLogin();
});