package cc.tochat.webserver;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class PEchoServer extends Endpoint {

	@Override
	public void onOpen(Session arg0, EndpointConfig arg1) {
		final Session mySession = arg0;
		mySession.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String arg0) {
				try {
					mySession.getBasicRemote().sendText("P:I got this (" + arg0 + ") so I am sending it back !");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
