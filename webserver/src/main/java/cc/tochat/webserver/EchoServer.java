package cc.tochat.webserver;

import java.io.IOException;
import java.util.Set;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo/")
public class EchoServer {

	@OnMessage
	public void echo(String incomingMessage, Session session) {
		Set<Session> openSessions = session.getOpenSessions();
		for (Session sess : openSessions) {
			try {
				sess.getBasicRemote().sendText("I got this (" + incomingMessage + ") so I am sending it back !");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
