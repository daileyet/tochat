<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ToChat List</title>
<script   src="https://code.jquery.com/jquery-1.12.3.min.js"   integrity="sha256-aaODHAgvwQW1bFOGXMeX+pC4PZIPsvn2h1sArYOhgXQ="   crossorigin="anonymous"></script>
</head>
<body>
<center>ToChat Room List</center>
<center>
<table border="1" id="room-list">
<tr>
<th>Name</th>
<th>Description</th>
<th>Create By</th>
<th>Create On</th>
<th>&nbsp;</th>
</tr>
<tr>
<td>Java</td>
<td>Discuss Java EE & ME</td>
<td>Dailey</td>
<td>2016/4/29</td>
<td><button>Join</button></td>
</tr>
</table>
<center>
<a name="bottom"></a>
<a href="#bottom" id="load-more">More</a>
</center>
</center>
<script>
	var ws, url = "ws://localhost:8080/tochatserver/room";
	function init() {
		$("a#load-more").click(function(){
			var am = {
					mt:'A01',
					cou:15,
					of: $(".room-item").length,
					fr:'Dailey',
					ti:new Date().getTime()+""
			};
			postToServer(JSON.stringify(am));
		});
		setWebSocket();
	}
	function cleanUp(){
		if(ws && ws.close){
			ws.close()
		}
	}
	
	function setWebSocket() {
		try{
			if(ws && ws.close){
				ws.close()
			}
		}catch(e){console.log(e);}
		ws = new WebSocket(url);
		ws.onmessage = function(message) {
			var amObj = JSON.parse(message.data);
			if(amObj.co.length>0){
				for(var i=0;i<amObj.co.length;i++){
					var rmObj = amObj.co[i];
					var strTR = "<tr class='room-item'><td>{name}</td><td>{desc}</td><td>{owner}</td><td>{createon}</td><td><button id='{id}'>Join</button></td>";
					var strTR = strTR.replace('{name}',rmObj['name'])
					.replace('{desc}',rmObj['desc'])
					.replace('{owner}',rmObj['owner'])
					.replace('{createon}',rmObj['createon'])
					.replace('{id}',rmObj['id']);
					$("#room-list").append(strTR);
				}
			}
		};
		ws.onerror = function(){
			console.log("WebSocket occur error.");
		}
	}
	

	function postToServer(txt) {
		ws.send(txt);
	}
	function closeConnect() {
		ws.close();
	}
	window.addEventListener("load", init, false);
	window.addEventListener("unload", cleanUp, false);
</script>
</body>
</html>