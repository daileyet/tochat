<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>ToChat v1.0</title>
<script>
	var ws, url = "ws://localhost:8080/tochatserver/chat/{room}";

	function init() {
		setWebSocket();
	}
	
	function setRoom(){
		setWebSocket();
	}

	function setWebSocket() {
		try{
			if(ws && ws.close){
				ws.close()
			}
		}catch(e){console.log(e);}
		var room = document.getElementById("roomlist").value;
		var _url = url.replace("{room}", room);
		ws = new WebSocket(_url);
		ws.onmessage = function(message) {
			console.log(message.data.fr);
			document.getElementById("chatlog").textContent += message.data
					+ "\n";
		};
		ws.onerror = function(){
			console.log("WebSocket occur error.");
		}
	}

	function postToServer() {
		var cm =ChatMessage.create({
			id:"1",
			from:"dailey",
			to:"",
			room:document.getElementById("roomlist").value,
			content:document.getElementById("msg").value
		});
		
		ws.send(cm.format());
		document.getElementById("msg").value = "";
	}
	function closeConnect() {
		ws.close();
	}
	window.addEventListener("load", init, false);
</script>
</head>
<body>
	<div>
		<label>Select a room:</label>
		<select id="roomlist">
			<option value="R1" selected>Room 1</option>
			<option value="R2">Room 2</option>
			<option value="R3">Room 3</option>
		</select>
		<button id="setRoom" onClick="setRoom()">Apply</button>
	</div>

	<textarea id="chatlog" readonly></textarea>
	<br />
	<input id="msg" type="text" />
	<button type="submit" id="sendButton" onClick="postToServer()">Send!</button>
	<button type="submit" id="sendButton" onClick="closeConnect()">End</button>
	<script type="text/javascript">
		function ChatMessage(){
			this.id="";
			this.from="";
			this.to="";
			this.room="";
			this.timestamp=new Date().getTime()+"";
			this.content="";
			
			this.format=function(){
				return '{"mt":"C00","id":"'+this.id+'","ro":"'+this.room+'","fr":"'+this.from+'","to":"'+this.to+'","co":"'+this.content+'","ti":"'+this.timestamp+'"}';
			}
		};
		ChatMessage.create=function(obj){
			var cm = new ChatMessage();
			if(obj==undefined)return cm;
			for(var i in obj){
					cm[i]=obj[i];
			}
			return cm;
		}
			
	</script>
</body>
</html>
